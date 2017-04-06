package org.dragberry.ozo.screen;

import org.dragberry.ozo.game.*;
import org.dragberry.ozo.game.level.Level;
import org.dragberry.ozo.game.util.Constants;
import org.dragberry.ozo.screen.popup.ObjectivePopup;
import org.dragberry.ozo.screen.popup.WrongVersionPopup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen extends AbstractGameScreen {

	private boolean paused;
	
	private GameController gameController;
	private GameRenderer gameRenderer;

	private ObjectivePopup objectivePopup;

	public GameScreen(DirectedGame game) {
		super(game);
	}

	public GameScreen init(Level<?> level, boolean restore) {
		this.gameController = GameController.instance.init(level, restore);
		this.gameRenderer = new GameRenderer();
		this.objectivePopup = game.getScreen(ObjectivePopup.class).init(level, restore);
		return this;
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
		if (game.wrongAppVersion) {
			DirectedGame.game.setPopup(DirectedGame.game.getScreen(WrongVersionPopup.class).init());
		} else {
			game.setPopup(objectivePopup);
		}
	}

	@Override
	public void hide() {}

	@Override
	public void pause() {
		paused = true;
	}
	
	@Override
	public void resume() {
		super.resume();
		paused = false;
	}

	@Override
	public void dispose() {
		gameRenderer.dispose();
	}
}
