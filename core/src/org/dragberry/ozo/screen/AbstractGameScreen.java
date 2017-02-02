package org.dragberry.ozo.screen;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.screen.popup.AbstractPopup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

public abstract class AbstractGameScreen implements Screen {
	
	protected DirectedGame game;
	
	private AbstractPopup popup;
	
	public AbstractGameScreen(DirectedGame game) {
		this.game = game;
	}

	public InputProcessor getInputProcessor() {
		if (popup != null) {
			return popup.getInputProcessor();
		} else {
			return getScreenInputProcessor();
		}
	}
	 
	protected abstract InputProcessor getScreenInputProcessor();
	
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
	}
	
	@Override
	public void dispose() {
		if (hasPopup()) {
			popup.dispose();
		}
	}
	
	public boolean hasPopup() {
		return popup != null;
	}
	
	public void showPopup(AbstractPopup popupScreen) {
		if (hasPopup()) {
			popup.dispose();
		}
		popup = popupScreen;
		popup.show();
		Gdx.input.setInputProcessor(popup.getInputProcessor());
	}
	
	public void hidePopup() {
		if (hasPopup()) {
			popup.hide();
			popup.dispose();
			popup = null;
			Gdx.input.setInputProcessor(getScreenInputProcessor());
		}
	}
	
	public AbstractPopup getPopup() {
		return popup;
	}
	

}

