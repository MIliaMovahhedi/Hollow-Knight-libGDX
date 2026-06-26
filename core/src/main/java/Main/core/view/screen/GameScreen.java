package Main.core.view.screen;

import Main.core.controller.GameController;
import Main.core.model.KnightModel;
import Main.core.model.LevelModel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.I18NBundle;

import java.io.StringReader;

public class GameScreen extends MenuScreen {

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private LevelModel levelModel;

    private GameController controller;
    private KnightModel knightModel;

    private int fixedFrameRate;
    private double fixedDeltaTime;
    private double accumulator;

    public GameScreen() {
        this.controller = new GameController();
        this.knightModel = new KnightModel();
    }

    @Override
    public void show() {
        super.show();

        accumulator = 0;
        fixedFrameRate = 120;
        fixedDeltaTime = (double) 1 / fixedFrameRate;

        map = new TmxMapLoader().load("maps/firstMap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera();
        levelModel = new LevelModel();

        loadCollisionLayer();
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
    }

    @Override
    public void onLanguageChanged(I18NBundle bundle) {

    }

    @Override
    public void render(float deltaSeconds) {

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        controller.update();

        camera.position.set((float) knightModel.getX(), (float) knightModel.getY(), 0);
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();

        accumulator += deltaSeconds;

        while (accumulator >= fixedDeltaTime) {

            accumulator -= fixedDeltaTime;
            knightModel.update(controller, levelModel, fixedDeltaTime);
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
}
