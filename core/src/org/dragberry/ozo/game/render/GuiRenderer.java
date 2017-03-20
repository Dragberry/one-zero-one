package org.dragberry.ozo.game.render;

import org.dragberry.ozo.common.util.TimeUtils;
import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.GameController;
import org.dragberry.ozo.game.util.Constants;
import org.dragberry.ozo.game.util.DigitUtil;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;

public class GuiRenderer implements Renderer {

	private static final float DIGIT_STATE_SCALE = 0.8f;
	private final GlyphLayout goal =  new GlyphLayout(Assets.instance.fonts.gui_s, Assets.instance.translation.format("ozo.goal"));
	private final GlyphLayout goalToLose =  new GlyphLayout(Assets.instance.fonts.gui_s, Assets.instance.translation.format("ozo.goalToLose"));
	private final GlyphLayout timeStr = new GlyphLayout(Assets.instance.fonts.gui_l, Assets.instance.translation.format("ozo.time"));
	private final GlyphLayout stepsStr = new GlyphLayout(Assets.instance.fonts.gui_l, Assets.instance.translation.format("ozo.steps"));
	private GlyphLayout steps;

	private OrthographicCamera camera;
	
	public GuiRenderer() {
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
		font.draw(batch, goal, 10, 15);
		font.draw(batch, goalToLose, camera.viewportWidth - goalToLose.width - 10, 15);
		GameController.instance.level.renderGoals(batch, 10.0f, 40.0f);
	}

	private void renderLevelName(SpriteBatch batch) {
		BitmapFont font = Assets.instance.fonts.gui_l;
        font.draw(batch, GameController.instance.level.settings.name,
                camera.viewportWidth / 4, 15, camera.viewportWidth / 2, Align.center, true);
	}

	private void renderSteps(SpriteBatch batch) {
		BitmapFont font = Assets.instance.fonts.gui_l;
		font.draw(batch, stepsStr, camera.viewportWidth - stepsStr.width - 10, camera.viewportHeight - stepsStr.height * 4);
		steps = new GlyphLayout(font, String.valueOf(GameController.instance.level.steps));
		font.draw(batch, steps, camera.viewportWidth - 25 - steps.width, camera.viewportHeight - steps.height * 2);
	}
	
	private void renderTime(SpriteBatch batch) {
		BitmapFont font = Assets.instance.fonts.gui_l;
		font.draw(batch, timeStr, 25, camera.viewportHeight - timeStr.height * 4);
		font.draw(batch, TimeUtils.timeToString((int) GameController.instance.level.time), 25, camera.viewportHeight - timeStr.height * 2);
	}
	
	private void renderState(SpriteBatch batch) {
		float offset = Assets.instance.level.unit.neutral.getRegionHeight();
		TextureRegion ball;

		// Blue ball
		ball = Assets.instance.level.unit.neutral;
		batch.draw(ball.getTexture(),
				camera.viewportWidth / 2 - ball.getRegionWidth() / 2, camera.viewportHeight - offset,
				0, 0,
				ball.getRegionWidth(), ball.getRegionHeight(),
				1, 1,
				0,
				ball.getRegionX(), ball.getRegionY(),
				ball.getRegionWidth(), ball.getRegionHeight(),
				false, true);
		float posX = camera.viewportWidth / 2;
		float upY = camera.viewportHeight - offset + ball.getRegionHeight() / 2 - 0.4f * Assets.instance.level.digits.minus.getRegionHeight();
		float downY = camera.viewportHeight - offset + ball.getRegionHeight() / 2 + DIGIT_STATE_SCALE * Assets.instance.level.digits.minus.getRegionHeight();
		DigitUtil.draw(batch, GameController.instance.zeroCountDigits,
				posX, upY,
				DIGIT_STATE_SCALE, DIGIT_STATE_SCALE,
				0,
				false, true);
		DigitUtil.draw(batch, GameController.instance.lostNumbersDigits,
				posX, downY,
				DIGIT_STATE_SCALE, DIGIT_STATE_SCALE,
				0,
				false, true);
		

		// Green ball
		ball = Assets.instance.level.unit.positive;
		batch.draw(ball.getTexture(),
				camera.viewportWidth / 2 - ball.getRegionWidth() * 1.5f, camera.viewportHeight - offset,
				0, 0,
				ball.getRegionWidth(), ball.getRegionHeight(),
				1, 1,
				0,
				ball.getRegionX(), ball.getRegionY(),
				ball.getRegionWidth(), ball.getRegionHeight(),
				false, true);
		posX = camera.viewportWidth / 2 - ball.getRegionWidth();
		DigitUtil.draw(batch, GameController.instance.posCountDigits,
				posX, upY,
				DIGIT_STATE_SCALE, DIGIT_STATE_SCALE,
				0,
				false, true);
		DigitUtil.draw(batch, GameController.instance.posSumDigits,
				posX, downY,
				DIGIT_STATE_SCALE, DIGIT_STATE_SCALE,
				0,
				false, true);

		// Red ball
		ball = Assets.instance.level.unit.negative;
		batch.draw(ball.getTexture(),
				camera.viewportWidth / 2 + ball.getRegionWidth() / 2, camera.viewportHeight - offset,
				0, 0,
				ball.getRegionWidth(), ball.getRegionHeight(),
				1, 1,
				0,
				ball.getRegionX(), ball.getRegionY(),
				ball.getRegionWidth(), ball.getRegionHeight(),
				false, true);
		
		posX = camera.viewportWidth / 2 + ball.getRegionWidth();
		
		DigitUtil.draw(batch, GameController.instance.negCountDigits,
				posX, upY,
				DIGIT_STATE_SCALE, DIGIT_STATE_SCALE,
				0,
				false, true);
		DigitUtil.draw(batch, GameController.instance.negSumDigits,
				posX, downY,
				DIGIT_STATE_SCALE, DIGIT_STATE_SCALE,
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
