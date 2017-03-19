package org.dragberry.ozo.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Disposable;

import org.dragberry.ozo.game.render.GuiRenderer;
import org.dragberry.ozo.game.render.LevelRenderer;
import org.dragberry.ozo.game.render.Renderer;
import org.dragberry.ozo.game.util.CameraHelper;
import org.dragberry.ozo.game.util.Constants;

public class GameRenderer implements Disposable {
	
	private static final String TAG = GameRenderer.class.getName();

	private static GameRenderer instance;

	public static GameRenderer init() {
		if (instance == null) {
			instance = new GameRenderer();
		}
		return instance;
	}
	
    private SpriteBatch batch;
	private GameController gameController;
	
	private Renderer levelRenderer;
	private Renderer guiRenderer;
	
	private ShapeRenderer shapeRenderer;
	private boolean debug = false;

	public GameRenderer() {
		gameController = GameController.getInstance();
		batch = new SpriteBatch();
		levelRenderer = new LevelRenderer();
		CameraHelper.INSTANCE.camera = levelRenderer.getCamera();
		guiRenderer = new GuiRenderer();
		CameraHelper.INSTANCE.cameraGui = guiRenderer.getCamera();
		initDebug();
	}

	private void initDebug() {
		if (!debug) {
			return;
		}
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
	}

	public void render() {
		batch.begin();
		levelRenderer.render(batch);
		guiRenderer.render(batch);
		batch.end();
		renderDebugInfo(shapeRenderer);
	}
	
	private void renderDebugInfo(ShapeRenderer shapeRenderer) {
		if (!debug) {
			return;
		}
		CameraHelper.INSTANCE.applyTo(levelRenderer.getCamera());
		shapeRenderer.setProjectionMatrix(levelRenderer.getCamera().combined);
		shapeRenderer.begin();
		shapeRenderer.set(ShapeType.Line);
		shapeRenderer.setColor(Color.BLACK);
		
		float width = gameController.level.width * Constants.UNIT_SIZE;
		float height = gameController.level.height * Constants.UNIT_SIZE;
		for (int x = 0; x <= gameController.level.width; x++) {
			float xCoord = x * Constants.UNIT_SIZE;
			shapeRenderer.line(xCoord, 0, xCoord, height);
		}
		for (int y = 0; y <= gameController.level.height; y++) {
			float yCoord = y * Constants.UNIT_SIZE;
			shapeRenderer.line(0, yCoord, width, yCoord);
		}
		shapeRenderer.end();
	}

	public void resize(int width, int height) {
		levelRenderer.resize(width, height);
		guiRenderer.resize(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        if (debug) {
        	shapeRenderer.dispose();
        }
    }

}
