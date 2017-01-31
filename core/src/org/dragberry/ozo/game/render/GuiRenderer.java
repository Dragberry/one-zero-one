package org.dragberry.ozo.game.render;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.GameController;
import org.dragberry.ozo.game.util.Constants;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GuiRenderer implements Renderer {
	
	private static final String TIME = "Time: ";
	private static final String STEPS = "Steps: ";
	private static final int SECONDS_PER_MINUTE = 60;
	private static final String EMPTY = "";
	private static final String ZERO = "0";
	private static final String COLON = ":";
	
	private OrthographicCamera camera;
	
	private GameController gameController;
	
	public GuiRenderer(GameController gameController) {
		this.gameController = gameController;
		init();
	}
	
	@Override
	public void init() {
		camera = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.setToOrtho(true);
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
		BitmapFont font = Assets.instance.fonts.gui_28;
		font.setColor(Color.BLACK);
		font.draw(batch, "Goal to win:", 10, 40);
		GlyphLayout layout = new GlyphLayout(font, "Goal to lose:");
		font.draw(batch, layout,
				camera.viewportWidth - layout.width - 10, 40);
		getGameContoller().level.renderGoals(batch);
	}

	private void renderLevelName(SpriteBatch batch) {
		BitmapFont font = Assets.instance.fonts.gui_28;
		font.setColor(Color.BLACK);
		GlyphLayout layout  = new GlyphLayout(font, getGameContoller().level.levelName);
		font.draw(batch, layout,
				camera.viewportWidth / 2 - layout.width / 2, 15);
	}

	@Override
	public GameController getGameContoller() {
		return gameController;
	}
	
	private void renderSteps(SpriteBatch batch) {
		BitmapFont font = Assets.instance.fonts.gui_28;
		font.setColor(Color.BLACK);
		String stepsString = STEPS + getGameContoller().level.steps;
		GlyphLayout layout = new GlyphLayout(font, stepsString);
		font.draw(batch, layout,
				camera.viewportWidth - layout.width - 10, 15);
	}
	
	private void renderTime(SpriteBatch batch) {
		BitmapFont font = Assets.instance.fonts.gui_28;
		font.setColor(Color.BLACK);
		font.draw(batch,
				   TIME + timeToString((int) getGameContoller().level.time), 10, 15);
	}
	
	private void renderState(SpriteBatch batch) {
		TextureRegion ball = null;
		ball = Assets.instance.unit.redBall;
		batch.draw(ball,
				10, camera.viewportHeight - 80,
				0, 0, 
				ball.getRegionWidth(), ball.getRegionHeight(), 
				1, 1,
				0);
		ball = Assets.instance.unit.greenBall;
		batch.draw(ball,
				camera.viewportWidth - ball.getRegionWidth() - 10, camera.viewportHeight - 80,
				0, 0, 
				ball.getRegionWidth(), ball.getRegionHeight(), 
				1, 1,
				0);
	}
	
	private static String timeToString(int timeInSeconds) {
		int minutes = timeInSeconds / SECONDS_PER_MINUTE;
		int seconds = timeInSeconds % SECONDS_PER_MINUTE;
		return prefixZero(minutes) + COLON + prefixZero(seconds);
	}
	
	@Override
	public void resize(int width, int height) {
		camera.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
        camera.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT / (float) height) * (float) width;
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
