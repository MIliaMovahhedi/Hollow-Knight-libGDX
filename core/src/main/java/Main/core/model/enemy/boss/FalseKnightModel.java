package Main.core.model.enemy.boss;

import Main.core.model.GameState;
import Main.core.model.LevelModel;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.enemy.boss.state.*;
import Main.core.model.enemy.state.EnemyState;
import Main.core.model.enemy.state.EnemyTurnState;
import Main.core.model.knight.KnightModel;
import com.badlogic.gdx.math.Rectangle;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class FalseKnightModel extends EnemyModel {

    private final Random random;
    private Class<? extends EnemyState> lastAttackClass = null;

    private int sameAttackCount = 0;
    private int consecutiveHitsReceived = 0;

    private double decisionTimer = 0.0;
    private double hurtTimer = 0.0;

    private boolean isPhaseTwo = false;

    private Rectangle activeWeaponBounds;
    private java.util.List<BossShockWave> activeShockWaves = new java.util.ArrayList<>();

    public FalseKnightModel(double startX, double startY) {

        super(startX, startY, 180, 220, 200.0, 2.0);
        this.random = new Random();
        this.currentState = null;
    }

    @Override
    public void update(double deltaTime, KnightModel knightModel, LevelModel levelModel) {

        if (hurtTimer > 0) {

            hurtTimer = Math.max(hurtTimer - deltaTime, 0.0);
        }

        updateDeath(deltaTime);

        if (!isDead()) {

            if (currentState == null) {

                processDecisionMaking(knightModel, deltaTime);
            } else {

                EnemyState newState = currentState.update(this, knightModel, levelModel, deltaTime);

                if (newState != currentState) {

                    changeState(newState);
                }
            }
        }

        java.util.Iterator<BossShockWave> it = activeShockWaves.iterator();

        while (it.hasNext()) {

            BossShockWave wave = it.next();
            wave.update(deltaTime, levelModel);

            if (!wave.isActive()) {
                it.remove();
            }
        }

        applyGravity(-900.0, deltaTime);
        handlePhysicsBounds(deltaTime, levelModel);
    }

    @Override
    public void takeDamage(double amount) {

        if (this.currentState instanceof StunState) {

            super.takeDamage(amount * 2.0);
        }
        else {

            super.takeDamage(amount);
        }

        if (isDead()) {
            GameState.getInstance().getGameStats().setFalseKnightDefeated(true);
            GameState.getInstance().getGameStats().setGameCompleted(true);
        }

        hurtTimer = 0.2;
        consecutiveHitsReceived++;

        if (this.health <= (this.maxHealth / 2) && !isPhaseTwo) {

            isPhaseTwo = true;
            changeState(new StunState());
        }
    }

    private void processDecisionMaking(KnightModel knightModel, double deltaTime) {

        decisionTimer += deltaTime;

        double currentDecisionDelay = isPhaseTwo ? 0.3 : 0.7;

        if (decisionTimer >= currentDecisionDelay) {

            EnemyState nextAttack = evaluateNextMove(knightModel);

            consecutiveHitsReceived = 0;
            decisionTimer = 0.0;

            changeState(nextAttack);
        }
    }
    private EnemyState evaluateNextMove(KnightModel knightModel) {

        double distanceToPlayer = Math.abs(this.x - knightModel.getX());
        NavigableMap<Double, EnemyState> roulette = new TreeMap<>();
        double totalWeight = 0.0;


        if (consecutiveHitsReceived >= 3) {

            return wrapWithTurnIfNeeded(new DefensiveLeapState(), knightModel);
        }


        if (distanceToPlayer < 200.0) {

            if (lastAttackClass != MaceSlamState.class || sameAttackCount < 2) {

                totalWeight += 60.0;
                roulette.put(totalWeight, new MaceSlamState());
            }
            if (lastAttackClass != OffensiveLeapState.class || sameAttackCount < 2) {

                totalWeight += 20.0;
                roulette.put(totalWeight, new OffensiveLeapState());
            }
        }
        else {

            if (lastAttackClass != ChargeState.class || sameAttackCount < 2) {

                totalWeight += 70.0;
                roulette.put(totalWeight, new ChargeState());
            }
            if (lastAttackClass != OffensiveLeapState.class || sameAttackCount < 2) {

                totalWeight += 30.0;
                roulette.put(totalWeight, new OffensiveLeapState());
            }
            if (isPhaseTwo) {

                if (lastAttackClass != ShockWaveLandingState.class || sameAttackCount < 2) {

                    totalWeight += 40.0;
                    roulette.put(totalWeight, new ShockWaveLandingState());
                }
            }
        }


        if (roulette.isEmpty()) {

            return wrapWithTurnIfNeeded(new MaceSlamState(), knightModel);
        }

        double randomSpin = random.nextDouble() * totalWeight;
        EnemyState selectedState = roulette.higherEntry(randomSpin).getValue();

        if (lastAttackClass == selectedState.getClass()) {

            sameAttackCount++;
        }
        else {

            sameAttackCount = 1;
            lastAttackClass = selectedState.getClass();
        }

        return wrapWithTurnIfNeeded(selectedState, knightModel);
    }

    private void handlePhysicsBounds(double deltaTime, LevelModel levelModel) {

        double nextX = this.x + (xVelocity * deltaTime);
        bounds.setX((float) nextX);
        for (Rectangle rect : levelModel.getGroundBounds()) {
            if (bounds.overlaps(rect)) {
                if (xVelocity > 0) {
                    nextX = rect.getX() - bounds.getWidth();
                } else if (xVelocity < 0) {
                    nextX = rect.getX() + rect.getWidth();
                }
                xVelocity = 0;
                bounds.setX((float) nextX);
                break;
            }
        }
        this.x = nextX;

        double nextY = this.y + (yVelocity * deltaTime);
        bounds.setY((float) nextY);
        isGrounded = false;
        for (Rectangle rect : levelModel.getGroundBounds()) {
            if (bounds.overlaps(rect)) {
                if (yVelocity > 0) {
                    nextY = rect.getY() - bounds.getHeight();
                    yVelocity = 0;
                } else if (yVelocity < 0) {
                    nextY = rect.getY() + rect.getHeight();
                    yVelocity = 0;
                    isGrounded = true;
                }
                bounds.setY((float) nextY);
                break;
            }
        }
        this.y = nextY;
    }

    public void setActiveWeaponBounds(Rectangle bounds) {
        this.activeWeaponBounds = bounds;
    }
    public Rectangle getActiveWeaponBounds() {
        return activeWeaponBounds;
    }

    public java.util.List<BossShockWave> getActiveShockWaves() {
        return activeShockWaves;
    }
    public void triggerShockWaves() {

        double centerX = this.x + (this.width / 2.0) - 20.0;
        activeShockWaves.add(new BossShockWave(centerX, this.y, true));
        activeShockWaves.add(new BossShockWave(centerX, this.y, false));
    }

    private EnemyState wrapWithTurnIfNeeded(EnemyState nextState, KnightModel knightModel) {

        boolean playerIsRight = knightModel.getX() > this.x;

        if (this.isFacingRight != playerIsRight) {

            return new EnemyTurnState(nextState);
        }

        return nextState;
    }

    public double getHurtTimer() {
        return this.hurtTimer;
    }
    public boolean isHurt() {
        return hurtTimer > 0.0;
    }
}
