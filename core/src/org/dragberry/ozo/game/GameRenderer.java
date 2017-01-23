package org.dragberry.ozo.game;

import org.dragberry.ozo.game.objects.Unit;
import org.dragberry.ozo.game.util.Constants;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Disposable;

public class GameRenderer implements Disposable {
	
	private OrthographicCamera camera;
    private SpriteBatch batch;
	private GameController gameController;
	
	private OrthographicCamera cameraGUI;
	
	private ShapeRenderer shapeRenderer;
	
	private boolean debug = true;

	public GameRenderer(GameController gameController) {
		this.gameController = gameController;
		init();
	}
	
	public void init() {
		batch = new SpriteBatch();
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(-Constants.VIEWPORT_WIDTH / 2, -Constants.VIEWPORT_HEIGHT / 2, 0);
        camera.update();
        
        cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        cameraGUI.position.set(0, 0, 0);
        cameraGUI.setToOrtho(true); // flip y-axis
        cameraGUI.update();
	
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
		renderGame(batch);
		renderDebugInfo(shapeRenderer);
	}
	
	private void renderDebugInfo(ShapeRenderer shapeRenderer) {
		if (!debug) {
			return;
		}
		gameController.cameraHelper.applyTo(camera);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin();
		shapeRenderer.set(ShapeType.Line);
		shapeRenderer.setColor(Color.BLACK);
		float factorX = Constants.VIEWPORT_WIDTH / gameController.gameWidth;
		float factorY = Constants.VIEWPORT_HEIGHT / gameController.gameHeight;
		
		for (int x = 0; x < gameController.gameWidth; x++) {
			float xCoord = x * factorX;
			shapeRenderer.line(xCoord, 0, xCoord, Constants.VIEWPORT_HEIGHT);
		}
		for (int y = 0; y < gameController.gameHeight; y++) {
			float yCoord = y * factorY;
			shapeRenderer.line(0, yCoord, Constants.VIEWPORT_WIDTH, yCoord);
		}
		shapeRenderer.end();
	}

	void renderGame(SpriteBatch batch) {
		gameController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (Unit[] row : gameController.units) {
			for (Unit unit : row) {
				unit.render(batch);
			}
		}
		batch.end();
	}
	
	public void resize(int width, int height) {
        camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
        camera.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

}
