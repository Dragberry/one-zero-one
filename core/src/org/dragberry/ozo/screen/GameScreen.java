package org.dragberry.ozo.screen;

import org.dragberry.ozo.game.GameController;
import org.dragberry.ozo.game.GameRenderer;
import org.dragberry.ozo.game.level.Level;
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
		Gdx.gl.glClearColor(0x64 / 255.0f,  0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
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
