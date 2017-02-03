package org.dragberry.ozo.screen.popup;

import org.dragberry.ozo.game.level.Level;
import org.dragberry.ozo.game.level.goal.Goal;
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

public class ObjectivePopup extends AbstractPopup {

	private Stage stage;
	private Level level;
	
	public ObjectivePopup(DirectedGame game, Level level, AbstractGameScreen parentScreen) {
		super(game, parentScreen);
		this.level = level;
	}

	@Override
	protected InputProcessor getScreenInputProcessor() {
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
		tbl.setWidth(viewportWidth * 0.75f);
		tbl.setHeight(viewportHeight / 2);
		tbl.setPosition(viewportWidth * 0.125f, viewportHeight * 0.25f);
		stage.addActor(tbl);
		Label winLbl = new Label("To win:", MenuSkin.getSkin());
		winLbl.setAlignment(Align.center);
		tbl.add(winLbl).fill().expand();
		tbl.row().expand().fill();
		Table winTbl = new Table();
		int index = 1;
		for (Goal goal : level.goalsToWin) {
			Label goalLbl = new Label(" " + index++ + ". " + goal.getMessage(), MenuSkin.getSkin());
			goalLbl.setAlignment(Align.left);
			winTbl.add(goalLbl).fill().expand();
			winTbl.row();
		}
		tbl.add(winTbl).expand().fill();
		tbl.row();
		
		Label loseLbl = new Label("To lose:", MenuSkin.getSkin());
		loseLbl.setAlignment(Align.center);
		tbl.add(loseLbl).fill().expand();
		tbl.row().expand().fill();
		Table loseTbl = new Table();
		index = 1;
		for (Goal goal : level.goalsToLose) {
			Label goalLbl = new Label(" " + index++ +" . " + goal.getMessage(), MenuSkin.getSkin());
			goalLbl.setAlignment(Align.left);
			loseTbl.add(goalLbl).fill().expand();
			loseTbl.row();
		}
		tbl.add(loseTbl).expand().fill();
		
		tbl.row();
		tbl.add(createOkBtn()).fill().expand();
		tbl.row();
	}
	
	private TextButton createOkBtn() {
		TextButton btn = new TextButton("Ok", MenuSkin.getSkin());
		btn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				level.started = true;
				parentScreen.hidePopup();
			}
		});
		return btn;
	}

	@Override
	public void hide() {
		stage.dispose();
	}

}
