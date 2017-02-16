package org.dragberry.ozo.screen;

import org.dragberry.ozo.game.GameController;
import org.dragberry.ozo.game.GameRenderer;
import org.dragberry.ozo.game.level.Level;
import org.dragberry.ozo.game.util.Constants;
import org.dragberry.ozo.screen.popup.ObjectivePopup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen extends AbstractGameScreen {

	private Level<?> level;
	private boolean paused;
	
	private ObjectivePopup objectivePopup;
	
	private GameController gameController;
	private GameRenderer gameRenderer;

	public GameScreen(DirectedGame game, Level<?> level) {
		super(game);
		this.level = level;
	}

	@Override
	public InputProcessor getInputProcessor() {
		return gameController;
	}

	@Override
	public void render(float deltaTime) {
		if (!paused) {
			gameController.update(deltaTime);
		}
		Gdx.gl.glClearColor(Constants.BACKGROUND_GAME.r,
				Constants.BACKGROUND_GAME.g,
				Constants.BACKGROUND_GAME.b,
				Constants.BACKGROUND_GAME.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameRenderer.render();
	}

	@Override
	public void resize(int width, int height) {
		gameRenderer.resize(width, height);
	}

	@Override
	public void show() {
		gameController = GameController.init(game, level);
		gameRenderer = new GameRenderer(gameController);
		objectivePopup = new ObjectivePopup(game, level);
		game.setPopup(objectivePopup);
	}

	@Override
	public void hide() {
		gameRenderer.dispose();
	}

	@Override
	public void pause() {
		paused = true;
	}
	
	@Override
	public void resume() {
		super.resume();
		paused = false;
	}


}
