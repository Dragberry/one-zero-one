package org.dragberry.ozo.screen.popup;

import org.dragberry.ozo.screen.AbstractGameScreen;
import org.dragberry.ozo.screen.DirectedGame;

import com.badlogic.gdx.InputProcessor;

public abstract class AbstractPopup extends AbstractGameScreen {
	
	protected final AbstractGameScreen parentScreen;
	
	public AbstractPopup(DirectedGame game, AbstractGameScreen parentScreen) {
		super(game);
		this.parentScreen = parentScreen;
	}


	@Override
	protected abstract InputProcessor getScreenInputProcessor();

	@Override
	public abstract void render(float deltaTime);

	@Override
	public void resize(int width, int height) { }

	@Override
	public abstract void show();

	@Override
	public abstract void hide();

	@Override
	public void pause() { }

}
