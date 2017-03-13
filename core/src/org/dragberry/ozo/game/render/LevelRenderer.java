package org.dragberry.ozo.game.render;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.GameController;
import org.dragberry.ozo.game.objects.Unit;
import org.dragberry.ozo.game.util.CameraHelper;
import org.dragberry.ozo.game.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class LevelRenderer implements Renderer {

	private static final float LONG_ASPECT_RATIO = 1f / 1.6f;


	private OrthographicCamera camera;
	private GameController gameController;


	private TextureAtlas.AtlasRegion bg = Assets.instance.level.background;
	private float bgOffsetX;
	private float bgOffsetY;
	private float bgZoom;

	public LevelRenderer(GameController gameController) {
		this.gameController = gameController;
		init();
	}

	@Override
	public void render(SpriteBatch batch) {
		CameraHelper.INSTANCE.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);

		TextureAtlas.AtlasRegion bg = Assets.instance.level.background;
		batch.draw(bg, bgOffsetX, bgOffsetY,
				0, 0,
				bg.getRegionWidth(), bg.getRegionHeight(),
				bgZoom, bgZoom, 0);

		Unit selectedUnit = null;
		for (Unit[] row : gameController.units) {
			for (Unit unit : row) {
				if (unit.isSelected()) {
					selectedUnit = unit;
					continue;
				}
				unit.render(batch);
			}
		}
		if (selectedUnit != null) {
			selectedUnit.render(batch);
		}
	}

	private void initBackground() {
		bg = Assets.instance.level.background;
		float bgWidthRatio = ((float) Gdx.graphics.getWidth()) / bg.getRegionWidth();
		float bgHeightRatio = (float) Gdx.graphics.getHeight() / bg.getRegionHeight();
		if (bgWidthRatio > bgHeightRatio) {
			bgOffsetY =  bg.getRegionHeight() *(bgHeightRatio - bgWidthRatio) / 2;
			bgZoom = bgWidthRatio;
		} else {
			bgOffsetX = bg.getRegionWidth() * (bgWidthRatio - bgHeightRatio) / 2;
			bgZoom = bgHeightRatio;
		}
	}

	@Override
	public void init() {
		float height = Gdx.graphics.getHeight() * (Constants.VIEWPORT_WIDTH / Gdx.graphics.getWidth());
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, height);
		camera.position.set(-Constants.VIEWPORT_WIDTH / 2, -height / 2, 0);
		camera.update();

		CameraHelper.INSTANCE.setPosition(
				gameController.level.width * Constants.UNIT_SIZE / 2,
				gameController.level.height * Constants.UNIT_SIZE / 2);
		setZoom();
		initBackground();
	}

	private void setZoom() {
		float screenAspectRatio = camera.viewportWidth / camera.viewportHeight;
		boolean longScreen = screenAspectRatio <= LONG_ASPECT_RATIO;
        float gameAspectRatio = gameController.level.width / gameController.level.height;
        float zoom = 0;
        if (screenAspectRatio > 1 && screenAspectRatio > gameAspectRatio) {
        	zoom = gameController.level.height * Constants.UNIT_SIZE / camera.viewportHeight;
        } else {
        	zoom = gameController.level.width * Constants.UNIT_SIZE / camera.viewportWidth;
        }
		CameraHelper.INSTANCE.setZoom(longScreen ? zoom : zoom * 1.15f);
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
