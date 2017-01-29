package org.dragberry.ozo.screen;

import org.dragberry.ozo.game.GameController;
import org.dragberry.ozo.game.GameRenderer;
import org.dragberry.ozo.game.level.AbstractLevel;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen extends AbstractGameScreen {

	private AbstractLevel level;
	private boolean paused;
	
	private GameController gameController;
	private GameRenderer gameRenderer;

	public GameScreen(Game game, AbstractLevel level) {
		super(game);
		this.level = level;
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
		gameController = new GameController(game, level);
		gameRenderer = new GameRenderer(gameController);
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void hide() {
		gameRenderer.dispose();
		Gdx.input.setCatchBackKey(false);
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
