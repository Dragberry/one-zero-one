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
	private static final float UI_PANELS_SCALE = Constants.VIEWPORT_GUI_WIDTH / 480f;
	private final GlyphLayout goal =  new GlyphLayout(Assets.instance.fonts.gui_s, Assets.instance.translation.format("ozo.goal"));
	private final GlyphLayout goalToLose =  new GlyphLayout(Assets.instance.fonts.gui_s, Assets.instance.translation.format("ozo.goalToLose"));
	private final GlyphLayout timeStr = new GlyphLayout(Assets.instance.fonts.gui_l, Assets.instance.translation.format("ozo.time"));
	private final GlyphLayout stepsStr = new GlyphLayout(Assets.instance.fonts.gui_l, Assets.instance.translation.format("ozo.steps"));
	private GlyphLayout steps;

	private OrthographicCamera camera;

	private int uiTopPanelWidth;
	private int uiTopPanelHeight;
	private int uiBottomPanelWidth;
	private int uiBottomPanelHeight;
	private float uiBottomPanelY;

	private TextureRegion frame = Assets.instance.level.unit.frame;
	private TextureRegion body = Assets.instance.level.unit.body;
	private float stateOffset;
	private float stateY;
	private float stateNeutralX;
	private float stateNegativeX;
	private float statePositiveX;
	private int stateFrameWidth;
	private int stateFrameHeight;
	
	public GuiRenderer() {
		init();
	}
	
	@Override
	public void init() {
		float height = Gdx.graphics.getHeight() * (Constants.VIEWPORT_GUI_WIDTH / Gdx.graphics.getWidth());
        camera = new OrthographicCamera();
		camera.setToOrtho(true, Constants.VIEWPORT_GUI_WIDTH, height);
        camera.update();

		uiTopPanelWidth = Assets.instance.level.uiTop.getRegionWidth();
		uiTopPanelHeight = Assets.instance.level.uiTop.getRegionHeight();
		uiBottomPanelWidth = Assets.instance.level.uiTop.getRegionWidth();
		uiBottomPanelHeight = Assets.instance.level.uiTop.getRegionHeight();
		uiBottomPanelY =  camera.viewportHeight - uiBottomPanelHeight * UI_PANELS_SCALE;

		stateOffset = frame.getRegionHeight();
		stateY = camera.viewportHeight - stateOffset;
		stateNeutralX = camera.viewportWidth / 2 - frame.getRegionWidth() / 2;
		stateNegativeX = camera.viewportWidth / 2 + frame.getRegionWidth() / 2;
		statePositiveX = camera.viewportWidth / 2 - frame.getRegionWidth() * 1.5f;
		stateFrameWidth = frame.getRegionWidth();
		stateFrameHeight = frame.getRegionHeight();
	}
	
	@Override
	public void render(SpriteBatch batch) {
		batch.setProjectionMatrix(camera.combined);
		batch.setColor(Color.WHITE);

		renderTopUI(batch);
		renderBottomUI(batch);

		renderTime(batch);
		renderLevelName(batch);
		renderSteps(batch);
		renderState(batch);
		renderGoals(batch);
	}

	private void renderTopUI(SpriteBatch batch) {
		batch.draw(Assets.instance.level.uiTop,
				0, 0,
				0, 0,
				uiTopPanelWidth, uiTopPanelHeight,
				UI_PANELS_SCALE, UI_PANELS_SCALE, 0);
	}

	private void renderBottomUI(SpriteBatch batch) {
		batch.draw(Assets.instance.level.uiBottom,
				0, uiBottomPanelY,
				0, 0,
				uiBottomPanelWidth, uiBottomPanelHeight,
				UI_PANELS_SCALE, UI_PANELS_SCALE, 0);
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
		// Blue frame
		batch.setColor(Constants.NEUTRAL);
		batch.draw(body.getTexture(),
				stateNeutralX, stateY,
				stateFrameWidth / 2, stateFrameHeight / 2,
				stateFrameWidth, stateFrameHeight,
				0.8f, 0.8f,
				0,
				body.getRegionX(), body.getRegionY(),
				body.getRegionWidth(), body.getRegionHeight(),
				false, true);

		batch.setColor(Color.WHITE);
		batch.draw(frame.getTexture(),
				stateNeutralX, stateY,
				0, 0,
				stateFrameWidth, stateFrameHeight,
				1, 1,
				0,
				frame.getRegionX(), frame.getRegionY(),
				stateFrameWidth, stateFrameHeight,
				false, true);

		float posX = camera.viewportWidth / 2;
		float upY = camera.viewportHeight - stateOffset + frame.getRegionHeight() / 2 - 0.4f * Assets.instance.level.digits.minus.getRegionHeight();
		float downY = camera.viewportHeight - stateOffset + frame.getRegionHeight() / 2 + DIGIT_STATE_SCALE * Assets.instance.level.digits.minus.getRegionHeight();

		batch.setColor(Constants.NEUTRAL_TXT);
		DigitUtil.draw(batch, GameController.instance.level.zeroCountDigits,
				posX, upY,
				DIGIT_STATE_SCALE, DIGIT_STATE_SCALE,
				0,
				false, true);
		DigitUtil.draw(batch, GameController.instance.level.lostNumbersDigits,
				posX, downY,
				DIGIT_STATE_SCALE, DIGIT_STATE_SCALE,
				0,
				false, true);
		batch.setColor(Color.WHITE);
		

		// Green frame
		batch.setColor(Constants.POSITIVE);
		batch.draw(body.getTexture(),
				statePositiveX, stateY,
				stateFrameWidth / 2, stateFrameHeight / 2,
				stateFrameWidth, stateFrameHeight,
				0.8f, 0.8f,
				0,
				body.getRegionX(), body.getRegionY(),
				body.getRegionWidth(), body.getRegionHeight(),
				false, true);

		batch.setColor(Color.WHITE);

		batch.draw(frame.getTexture(),
				statePositiveX, stateY,
				0, 0,
				stateFrameWidth, stateFrameHeight,
				1, 1,
				0,
				frame.getRegionX(), frame.getRegionY(),
				stateFrameWidth, stateFrameHeight,
				false, true);
		posX = camera.viewportWidth / 2 - frame.getRegionWidth();

		batch.setColor(Constants.POSITIVE_TXT);
		DigitUtil.draw(batch, GameController.instance.level.posCountDigits,
				posX, upY,
				DIGIT_STATE_SCALE, DIGIT_STATE_SCALE,
				0,
				false, true);
		DigitUtil.draw(batch, GameController.instance.level.posSumDigits,
				posX, downY,
				DIGIT_STATE_SCALE, DIGIT_STATE_SCALE,
				0,
				false, true);
		batch.setColor(Color.WHITE);

		// Red frame
		batch.setColor(Constants.NEGATIVE);
		batch.draw(body.getTexture(),
				stateNegativeX, stateY,
				stateFrameWidth / 2, stateFrameHeight / 2,
				stateFrameWidth, stateFrameHeight,
				0.8f, 0.8f,
				0,
				body.getRegionX(), body.getRegionY(),
				body.getRegionWidth(), body.getRegionHeight(),
				false, true);

		batch.setColor(Color.WHITE);

		batch.draw(frame.getTexture(),
				stateNegativeX, stateY,
				0, 0,
				stateFrameWidth, stateFrameHeight,
				1, 1,
				0,
				frame.getRegionX(), frame.getRegionY(),
				stateFrameWidth, stateFrameHeight,
				false, true);
		
		posX = camera.viewportWidth / 2 + frame.getRegionWidth();

		batch.setColor(Constants.NEGATIVE_TXT);
		DigitUtil.draw(batch, GameController.instance.level.negCountDigits,
				posX, upY,
				DIGIT_STATE_SCALE, DIGIT_STATE_SCALE,
				0,
				false, true);
		DigitUtil.draw(batch, GameController.instance.level.negSumDigits,
				posX, downY,
				DIGIT_STATE_SCALE, DIGIT_STATE_SCALE,
				0,
				false, true);
		batch.setColor(Color.WHITE);
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
