package org.dragberry.ozo.screen;

import org.dragberry.ozo.game.Assets;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

public abstract class AbstractGameScreen implements Screen {
	
	protected DirectedGame game;
	
	public AbstractGameScreen(DirectedGame game) {
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
	public void resume() {
		Assets.instance.init(new AssetManager());
	}
	
	@Override
	public void dispose() {
		Assets.instance.dispose();
	}

}

