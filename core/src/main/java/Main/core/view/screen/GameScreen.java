package Main.core.view.screen;

import Main.core.controller.GameController;
import Main.core.model.enemy.*;
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

public class GameScreen extends MenuScreen {

    private ShapeRenderer shapeRenderer;
    private BitmapFont gameFont;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private LevelModel levelModel;

    private GameController controller;
    private KnightModel knightModel;

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


    public GameScreen() {
        this.controller = new GameController();
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

        map = new TmxMapLoader().load("maps/Forgotten-Crossroad-Map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera();
        levelModel = new LevelModel();

        camera.setToOrtho(false, 1920, 1080);

        loadCollisionLayer();

        MapLayer spawnLayer = map.getLayers().get("Spawn");

        MapObject spawnObject = spawnLayer.getObjects().get("PlayerSpawn");

        float startX = spawnObject.getProperties().get("x", Float.class);
        float startY = spawnObject.getProperties().get("y", Float.class);

        this.knightModel = new KnightModel(startX, startY);

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

//        falseKnightAtlas = new TextureAtlas(Gdx.files.internal("animations/enemies_atlas/falseKnight.atlas"));
//        falseKnightRenderer = new FalseKnightRenderer(falseKnightAtlas);
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

        if (map.getLayers().get("Hazards") != null) {

            for (MapObject object : map.getLayers().get("Hazards").getObjects()) {

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
    }

    @Override
    public void onLanguageChanged(I18NBundle bundle) {

    }

    @Override
    public void render(float deltaSeconds) {

        handleMenuInputs();

        if (!isPaused) {

            boolean isZoteTalking = levelModel.getZote().getCurrentState() instanceof ZoteTalkingState;

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
        float targetY = (float) knightModel.getY() + (float) (knightModel.getHeight() / 2);

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

        float cameraHalfWidth = camera.viewportWidth * 0.5f;
        float cameraHalfHeight = camera.viewportHeight * 0.5f;

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

            levelModel.getZote().interact();
        }
    }

    private void updateGameLogic(float deltaSeconds) {
        accumulator += deltaSeconds;

        while (accumulator >= fixedDeltaTime) {
            accumulator -= fixedDeltaTime;
            knightModel.update(controller, levelModel, fixedDeltaTime);
            levelModel.updateEnemies(fixedDeltaTime, knightModel);
        }

        updateCamera(deltaSeconds);
    }

    public void renderGameGraphics(float deltaSeconds) {

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.setView(camera);
        mapRenderer.render();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        knightRenderer.render(spriteBatch, deltaSeconds);
        enemyRenderer.render(spriteBatch, levelModel, deltaSeconds);
        spellRenderer.render(spriteBatch, deltaSeconds);
        zoteRenderer.render(spriteBatch, levelModel.getZote(), deltaSeconds);
        dialogueRenderer.render(spriteBatch, levelModel.getZote(), deltaSeconds, camera);
        spriteBatch.end();

        hudRenderer.render(spriteBatch, deltaSeconds);
    }

    public void resetGameSession() {
        this.knightModel = null;
    }
}
