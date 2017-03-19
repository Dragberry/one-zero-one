package org.dragberry.ozo.screen.popup;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.GameController;
import org.dragberry.ozo.game.level.Level;
import org.dragberry.ozo.game.level.goal.AbstractGoal;
import org.dragberry.ozo.game.level.goal.Goal;
import org.dragberry.ozo.game.level.settings.LevelSettings;
import org.dragberry.ozo.screen.DirectedGame;

public class ObjectivePopup extends AbstractPopup {

	private Level<? extends LevelSettings> level;

	private boolean restore;

	private Label winLbl;
	private Table winTbl;
	private Label loseLbl;
	private Table loseTbl;

	private TextButton newGameBtn;
	private TextButton continueBtn;
	private TextButton okBtn;

	private static ObjectivePopup instance;

	public static ObjectivePopup init(DirectedGame game, Level<?> level, boolean restore) {
		if (instance == null) {
			instance = new ObjectivePopup(game);
		}
		instance.restore = restore;
		instance.level = level;
		return instance;
	}
	
	private ObjectivePopup(DirectedGame game) {
		super(game);
	}

	@Override
	protected void rebuildStage() {
		popupWindow.clear();
		winTbl.clear();
		loseTbl.clear();

		popupWindow.add(winLbl).fill().expand();
		popupWindow.row().expand().fill();

		populateTable(winTbl, level.goalsToWin);
		popupWindow.add(winTbl).expand().fill();
		popupWindow.row();

		popupWindow.add(loseLbl).fill().expand();
		popupWindow.row().expand().fill();

		populateTable(loseTbl, level.goalsToLose);
		popupWindow.add(loseTbl).expand().fill();
		popupWindow.row();

		if (restore) {
			popupWindow.add(continueBtn).fill().expand().pad(10f);
			popupWindow.row();
			popupWindow.add(newGameBtn).fill().expand().pad(10f);
			popupWindow.row();
		} else {
			popupWindow.add(okBtn).fill().expand().pad(10f);
			popupWindow.row();
		}
	}

	private void populateTable(Table tbl, Array<AbstractGoal> goals) {
		int index;
		index = 1;
		for (Goal goal : goals) {
			Label goalLbl = new Label(" " + index++ +". " + goal.getMessage(), Assets.instance.skin.skin);
			goalLbl.setWrap(true);
			goalLbl.setAlignment(Align.center);
			tbl.add(goalLbl).fill().expand();
			tbl.row();
		}
	}

	@Override
	protected void buildStage(float viewportWidth, float viewportHeight) {
		popupWindow.setWidth(viewportWidth * 0.75f);
		popupWindow.setHeight(viewportHeight * 0.75f);

		winLbl = new Label(Assets.instance.translation.get("ozo.toWin"), Assets.instance.skin.skin);
		winLbl.setAlignment(Align.center);

		winTbl = new Table();

		loseLbl = new Label(Assets.instance.translation.get("ozo.toLose"), Assets.instance.skin.skin);
		loseLbl.setAlignment(Align.center);

		loseTbl = new Table();

		newGameBtn = createNewGameBtn();
		continueBtn = createContinueBtn();
		okBtn = createOkBtn();

		rebuildStage();
	}
	
	private TextButton createNewGameBtn() {
		TextButton btn = new TextButton(Assets.instance.translation.get("ozo.startNewGame"), Assets.instance.skin.skin);
		btn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				GameController.init(game, level, false);
				level.started = true;
				game.setPopup(null);
			}
		});
		return btn;
	}

	private TextButton createContinueBtn() {
		TextButton btn = new TextButton(Assets.instance.translation.get("ozo.continueSavedGame"), Assets.instance.skin.skin);
		btn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				level.started = true;
				game.setPopup(null);
			}
		});
		return btn;
	}

	private TextButton createOkBtn() {
		TextButton btn = new TextButton(Assets.instance.translation.get("ozo.ok"), Assets.instance.skin.skin);
		btn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				level.started = true;
				game.setPopup(null);
			}
		});
		return btn;
	}

}
