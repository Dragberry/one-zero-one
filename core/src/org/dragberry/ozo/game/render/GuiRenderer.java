package org.dragberry.ozo.game.render;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.GameController;
import org.dragberry.ozo.game.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class GuiRenderer implements Renderer {
	
	private static final String TIME = "ozo.time";
	private static final String STEPS = "ozo.steps";
	private static final int SECONDS_PER_MINUTE = 60;
	private static final String EMPTY = "";
	private static final String ZERO = "0";
	private static final String COLON = ":";
	
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
		getGameContoller().level.renderGoals(batch, new Vector2(25.0f, 40.0f));
	}

	private void renderLevelName(SpriteBatch batch) {
		BitmapFont font = Assets.instance.fonts.gui_l;
		font.setColor(Color.BLACK);
		GlyphLayout layout  = new GlyphLayout(font, getGameContoller().level.settings.name);
		font.draw(batch, layout,
				camera.viewportWidth / 2 - layout.width / 2, 15);
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
		font.draw(batch, timeToString((int) getGameContoller().level.time), 25, camera.viewportHeight - timeStr.height * 2);
	}
	
	private void renderState(SpriteBatch batch) {
		float offset = 105f;
		GlyphLayout layout;
		BitmapFont font;
		TextureRegion ball;

		// Blue ball
		font = Assets.instance.fonts.gui_m;
		font.setColor(Color.BLACK);
		ball = Assets.instance.unit.ball;
		batch.setColor(Color.BLUE);
		batch.draw(ball,
				camera.viewportWidth / 2 - ball.getRegionWidth() / 2, camera.viewportHeight - offset,
				0, 0,
				ball.getRegionWidth(), ball.getRegionHeight(),
				1, 1,
				0);
		layout = new GlyphLayout(font, String.valueOf(gameController.zeroCount));
		font.draw(batch, layout,
				camera.viewportWidth / 2 - layout.width / 2,
				camera.viewportHeight - offset + ball.getRegionHeight() / 2 - layout.height / 2);

		// Green ball
		font = Assets.instance.fonts.gui_s;
		font.setColor(Color.BLACK);
		ball = Assets.instance.unit.infoBall;
		batch.setColor(Color.GREEN);
		batch.draw(ball,
				camera.viewportWidth / 2 - ball.getRegionWidth() * 1.5f, camera.viewportHeight - offset,
				0, 0,
				ball.getRegionWidth(), ball.getRegionHeight(),
				1, 1,
				0);
		layout = new GlyphLayout(font, String.valueOf(gameController.posCount));
		float countY = camera.viewportHeight - offset + ball.getRegionHeight() / 2 - 1.5f * layout.height;
		float sumY = camera.viewportHeight - offset + ball.getRegionHeight() / 2 + 0.5f * layout.height;
		float posX = camera.viewportWidth / 2 - ball.getRegionWidth() - layout.width / 2;
		font.draw(batch, layout, posX, countY);
		layout = new GlyphLayout(font, String.valueOf(gameController.posSum));
		font.draw(batch, layout, posX, sumY);

		// Red ball
		batch.setColor(Color.RED);
		batch.draw(ball,
				camera.viewportWidth / 2 + ball.getRegionWidth() / 2, camera.viewportHeight - offset,
				0, 0,
				ball.getRegionWidth(), ball.getRegionHeight(),
				1, 1,
				0);
		layout = new GlyphLayout(font, String.valueOf(gameController.negCount));
		float negX = camera.viewportWidth / 2 + ball.getRegionWidth() - layout.width / 2;
		font.draw(batch, layout, negX, countY);
		layout = new GlyphLayout(font, String.valueOf(-gameController.negSum));
		font.draw(batch, layout, negX, sumY);
	}
	
	private static String timeToString(int timeInSeconds) {
		int minutes = timeInSeconds / SECONDS_PER_MINUTE;
		int seconds = timeInSeconds % SECONDS_PER_MINUTE;
		return prefixZero(minutes) + COLON + prefixZero(seconds);
	}
	
	@Override
	public void resize(int width, int height) {
        camera.viewportHeight = (Constants.VIEWPORT_GUI_WIDTH / width) * height;
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
	}
	
	private static String prefixZero(int time) {
		return (time < 10 ? ZERO : EMPTY) + time;
	}

	@Override
	public OrthographicCamera getCamera() {
		return camera;
	}
}
