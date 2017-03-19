package org.dragberry.ozo.screen;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;

public abstract class AbstractGameScreen implements Screen {
	
	protected org.dragberry.ozo.game.DirectedGame game;
	
	public AbstractGameScreen(org.dragberry.ozo.game.DirectedGame game) {
		this.game = game;
	}

	public abstract InputProcessor getInputProcessor();
	
	@Override
	public abstract void render(float deltaTime);
	
	@Override
	public abstract void resize(int width, int height);
	
	@Override
	public abstract void show();
	
	@Override
	public abstract void hide();
	
	@Override
	public abstract void pause();
	
	@Override
	public void resume() { }
	
	@Override
	public void dispose() { }
	

}

