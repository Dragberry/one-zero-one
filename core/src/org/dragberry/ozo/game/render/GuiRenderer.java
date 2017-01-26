package org.dragberry.ozo.game.render;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.GameController;
import org.dragberry.ozo.game.util.Constants;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
		batch.begin();
		renderGuiTime(batch);
		renderGuiSteps(batch);
		batch.end();
	}
	
	@Override
	public GameController getGameContoller() {
		return gameController;
	}
	
	private void renderGuiSteps(SpriteBatch batch) {
		BitmapFont font = Assets.instance.fonts._24;
		font.setColor(Color.BLACK);
		String stepsString = STEPS + getGameContoller().steps;
		GlyphLayout layout = new GlyphLayout(font, stepsString);
		font.draw(batch, layout,
				camera.viewportWidth - layout.width - 10, 15);
	}
	
	private void renderGuiTime(SpriteBatch batch) {
		BitmapFont font = Assets.instance.fonts._24;
		font.setColor(Color.BLACK);
		font.draw(batch,
				   TIME + timeToString((int) getGameContoller().time), 10, 15);
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
        camera.setToOrtho(true);
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
