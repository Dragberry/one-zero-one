package org.dragberry.ozo.game.render;

import org.dragberry.ozo.game.GameController;
import org.dragberry.ozo.game.objects.Unit;
import org.dragberry.ozo.game.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LevelRenderer implements Renderer {
	
	private OrthographicCamera camera;
	private GameController gameController;
	
	public LevelRenderer(GameController gameController) {
		this.gameController = gameController;
		init();
	}

	@Override
	public void render(SpriteBatch batch) {
		gameController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		Unit selectedUnit = null;
		for (Unit[] row : gameController.units) {
			for (Unit unit : row) {
				if (unit.selected) {
					selectedUnit = unit;
					continue;
				}
				unit.render(batch);
			}
		}
		if (selectedUnit != null) {
			selectedUnit.render(batch);
		}
		batch.end();
	}

	@Override
	public void init() {
		float height = Gdx.graphics.getHeight() * (Constants.VIEWPORT_WIDTH / Gdx.graphics.getWidth());
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, height);
        camera.position.set(-Constants.VIEWPORT_WIDTH / 2, -height / 2, 0);
        camera.update();
        
        gameController.cameraHelper.setPosition(
        		gameController.level.width * Constants.UNIT_SIZE / 2, 
        		gameController.level.height * Constants.UNIT_SIZE / 2);
        setZoom();
	}

	private void setZoom() {
		float screenAspectRatio = camera.viewportWidth / camera.viewportHeight;
        float gameAspectRatio = gameController.level.width / gameController.level.height;
        float zoom = 0;
        if (screenAspectRatio > 1 && screenAspectRatio > gameAspectRatio) {
        	zoom = gameController.level.height * Constants.UNIT_SIZE / camera.viewportHeight;
        } else {
        	zoom = gameController.level.width * Constants.UNIT_SIZE / camera.viewportWidth;
        }
        gameController.cameraHelper.setZoom(zoom);
	}
	
	@Override
	public void resize(int width, int height) {
		camera.viewportHeight = (Constants.VIEWPORT_WIDTH / width) * height;
		camera.update();
	}

	@Override
	public GameController getGameContoller() {
		return gameController;
	}
	
	@Override
	public OrthographicCamera getCamera() {
		return camera;
	}

}
