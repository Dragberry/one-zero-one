package org.dragberry.ozo.screen;

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

public class VictoryScreen extends AbstractGameScreen {

	private Stage stage;
	
	public VictoryScreen(DirectedGame game) {
		super(game);
	}

	@Override
	public InputProcessor getInputProcessor() {
		return stage;
	}

	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(MenuSkin.BACKGROUND_COLOR.r, MenuSkin.BACKGROUND_COLOR.g, MenuSkin.BACKGROUND_COLOR.b, MenuSkin.BACKGROUND_COLOR.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
	}
	

	@Override
	public void show() {
		stage = new Stage();
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
		tbl.add(new TextButton("Next level", MenuSkin.getSkin())).fill().expand();
		tbl.row();
		tbl.add(new TextButton("Play again", MenuSkin.getSkin())).fill().expand();
		tbl.row();
		tbl.add(createMainMenuBtn()).fill().expand();
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

	@Override
	public void hide() {
		stage.dispose();
	}

	@Override
	public void pause() {
		
	}

}
