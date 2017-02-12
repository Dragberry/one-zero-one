package org.dragberry.ozo.screen.popup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import org.dragberry.ozo.screen.AbstractGameScreen;
import org.dragberry.ozo.screen.DirectedGame;
import org.dragberry.ozo.screen.MenuSkin;

public abstract class AbstractPopup extends AbstractGameScreen {

	private Stage stage;
	protected Window popupWindow;
	protected String popupTitle;

	public AbstractPopup(DirectedGame game) {
		this(game, "");
	}

	public AbstractPopup(DirectedGame game, String popupTitle) {
		super(game);
		this.popupTitle = popupTitle;
	}

	@Override
	public InputProcessor getInputProcessor() {
		return stage;
	}

	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(1, 1, 1, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) { }

	@Override
	public void show() {
		stage = new Stage();
		rebuildStage();
	}

	private void rebuildStage() {
		popupWindow = new Window(popupTitle, MenuSkin.getSkin());
		final float viewportWidth = stage.getViewport().getCamera().viewportWidth;
		final float viewportHeight = stage.getViewport().getCamera().viewportHeight;
		stage.addActor(popupWindow);
		popupWindow.row().fill().expand();
		rebuildStage(viewportWidth, viewportHeight);
		float popupWidth = popupWindow.getWidth();
		float popupHeight = popupWindow.getHeight();
		popupWindow.setPosition(
				(viewportWidth - popupWidth) / 2,
				(viewportHeight - popupHeight) / 2
		);
	}

	protected abstract void rebuildStage(float viewportWidth, float viewportHeight);


	@Override
	public void hide() {
		stage.dispose();
	}

	@Override
	public void pause() { }

}
