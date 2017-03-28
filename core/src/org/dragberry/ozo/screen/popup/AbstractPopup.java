package org.dragberry.ozo.screen.popup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.util.Constants;
import org.dragberry.ozo.screen.AbstractGameScreen;
import org.dragberry.ozo.game.DirectedGame;

public abstract class AbstractPopup extends AbstractGameScreen {

	protected Stage stage;
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
		game.platform.getAdsController().showBannerAd();
		if (stage == null) {
			stage = new Stage(new ScalingViewport(Scaling.stretch,
					Constants.VIEWPORT_GUI_WIDTH,
					Gdx.graphics.getHeight() * Constants.VIEWPORT_GUI_WIDTH / Gdx.graphics.getWidth()));
			buildStage();
		} else {
			rebuildStage();
		}
	}

	protected abstract void rebuildStage();

	protected void buildStage() {
		popupWindow = new Window(popupTitle, Assets.instance.skin.skin);
		final float viewportWidth = stage.getViewport().getCamera().viewportWidth;
		final float viewportHeight = stage.getViewport().getCamera().viewportHeight;
		stage.addActor(popupWindow);
		popupWindow.row().fill().expand();
		buildStage(viewportWidth, viewportHeight);
		float popupWidth = popupWindow.getWidth();
		float popupHeight = popupWindow.getHeight();
		popupWindow.setPosition(
				(viewportWidth - popupWidth) / 2,
				(viewportHeight - popupHeight) / 2
		);
	}

	protected abstract void buildStage(float viewportWidth, float viewportHeight);


	@Override
	public void hide() {
		game.platform.getAdsController().hideBannerAd();
	}

	@Override
	public void pause() { }

	@Override
	public void dispose() {
		if (stage != null) {
			stage.dispose();
		}
	}
}
