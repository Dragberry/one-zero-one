package org.dragberry.ozo.screen.popup;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.level.Level;
import org.dragberry.ozo.game.level.goal.Goal;
import org.dragberry.ozo.game.level.settings.LevelSettings;
import org.dragberry.ozo.screen.DirectedGame;

public class ObjectivePopup extends AbstractPopup {

	private Level<? extends LevelSettings> level;
	
	public ObjectivePopup(DirectedGame game, Level<? extends LevelSettings> level) {
		super(game);
		this.level = level;
	}

	@Override
	protected void rebuildStage(float viewportWidth, float viewportHeight) {
		popupWindow.setWidth(viewportWidth * 0.75f);
		popupWindow.setHeight(viewportHeight * 0.75f);

		Label winLbl = new Label(Assets.instance.translation.get("ozo.toWin"), Assets.instance.skin.skin);
		winLbl.setAlignment(Align.center);
		popupWindow.add(winLbl).fill().expand();
		popupWindow.row().expand().fill();
		Table winTbl = new Table();
		int index = 1;
		for (Goal goal : level.goalsToWin) {
			Label goalLbl = new Label(" " + index++ + ". " + goal.getMessage(), Assets.instance.skin.skin);
			goalLbl.setWrap(true);
			goalLbl.setAlignment(Align.center);
			winTbl.add(goalLbl).fill().expand();
			winTbl.row();
		}
		popupWindow.add(winTbl).expand().fill();
		popupWindow.row();
		
		Label loseLbl = new Label(Assets.instance.translation.get("ozo.toLose"), Assets.instance.skin.skin);
		loseLbl.setAlignment(Align.center);
		popupWindow.add(loseLbl).fill().expand();
		popupWindow.row().expand().fill();
		Table loseTbl = new Table();
		index = 1;
		for (Goal goal : level.goalsToLose) {
			Label goalLbl = new Label(" " + index++ +". " + goal.getMessage(), Assets.instance.skin.skin);
			goalLbl.setWrap(true);
			goalLbl.setAlignment(Align.center);
			loseTbl.add(goalLbl).fill().expand();
			loseTbl.row();
		}
		popupWindow.add(loseTbl).expand().fill();

		popupWindow.row();
		popupWindow.add(createOkBtn()).fill(0.5f, 1.0f).expandY();
		popupWindow.row();
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
