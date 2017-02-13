package org.dragberry.ozo.game.render;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.GameController;
import org.dragberry.ozo.game.util.Constants;
import org.dragberry.ozo.game.util.DigitUtil;
import org.dragberry.ozo.game.util.TimeUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

public class GuiRenderer implements Renderer {
	
	private final GlyphLayout goal =  new GlyphLayout(Assets.instance.fonts.gui_s, Assets.instance.translation.format("ozo.goal"));
	private final GlyphLayout goalToLose =  new GlyphLayout(Assets.instance.fonts.gui_s, Assets.instance.translation.format("ozo.goalToLose"));
	private final GlyphLayout timeStr = new GlyphLayout(Assets.instance.fonts.gui_l, Assets.instance.translation.format("ozo.time"));
	private final GlyphLayout stepsStr = new GlyphLayout(Assets.instance.fonts.gui_l, Assets.instance.translation.format("ozo.steps"));

	private OrthographicCamera camera;
	
	private GameController gameController;
	
	public GuiRenderer(GameController gameController) {
		this.gameController = gameController;
		init();
	}
	
	@Override
	public void init() {
		float height = Gdx.graphics.getHeight() * (Constants.VIEWPORT_GUI_WIDTH / Gdx.graphics.getWidth());
        camera = new OrthographicCamera();
		camera.setToOrtho(true, Constants.VIEWPORT_GUI_WIDTH, height);
        camera.update();
	}
	
	@Override
	public void render(SpriteBatch batch) {
		batch.setProjectionMatrix(camera.combined);
		batch.setColor(Color.WHITE);
		renderTime(batch);
		renderLevelName(batch);
		renderSteps(batch);
		renderState(batch);
		renderGoals(batch);
	}
	
	private void renderGoals(SpriteBatch batch) {
		BitmapFont font = Assets.instance.fonts.gui_s;
		font.setColor(Color.BLACK);
		font.draw(batch, goal, 10, 15);
		font.draw(batch, goalToLose, camera.viewportWidth - goalToLose.width - 10, 15);
		getGameContoller().level.renderGoals(batch, new Vector2(10.0f, 40.0f));
	}

	private void renderLevelName(SpriteBatch batch) {
		BitmapFont font = Assets.instance.fonts.gui_l;
		font.setColor(Color.BLACK);
        font.draw(batch, getGameContoller().level.settings.name,
                camera.viewportWidth / 4, 15, camera.viewportWidth / 2, Align.center, true);
	}

	@Override
	public GameController getGameContoller() {
		return gameController;
	}
	
	private void renderSteps(SpriteBatch batch) {
		BitmapFont font = Assets.instance.fonts.gui_l;
		font.setColor(Color.BLACK);
		font.draw(batch, stepsStr, camera.viewportWidth - stepsStr.width - 10, camera.viewportHeight - stepsStr.height * 4);
		GlyphLayout steps = new GlyphLayout(font, String.valueOf(gameController.level.steps));
		font.draw(batch, steps, camera.viewportWidth - 25 - steps.width, camera.viewportHeight - steps.height * 2);
	}
	
	private void renderTime(SpriteBatch batch) {
		BitmapFont font = Assets.instance.fonts.gui_l;
		font.setColor(Color.BLACK);
		font.draw(batch, timeStr, 25, camera.viewportHeight - timeStr.height * 4);
		font.draw(batch, TimeUtils.timeToString((int) getGameContoller().level.time), 25, camera.viewportHeight - timeStr.height * 2);
	}
	
	private void renderState(SpriteBatch batch) {
		float offset = 105f;
		TextureRegion ball;

		// Blue ball
		ball = Assets.instance.unit.ball;
		batch.setColor(Constants.NEUTRAL);
		batch.draw(ball,
				camera.viewportWidth / 2 - ball.getRegionWidth() / 2, camera.viewportHeight - offset,
				0, 0,
				ball.getRegionWidth(), ball.getRegionHeight(),
				1, 1,
				0);
		DigitUtil.draw(batch, gameController.zeroCountDigits,
				camera.viewportWidth / 2, camera.viewportHeight - offset + ball.getRegionHeight() / 2,
				1, 1,
				0,
				false, true);
		

		// Green ball
		ball = Assets.instance.unit.infoBall;
		batch.setColor(Constants.POSITIVE);
		batch.draw(ball,
				camera.viewportWidth / 2 - ball.getRegionWidth() * 1.5f, camera.viewportHeight - offset,
				0, 0,
				ball.getRegionWidth(), ball.getRegionHeight(),
				1, 1,
				0);
		float countY = camera.viewportHeight - offset + ball.getRegionHeight() / 2 - 0.4f * Assets.instance.digits.minus.getRegionHeight();
		float sumY = camera.viewportHeight - offset + ball.getRegionHeight() / 2 + 0.8f * Assets.instance.digits.minus.getRegionHeight();
		float posX = camera.viewportWidth / 2 - ball.getRegionWidth();
		
		DigitUtil.draw(batch, gameController.posCountDigits,
				posX, countY,
				0.6f, 0.6f,
				0,
				false, true);
		DigitUtil.draw(batch, gameController.posSumDigits,
				posX, sumY,
				0.6f, 0.6f,
				0,
				false, true);

		// Red ball
		batch.setColor(Constants.NEGATIVE);
		batch.draw(ball,
				camera.viewportWidth / 2 + ball.getRegionWidth() / 2, camera.viewportHeight - offset,
				0, 0,
				ball.getRegionWidth(), ball.getRegionHeight(),
				1, 1,
				0);
		
		float negX = camera.viewportWidth / 2 + ball.getRegionWidth();
		
		DigitUtil.draw(batch, gameController.negCountDigits,
				negX, countY,
				0.6f, 0.6f,
				0,
				false, true);
		DigitUtil.draw(batch, gameController.negSumDigits,
				negX, sumY,
				0.6f, 0.6f,
				0,
				false, true);
	}
	
	@Override
	public void resize(int width, int height) {
        camera.viewportHeight = (Constants.VIEWPORT_GUI_WIDTH / width) * height;
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
	}
	
	@Override
	public OrthographicCamera getCamera() {
		return camera;
	}
}
