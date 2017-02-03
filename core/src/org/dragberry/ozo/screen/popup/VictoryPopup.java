package org.dragberry.ozo.screen.popup;

import org.dragberry.ozo.screen.AbstractGameScreen;
import org.dragberry.ozo.screen.DirectedGame;
import org.dragberry.ozo.screen.MenuSkin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class VictoryPopup extends AbstractPopup {

	private Stage stage;
	
	public VictoryPopup(DirectedGame game, AbstractGameScreen parent) {
		super(game, parent);
	}

	@Override
	public InputProcessor getScreenInputProcessor() {
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
	public void show() {
		stage = new Stage();
		rebuildStage();
	}

	private void rebuildStage() {
		Table tbl = new Table();
		tbl.align(Align.center);
		final float viewportWidth = stage.getViewport().getCamera().viewportWidth;
		final float viewportHeight = stage.getViewport().getCamera().viewportHeight;
		tbl.setWidth(viewportWidth / 2);
		tbl.setHeight(viewportHeight / 2);
		tbl.setPosition(viewportWidth * 0.25f, viewportHeight * 0.25f);
		stage.addActor(tbl);
		Label congrLbl = new Label("Congratulations!", MenuSkin.getSkin());
		congrLbl.setAlignment(Align.center);
		tbl.add(congrLbl).fill().expand();
		tbl.row();
		Label wonLbl = new Label("You won!", MenuSkin.getSkin());
		wonLbl.setAlignment(Align.center);
		tbl.add(wonLbl).fill().expand();
		tbl.row();
		tbl.add(createNextBtn()).fill().expand();
		tbl.row();
		tbl.add(createPlayAgainBtn()).fill().expand();
		tbl.row();
		tbl.add(createMainMenuBtn()).fill().expand();
	}

	private TextButton createNextBtn() {
		TextButton btn = new TextButton("Next level", MenuSkin.getSkin());
		btn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.playNextLevel();
			}
		});
		return btn;
	}

	private TextButton createMainMenuBtn() {
		TextButton btn = new TextButton("Go to menu", MenuSkin.getSkin());
		btn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.back();
			}
		});
		return btn;
	}

	private TextButton createPlayAgainBtn() {
		TextButton btn = new TextButton("Play agian", MenuSkin.getSkin());
		btn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.playLevel();
			}
		});
		return btn;
	}

	@Override
	public void hide() {
		stage.dispose();
	}

}
