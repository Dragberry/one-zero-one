package org.dragberry.ozo.screen.popup;

import org.dragberry.ozo.screen.AbstractGameScreen;
import org.dragberry.ozo.screen.DirectedGame;

public abstract class AbstractPopup extends AbstractGameScreen {
	
	public AbstractPopup(DirectedGame game) {
		super(game);
	}

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
