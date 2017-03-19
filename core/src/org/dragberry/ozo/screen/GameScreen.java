package org.dragberry.ozo.screen;

import org.dragberry.ozo.game.*;
import org.dragberry.ozo.game.level.Level;
import org.dragberry.ozo.game.util.Constants;
import org.dragberry.ozo.screen.popup.ObjectivePopup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen extends AbstractGameScreen {

	private static GameScreen instance;

	public static GameScreen init(org.dragberry.ozo.game.DirectedGame game, Level<?> level, boolean restore) {
		if (instance == null) {
			instance = new GameScreen(game);
		}
		instance.gameController = GameController.init(game, level, restore);
		instance.gameRenderer = GameRenderer.init();
		instance.objectivePopup = ObjectivePopup.init(game, level, restore);
		return instance;
	}

	private boolean paused;
	
	private GameController gameController;
	private GameRenderer gameRenderer;

	private ObjectivePopup objectivePopup;

	public GameScreen(org.dragberry.ozo.game.DirectedGame game) {
		super(game);
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
		game.setPopup(objectivePopup);
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


}
