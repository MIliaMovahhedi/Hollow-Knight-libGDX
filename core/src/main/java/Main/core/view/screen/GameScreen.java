package Main.core.view.screen;

import Main.core.controller.CheatController;
import Main.core.controller.GameController;
import Main.core.controller.manager.AudioManager;
import Main.core.model.GameState;
import Main.core.model.TransitionDoor;
import Main.core.model.achievement.AchievementManager;
import Main.core.model.enemy.*;
import Main.core.model.enemy.boss.FalseKnightModel;
import Main.core.model.enums.ScreenState;
import Main.core.model.knight.KnightModel;
import Main.core.model.LevelModel;
import Main.core.model.zote.ZoteModel;
import Main.core.model.zote.state.ZoteTalkingState;
import Main.core.view.ScreenManager;
import Main.core.view.renderer.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

public class GameScreen extends MenuScreen {

    private ShapeRenderer shapeRenderer;
    private BitmapFont gameFont;

    private final String MAP_CROSSROADS = "maps/Forgotten-Crossroad-Map.tmx";
    private final String MAP_GREENPATH = "maps/Green-Path-Map.tmx";
    private final String MAP_BOSS_ROOM = "maps/Boss-Room-Map.tmx";

    private String[] maps;
    private int[] backgroundLayers;
    private int[] foregroundLayers;
    private int currentMapIndex = 0;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private LevelModel levelModel;
    private GameController controller;
    private CheatController cheatController;
    private KnightModel knightModel;
    private FalseKnightModel falseKnightModel;
    private KnightRenderer knightRenderer;
    private HUDRenderer hudRenderer;
    private SpellRenderer spellRenderer;
    private EnemyRenderer enemyRenderer;
    private DialogueRenderer dialogueRenderer;
    private ZoteRenderer zoteRenderer;
    private FalseKnightRenderer falseKnightRenderer;
    private TextureAtlas knightAtlas;
    private TextureAtlas hudAtlas;
    private TextureAtlas effectsAtlas;
    private TextureAtlas crawlidAtlas;
    private TextureAtlas mossflyAtlas;
    private TextureAtlas huskAtlas;
    private TextureAtlas guardianAtlas;
    private TextureAtlas zoteAtlas;
    private TextureAtlas falseKnightAtlas;
    private SpriteBatch spriteBatch;

    private int fixedFrameRate;
    private double fixedDeltaTime;
    private double accumulator;
    private boolean isPaused = false;
    private FitViewport viewport;
    private AchievementPopupManager achievementPopupManager;

    public GameScreen() {
        this.controller = new GameController();
        this.cheatController = new CheatController();
    }

    @Override
    public void show() {
        isPaused = false;
        super.show();
        if (knightModel != null) {
            return;
        }

        shapeRenderer = new ShapeRenderer();
        gameFont = new BitmapFont(Gdx.files.internal("ui/trajan.fnt"));
        dialogueRenderer = new DialogueRenderer(gameFont);
        accumulator = 0;
        fixedFrameRate = 120;
        fixedDeltaTime = (double) 1 / fixedFrameRate;
        maps = new String[]{MAP_CROSSROADS, MAP_GREENPATH, MAP_BOSS_ROOM};

        int slot = GameState.getInstance().getCurrentSlotIndex();
        Main.core.model.SaveData data = Main.core.controller.manager.SaveManager.getInstance().loadGame(slot);

        if (data != null) {

            currentMapIndex = data.getMapIndex();
            GameState.getInstance().setGameStats(data.getGameStats());
            AchievementManager.getInstance().loadAchievements(data.getUnlockedAchievements());
        }

        else {

            currentMapIndex = 0;
            GameState.getInstance().resetGameStats();
            AchievementManager.getInstance().loadAchievements(new ArrayList<>());
        }

        loadLevel(currentMapIndex);

        if (data != null) {

            knightModel.teleport(data.getKnightX(), data.getKnightY());
            knightModel.setCurrentHealth(data.getCurrentHealth());
            knightModel.setCurrentSoul(data.getCurrentSoul());

            for (Main.core.model.charm.Charm charm : data.getEquippedCharms()) {

                knightModel.getCharmManager().equipCharm(charm);
            }
        }

        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);
        viewport.apply();

        spriteBatch = new SpriteBatch();
        knightAtlas = new TextureAtlas(Gdx.files.internal("animations/knight_atlas/knight.atlas"));
        knightRenderer = new KnightRenderer(knightModel, knightAtlas);
        hudAtlas = new TextureAtlas(Gdx.files.internal("animations/HUD_atlas/hud.atlas"));
        hudRenderer = new HUDRenderer(knightModel, hudAtlas);
        effectsAtlas = new TextureAtlas(Gdx.files.internal("animations/effects_atlas/effects.atlas"));
        spellRenderer = new SpellRenderer(knightModel, effectsAtlas);
        crawlidAtlas = new TextureAtlas(Gdx.files.internal("animations/enemies_atlas/crawlid.atlas"));
        mossflyAtlas = new TextureAtlas(Gdx.files.internal("animations/enemies_atlas/mossfly.atlas"));
        huskAtlas = new TextureAtlas(Gdx.files.internal("animations/enemies_atlas/husk.atlas"));
        guardianAtlas = new TextureAtlas(Gdx.files.internal("animations/enemies_atlas/guardian.atlas"));
        enemyRenderer = new EnemyRenderer(crawlidAtlas, mossflyAtlas, huskAtlas, guardianAtlas);
        zoteAtlas = new TextureAtlas(Gdx.files.internal("animations/npc_atlas/zote.atlas"));
        zoteRenderer = new ZoteRenderer(zoteAtlas);
        falseKnightAtlas = new TextureAtlas(Gdx.files.internal("animations/enemies_atlas/falseKnight.atlas"));
        falseKnightRenderer = new FalseKnightRenderer(falseKnightAtlas);

        achievementPopupManager = new AchievementPopupManager();
        Main.core.model.achievement.AchievementManager.getInstance().addObserver(achievementPopupManager);
    }

    public void loadLevel(int index) {

        this.currentMapIndex = index;

        if (levelModel != null) {

            if (levelModel.getZote() != null && levelModel.getZote().getCurrentState() != null) {
                levelModel.getZote().getCurrentState().exit(levelModel.getZote());
            }
            for (EnemyModel enemy : levelModel.getEnemies()) {
                if (enemy.getCurrentState() != null) {
                    enemy.getCurrentState().exit(enemy);
                }
            }
        }

        if (map != null)
            map.dispose();
        if (mapRenderer != null)
            mapRenderer.dispose();

        map = new TmxMapLoader().load(maps[index]);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        levelModel = new LevelModel();

        java.util.List<Integer> bgList = new java.util.ArrayList<>();
        java.util.List<Integer> fgList = new java.util.ArrayList<>();
        for (int i = 0; i < map.getLayers().getCount(); i++) {
            com.badlogic.gdx.maps.MapLayer layer = map.getLayers().get(i);
            String name = layer.getName().toLowerCase();
            if (name.contains("front")) {
                fgList.add(i);
            } else {
                bgList.add(i);
            }
        }
        backgroundLayers = new int[bgList.size()];
        for (int i = 0; i < bgList.size(); i++) {
            backgroundLayers[i] = bgList.get(i);
        }
        foregroundLayers = new int[fgList.size()];
        for (int i = 0; i < fgList.size(); i++) {
            foregroundLayers[i] = fgList.get(i);
        }

        loadCollisionLayer();

        MapLayer spawnLayer = map.getLayers().get("Spawn");
        MapObject playerSpawn = spawnLayer.getObjects().get("PlayerSpawn");
        float startX = playerSpawn.getProperties().get("x", Float.class);
        float startY = playerSpawn.getProperties().get("y", Float.class);

        if (this.knightModel == null) {

            this.knightModel = new KnightModel(startX, startY);
        }
        else {

            this.knightModel.teleport(startX, startY);
        }

        MapObject bossSpawnObj = spawnLayer.getObjects().get("bossSpawn");
        if (bossSpawnObj != null) {
            float bx = bossSpawnObj.getProperties().get("x", Float.class);
            float by = bossSpawnObj.getProperties().get("y", Float.class);
            this.falseKnightModel = new FalseKnightModel(bx, by);
            levelModel.addEnemy(this.falseKnightModel);
        }
        else {
            this.falseKnightModel = null;
        }

        float fadeDuration = 1.5f;
        if (index == 2) {
            AudioManager.getInstance().crossfadeMusic(AudioManager.getInstance().bossFightMusic, fadeDuration, true);
        }
        else if (index == 1) {
            AudioManager.getInstance().crossfadeMusic(AudioManager.getInstance().greenpathMusic, fadeDuration, true);
        }
        else {
            AudioManager.getInstance().crossfadeMusic(AudioManager.getInstance().crossroadsMusic, fadeDuration, true);
        }
    }

    public KnightModel getKnightModel() {
        return knightModel;
    }

    private void loadCollisionLayer() {
        if (map.getLayers().get("Ground") != null) {
            for (MapObject object : map.getLayers().get("Ground").getObjects()) {
                if (object instanceof RectangleMapObject) {
                    Rectangle rect = ((RectangleMapObject) object).getRectangle();
                    levelModel.addGroundBound(new Rectangle(rect.x, rect.y, rect.width, rect.height));
                }
            }
        }
        if (map.getLayers().get("Hazard") != null) {
            for (MapObject object : map.getLayers().get("Hazard").getObjects()) {
                if (object instanceof RectangleMapObject) {
                    Rectangle rect = ((RectangleMapObject) object).getRectangle();
                    levelModel.addHazardBound(new Rectangle(rect.x, rect.y, rect.width, rect.height));
                }
            }
        }
        if (map.getLayers().get("Enemies") != null) {
            for (MapObject object : map.getLayers().get("Enemies").getObjects()) {
                float spawnX = object.getProperties().get("x", Float.class);
                float spawnY = object.getProperties().get("y", Float.class);
                String enemyType = object.getName();
                if (enemyType == null) continue;

                EnemyModel enemy = switch (enemyType) {
                    case "Crawlid" -> new CrawlidModel(spawnX, spawnY);
                    case "Mossfly" -> new MossflyModel(spawnX, spawnY);
                    case "Husk" -> new HuskHornHeadModel(spawnX, spawnY);
                    case "Guardian" -> new CrystalGuardianModel(spawnX, spawnY);
                    default -> null;
                };
                if (enemy != null) {
                    levelModel.addEnemy(enemy);
                }
            }
        }
        if (map.getLayers().get("Zote") != null) {
            for (MapObject object : map.getLayers().get("Zote").getObjects()) {
                float spawnX = object.getProperties().get("x", Float.class);
                float spawnY = object.getProperties().get("y", Float.class);
                levelModel.addZote(new ZoteModel(spawnX, spawnY));
            }
        }
        if (map.getLayers().get("Transitions") != null) {

            for (MapObject object : map.getLayers().get("Transitions").getObjects()) {

                if (object instanceof RectangleMapObject) {

                    Rectangle rect = ((RectangleMapObject) object).getRectangle();
                    Integer targetMapIndex = object.getProperties().get("targetMapIndex", Integer.class);

                    if (targetMapIndex != null) {
                        levelModel.addTransitionDoor(new TransitionDoor(rect, targetMapIndex));
                    }
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height, true);
        if (achievementPopupManager != null) {
            achievementPopupManager.resize(width, height);
        }
    }

    @Override
    public void onLanguageChanged(I18NBundle bundle) {
    }

    @Override
    public void render(float deltaSeconds) {
        AudioManager.getInstance().update(deltaSeconds);
        handleMenuInputs();

        if (!isPaused) {
            boolean isZoteTalking = false;
            if (levelModel.getZote() != null) {
                isZoteTalking = levelModel.getZote().getCurrentState() instanceof ZoteTalkingState;
            }

            if (!isZoteTalking) {
                controller.update();
            }
            else {
                knightModel.setXVelocity(0);
            }

            updateGameLogic(deltaSeconds);
            renderGameGraphics(deltaSeconds);
        }
        else {
            renderGameGraphics(0);
        }
    }

    private void updateCamera(float deltaSeconds) {
        float targetX = (float) knightModel.getX() + (float) (knightModel.getWidth() / 2);
        float targetY = (float) knightModel.getY() + (float) (knightModel.getHeight() / 2) + 200;

        float lerpSpeed = 5.0f;
        float lerpFactor = Math.min(lerpSpeed * deltaSeconds, 1.0f);

        camera.position.x += (targetX - camera.position.x) * lerpFactor;
        camera.position.y += (targetY - camera.position.y) * lerpFactor;

        MapProperties mapProperties = map.getProperties();
        int mapWidth = mapProperties.get("width", Integer.class);
        int mapHeight = mapProperties.get("height", Integer.class);
        int tilePixelWidth = mapProperties.get("tilewidth", Integer.class);
        int tilePixelHeight = mapProperties.get("tileheight", Integer.class);

        int mapPixelWidth = mapWidth * tilePixelWidth;
        int mapPixelHeight = mapHeight * tilePixelHeight;

        float cameraHalfWidth = viewport.getWorldWidth() * 0.5f;
        float cameraHalfHeight = viewport.getWorldHeight() * 0.5f;

        camera.position.x = MathUtils.clamp(camera.position.x, cameraHalfWidth, mapPixelWidth - cameraHalfWidth);
        camera.position.y = MathUtils.clamp(camera.position.y, cameraHalfHeight, mapPixelHeight - cameraHalfHeight);

        camera.update();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        map.dispose();
        mapRenderer.dispose();
        knightAtlas.dispose();
        hudAtlas.dispose();
        effectsAtlas.dispose();
        crawlidAtlas.dispose();
        mossflyAtlas.dispose();
        huskAtlas.dispose();
        guardianAtlas.dispose();
        zoteAtlas.dispose();
        dialogueRenderer.dispose();
        AchievementManager.getInstance().removeObserver(achievementPopupManager);
        achievementPopupManager.dispose();
        super.dispose();
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    private void handleMenuInputs() {
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.I)) {
            isPaused = true;
            ScreenManager.getInstance().navigateTo(ScreenState.INVENTORY_MENU);
        }
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            isPaused = true;
            ScreenManager.getInstance().navigateTo(ScreenState.PAUSE_MENU);
        }
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ENTER)) {
            if (levelModel.getZote() != null) {
                levelModel.getZote().interact();
            }
        }
    }

    private void updateGameLogic(float deltaSeconds) {
        if (GameState.getInstance().getGameStats().isGameCompleted()) {
            ScreenManager.getInstance().navigateTo(ScreenState.END_GAME);
            return;
        }

        if (knightModel != null && levelModel != null) {
            cheatController.update(knightModel, levelModel, this);
        }

        GameState.getInstance().getGameStats().addPlayTime(deltaSeconds);
        Main.core.model.achievement.AchievementManager.getInstance().evaluateAchievements(GameState.getInstance().getGameStats());

        accumulator += deltaSeconds;
        while (accumulator >= fixedDeltaTime) {
            accumulator -= fixedDeltaTime;
            knightModel.update(controller, levelModel, fixedDeltaTime);
            levelModel.updateEnemies(fixedDeltaTime, knightModel);
        }

        for (TransitionDoor door : levelModel.getTransitionDoors()) {

            if (knightModel.getBounds().overlaps(door.getBounds())) {

                loadLevel(door.getTargetMapIndex());
                break;
            }
        }

        updateCamera(deltaSeconds);
    }

    public void renderGameGraphics(float deltaSeconds) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.setView(camera);
        mapRenderer.render(backgroundLayers);

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        knightRenderer.render(spriteBatch, deltaSeconds);
        enemyRenderer.render(spriteBatch, levelModel, deltaSeconds);

        if (falseKnightModel != null) {
            falseKnightRenderer.render(spriteBatch, falseKnightModel, deltaSeconds);
        }

        spellRenderer.render(spriteBatch, deltaSeconds);

        if (levelModel.getZote() != null) {
            zoteRenderer.render(spriteBatch, levelModel.getZote(), deltaSeconds);
            dialogueRenderer.render(spriteBatch, levelModel.getZote(), deltaSeconds, camera);
        }

        spriteBatch.end();
        mapRenderer.render(foregroundLayers);

        hudRenderer.render(spriteBatch, deltaSeconds);
        achievementPopupManager.render(deltaSeconds);
    }

    public void resetGameSession() {
        this.knightModel = null;
        if (levelModel != null) {

            if (levelModel.getZote() != null && levelModel.getZote().getCurrentState() != null) {
                levelModel.getZote().getCurrentState().exit(levelModel.getZote());
            }
            for (EnemyModel enemy : levelModel.getEnemies()) {
                if (enemy.getCurrentState() != null) {
                    enemy.getCurrentState().exit(enemy);
                }
            }
        }
        AudioManager.getInstance().stopAll();
    }

    public int getCurrentMapIndex() {
        return currentMapIndex;
    }
}
