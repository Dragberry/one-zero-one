package org.dragberry.ozo.game;

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
	
	private ShapeRenderer shapeRenderer;
	
	private boolean debug = true;

	public GameRenderer(GameController gameController) {
		this.gameController = gameController;
		init();
	}
	
	public void init() {
		batch = new SpriteBatch();
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        initDebug();
        camera.position.set(0, 0 ,0);
        camera.update();
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
		float leftBorder = -Constants.VIEWPORT_WIDTH;
		float rightBorder = Constants.VIEWPORT_WIDTH;
		float topBorder = Constants.VIEWPORT_HEIGHT;
		float bottomBorder = -Constants.VIEWPORT_HEIGHT;
		for (float x = leftBorder; x <= rightBorder; x += 1.0f) {
			shapeRenderer.line(x, bottomBorder, x, topBorder);
		}
		for (float y = bottomBorder; y <= topBorder; y += 1.0f) {
			shapeRenderer.line(leftBorder, y, rightBorder, y);
		}
		shapeRenderer.end();
	}

	void renderGame(SpriteBatch batch) {
		gameController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		gameController.gameArea.render(batch);
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
