package Main.core.model.knight;

import Main.core.controller.GameController;
import Main.core.controller.manager.AudioManager;
import Main.core.model.GameState;
import Main.core.model.LevelModel;
import Main.core.model.charm.Charm;
import Main.core.model.charm.CharmManager;
import Main.core.model.charm.CharmObserver;
import Main.core.model.enemy.EnemyModel;
import Main.core.model.knight.knightState.*;
import Main.core.model.knight.spell.HowlingWraiths;
import Main.core.model.knight.spell.Spell;
import Main.core.model.knight.spell.VengefulSpirit;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KnightModel implements CharmObserver {

    private double x;
    private double y;
    private final double startX;
    private final double startY;
    private double lastSafeX;
    private double lastSafeY;
    private double xVelocity;
    private double yVelocity;
    private double width;
    private double height;
    private boolean isDown;
    private boolean isOnHurt;
    Rectangle attackHitbox;

    private double maxHealth;
    private double currentHealth;
    private int currentSoul;
    private double invincibilityDuration;
    private double invincibilityTimer;
    private boolean isInvincible;
    private double damageAmount;

    private List<Spell> activeSpells;

    private boolean isGrounded;
    private boolean canDoubleJump;
    private boolean canDash = true;
    private boolean isFacingRight = true;

    private final double GRAVITY = -900.0;
    private final double MAX_SPEED = 300.0;
    private final double MAX_JUMP_SPEED = 600.0;
    private final double DASH_SPEED = 800f;
    private final double DASH_DURATION = 0.2f;
    private final int MAX_SOUL = 99;
    private final int SOUL_PER_HIT = 11;
    private final int SOUL_PER_ACTION = 33;
    private final double ATTACK_RANGE = 150.0;
    private final double POGO_BOUNCE_SPEED = 750.0;

    private final int baseSoulPerHit = 11;
    private final double baseDamageAmount = 1.0;
    private final double baseAttackDuration = 0.25;
    private final double baseFocusDuration = 1.5;
    private final double baseDashCooldown = 0.6;

    private int currentSoulPerHit;
    private double currentAttackDuration;
    private double currentFocusDuration;
    private double currentDashCooldown;

    private double dashCooldownTimer;

    private CharmManager charmManager;

    private Rectangle bounds;

    private KnightState knightState;

    public KnightModel(double startX, double startY) {

        knightState = new IdleState();

        this.x = startX;
        this.y = startY;
        this.startX = startX;
        this.startY = startY;
        this.xVelocity = 0.0;
        this.yVelocity = 0.0;
        this.width = 55;
        this.height = 85;
        this.isOnHurt = false;

        this.activeSpells = new ArrayList<>();

        this.maxHealth = 5;
        this.currentHealth = this.maxHealth;
        this.currentSoul = 66;
        this.invincibilityDuration = 1.0;
        this.invincibilityTimer = 0.0;
        this.isInvincible = false;
        this.damageAmount = 1.0;

        this.bounds = new Rectangle((float) x, (float) y, (float) width, (float) height);

        this.currentSoulPerHit = baseSoulPerHit;
        this.damageAmount = baseDamageAmount;
        this.currentAttackDuration = baseAttackDuration;
        this.currentFocusDuration = baseFocusDuration;
        this.currentDashCooldown = baseDashCooldown;
        this.dashCooldownTimer = 0.0;

        this.charmManager = new CharmManager();
        this.charmManager.addObserver(this);
    }

    public KnightState getKnightState() {
        return knightState;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }

    public double getMaxSpeed() {
        return MAX_SPEED;
    }
    public double getMaxJumpSpeed() {
        return MAX_JUMP_SPEED;
    }
    public double getGravity() {
        return GRAVITY;
    }

    public boolean isGrounded() {
        return isGrounded;
    }
    public void setGrounded(boolean grounded) {
        isGrounded = grounded;
    }

    public boolean isFacingRight() {
        return isFacingRight;
    }
    public void setFacingRight(boolean facingRight) {
        isFacingRight = facingRight;
    }

    public boolean canDash() { return canDash && dashCooldownTimer <= 0; }
    public void setCanDash(boolean canDash) {
        this.canDash = canDash;
    }
    public void startDashCooldown() {

        dashCooldownTimer = currentDashCooldown;
        canDash = false;
    }

    public double getDashDuration() {
        return DASH_DURATION;
    }
    public double getDashSpeed() {
        return DASH_SPEED;
    }

    public double getBaseAttackDuration() {
        return baseAttackDuration;
    }

    public boolean canDoubleJump() {
        return canDoubleJump;
    }
    public void setCanDoubleJump(boolean canDoubleJump) {
        this.canDoubleJump = canDoubleJump;
    }

    public void setXVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }
    public void setYVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
    }

    public double getXVelocity() {
        return xVelocity;
    }
    public double getYVelocity() {
        return yVelocity;
    }

    private boolean takeDamage(double incomingDamage) {

        if (isInvincible) return true;

        isOnHurt = true;

        AudioManager.getInstance().playSfx(AudioManager.getInstance().knightDamageSound);

        currentHealth -= incomingDamage;
        if (currentHealth <= 0) {

            handleDeath();
            return false;
        }
        else {

            isInvincible = true;
            invincibilityTimer = invincibilityDuration;
            return true;
        }
    }
    private void handleDeath() {

        GameState.getInstance().getGameStats().incrementPlayerDeaths();

        currentHealth = maxHealth;
        this.x = 100.0;
        this.y = 100.0;
        this.xVelocity = 0;
        this.yVelocity = 0;
        bounds.setX((float) x);
        bounds.setY((float) y);
    }

    public Rectangle getAttackHitbox() {
        return attackHitbox;
    }

    public int getCurrentSoul() {
        return currentSoul;
    }
    public double getCurrentHealth() {
        return currentHealth;
    }
    public double getMaxHealth() {
        return maxHealth;
    }
    public void addSoul() {

        if (currentSoul + SOUL_PER_HIT <= MAX_SOUL) {

            AudioManager.getInstance().playScaledSfx(AudioManager.getInstance().soulCollectSound, 0.3f);
        }

        currentSoul = Math.min(currentSoul + SOUL_PER_HIT, MAX_SOUL);
    }
    public boolean consumeSoul() {

        if (currentSoul >= SOUL_PER_ACTION) {

            currentSoul -= SOUL_PER_ACTION;
            return true;
        }
        return false;
    }
    public void heal() {

        if (currentHealth < maxHealth) {

            AudioManager.getInstance().playSfx(AudioManager.getInstance().focusGetSound);
            currentHealth++;
        }
    }
    public boolean isInvincible() {
        return isInvincible;
    }
    public boolean isDown() {
        return isDown;
    }
    public boolean isOnHurt() {
        return isOnHurt;
    }

    public List<Spell> getActiveSpells() {
        return activeSpells;
    }

    public void setKnightState(KnightState knightState) {
        this.knightState.exit(this);
        this.knightState = knightState;
        knightState.enter(this);
    }
    public void update(GameController gameController, LevelModel levelModel, double deltaTime) {

        isOnHurt = false;

        if (dashCooldownTimer > 0) {

            dashCooldownTimer -= deltaTime;
        }

        if (gameController.isOnAttack() && !(this.knightState instanceof AttackState)) {

            isDown = gameController.isOnDownMove() && !isGrounded;

            setKnightState(new AttackState(isDown));
            executeAttack(isDown, levelModel);
        }
        else if (gameController.isOnFocus() && isGrounded && !(knightState instanceof FocusState)) {

            setKnightState(new FocusState());
        }
        else if (gameController.isOnSpell() && !(knightState instanceof CastState)) {

            if (consumeSoul()) {

                boolean isUp = gameController.isOnUpMove();
                setKnightState(new CastState(isUp));
                executeSpell(isUp);
            }
        }

        KnightState newState = this.knightState.update(this, gameController, deltaTime);

        if (newState != this.knightState) {

            setKnightState(newState);
        }

        yVelocity += GRAVITY * newState.getGravityScale() * deltaTime;

        handleGroundBounds(deltaTime, levelModel);
        handleHazardBounds(levelModel);
        handleEnemyCollisions(levelModel);

        if (isInvincible) {

            invincibilityTimer -= deltaTime;
            if (invincibilityTimer <= 0) {

                isInvincible = false;
                invincibilityTimer = 0.0;
            }
        }

        if (isGrounded) {
            lastSafeX = this.x;
            lastSafeY = this.y;
        }

        Iterator<Spell> iterator = activeSpells.iterator();
        while (iterator.hasNext()) {

            Spell spell = iterator.next();
            spell.update(deltaTime, levelModel);

            if (!spell.isActive()) {

                iterator.remove();
            }
        }
    }

    private void respawnAtLastSafePlatform() {
        this.x = lastSafeX;
        this.y = lastSafeY;
        this.xVelocity = 0;
        this.yVelocity = 0;
        bounds.setX((float) x);
        bounds.setY((float) y);
    }
    private void respawnAtStart() {
        this.x = startX;
        this.y = startY;
    }
    private void handleGroundBounds(double deltaTime, LevelModel levelModel) {

        double nextX = this.x + (xVelocity * deltaTime);
        bounds.setX((float) nextX);

        for (Rectangle rect : levelModel.getGroundBounds()) {

            if (bounds.overlaps(rect)) {

                if (xVelocity > 0) {

                    nextX = rect.getX() - bounds.getWidth();
                }
                else if (xVelocity < 0) {

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
                }
                else if (yVelocity < 0) {

                    nextY = rect.getY() + rect.getHeight();
                    yVelocity = 0;
                    isGrounded = true;
                    canDoubleJump = true;
                    canDash = true;
                }
                bounds.setY((float) nextY);
                break;
            }
        }
        this.y = nextY;
    }
    private void handleHazardBounds(LevelModel levelModel) {

        for (Rectangle hazard : levelModel.getHazardBounds()) {

            if (bounds.overlaps(hazard)) {

                if (!isInvincible) {

                    boolean survived = takeDamage(1.0);
                    if (survived) {

                        respawnAtLastSafePlatform();
                    }
                    else {

                        respawnAtStart();
                    }
                }
                break;
            }
        }
    }

    private void executeAttack(boolean isDown, LevelModel levelModel) {

        if (isDown) {

            attackHitbox = new Rectangle((float) x, (float) (y - ATTACK_RANGE), (float) width, (float) ATTACK_RANGE);
        }
        else {

            float hitX = isFacingRight ? (float) (x + width) : (float) (x - ATTACK_RANGE);
            attackHitbox = new Rectangle(hitX, (float) y, (float) ATTACK_RANGE, (float) height);
        }

        checkAttackCollisions(attackHitbox, isDown, levelModel);
    }
    private void checkAttackCollisions(Rectangle attackHitbox, boolean isDown, LevelModel levelModel) {

        boolean hitSuccessful = false;

        for (Rectangle hazard : levelModel.getHazardBounds()) {

            if (attackHitbox.overlaps(hazard)) {

                hitSuccessful = true;
                if (isDown) {

                    applyPogoBounce();
                }
            }
        }

        for (EnemyModel enemy : levelModel.getEnemies()) {

            if (enemy.isDead()) continue;

            if (attackHitbox.overlaps(enemy.getBounds())) {

                hitSuccessful = true;
                enemy.takeDamage(this.damageAmount);

                if (isDown) {

                    applyPogoBounce();
                }
            }
        }

        if (hitSuccessful) {

            addSoul();
        }
    }
    private void applyPogoBounce() {

        AudioManager.getInstance().playSfx(AudioManager.getInstance().pogoSound);
        yVelocity = POGO_BOUNCE_SPEED;
        canDash = true;
        canDoubleJump = true;
    }

    private void executeSpell(boolean isUpwardCast) {

        if (isUpwardCast) {

            activeSpells.add(new HowlingWraiths(x, y, width, height));
        }
        else {
            activeSpells.add(new VengefulSpirit(x, y, width, height, isFacingRight));
        }
    }

    private void handleEnemyCollisions(LevelModel levelModel) {

        for (EnemyModel enemy : levelModel.getEnemies()) {

            if (enemy.isDead()) continue;

            if (bounds.overlaps(enemy.getBounds())) {
                processDamageFromSource(enemy);
            }

            if (enemy instanceof Main.core.model.enemy.CrystalGuardianModel) {

                Rectangle laser = ((Main.core.model.enemy.CrystalGuardianModel) enemy).getActiveLaserBounds();
                if (laser != null && bounds.overlaps(laser)) {

                    processDamageFromSource(enemy);
                }
            }

            if (enemy instanceof Main.core.model.enemy.boss.FalseKnightModel boss) {


                Rectangle mace = boss.getActiveWeaponBounds();
                if (mace != null && bounds.overlaps(mace)) {

                    processDamageFromSource(boss);
                }

                for (Main.core.model.enemy.boss.BossShockWave wave : boss.getActiveShockWaves()) {

                    if (bounds.overlaps(wave.getBounds()) && !wave.hasHitPlayer()) {

                        if (!isInvincible) {

                            boolean survived = takeDamage(wave.getDamageAmount());
                            wave.setHasHitPlayer();

                            if (survived) {

                                this.isGrounded = false;
                                this.yVelocity = 400.0;
                                this.xVelocity = (this.x < wave.getBounds().getX()) ? -500.0 : 500.0;
                            }
                            else {

                                respawnAtStart();
                            }
                        }
                    }
                }
            }
        }
    }
    private void processDamageFromSource(EnemyModel enemy) {

        if (!isInvincible) {

            boolean survived = takeDamage(enemy.getDamageAmount());
            if (survived) {

                applyDamageKnockback(enemy);
            }
            else {

                respawnAtStart();
            }
        }
    }
    private void applyDamageKnockback(EnemyModel enemy) {

        double knightCenterX = this.x + (this.width / 2.0);
        double knightCenterY = this.y + (this.height / 2.0);
        double enemyCenterX = enemy.getX() + (enemy.getWidth() / 2.0);
        double enemyCenterY = enemy.getY() + (enemy.getHeight() / 2.0);

        double overlapX = (this.width / 2.0) + (enemy.getWidth() / 2.0) - Math.abs(knightCenterX - enemyCenterX);
        double overlapY = (this.height / 2.0) + (enemy.getHeight() / 2.0) - Math.abs(knightCenterY - enemyCenterY);

        this.isGrounded = false;

        if (overlapX < overlapY) {

            this.yVelocity = 300.0;
            this.xVelocity = (knightCenterX < enemyCenterX) ? -400.0 : 400.0;
        }
        else {

            if (knightCenterY > enemyCenterY) {

                this.yVelocity = 500.0;
                this.xVelocity = (knightCenterX < enemyCenterX) ? -150.0 : 150.0;
            }
            else {
                this.yVelocity = -300.0;
                this.xVelocity = (knightCenterX < enemyCenterX) ? -400.0 : 400.0;
            }
        }
    }

    @Override
    public void onCharmsUpdated(List<Charm> activeCharms) {

        currentSoulPerHit = baseSoulPerHit;
        damageAmount = baseDamageAmount;
        currentAttackDuration = baseAttackDuration;
        currentFocusDuration = baseFocusDuration;
        currentDashCooldown = baseDashCooldown;

        for (Charm charm : activeCharms) {

            switch (charm) {

                case SOUL_CATCHER -> currentSoulPerHit += 3;
                case UNBREAKABLE_STRENGTH -> damageAmount *= 1.5;
                case QUICK_SLASH -> currentAttackDuration *= 0.6;
                case QUICK_FOCUS -> currentFocusDuration *= 0.6;
                case DASHMASTER -> currentDashCooldown *= 0.5;
            }
        }
    }

    public double getAttackDuration() { return currentAttackDuration; }
    public double getFocusDuration() { return currentFocusDuration; }

    public CharmManager getCharmManager() {
        return charmManager;
    }
}
