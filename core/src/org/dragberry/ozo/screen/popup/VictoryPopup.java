package org.dragberry.ozo.screen.popup;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.DirectedGame;
import org.dragberry.ozo.screen.ActionExecutor;

public class VictoryPopup extends AbstractGameFinishedPopup {

	private Label congrLbl;
	private Label wonLbl;

	private TextButton nextLevelBtn;
	private TextButton playAgainBtn;
	private TextButton mainMenuBtn;

	public VictoryPopup(DirectedGame game) {
		super(game);
	}

	@Override
	protected Label getTitleLabel() {
		return congrLbl;
	}

	@Override
	protected Label getNoNewResultsLabel() {
		return wonLbl;
	}

	@Override
	protected void addButtons() {
		popupWindow.add(nextLevelBtn).fill().expand().pad(10f);
		popupWindow.row();
		popupWindow.add(playAgainBtn).fill().expand().pad(10f);
		popupWindow.row();
		popupWindow.add(mainMenuBtn).fill().expand().pad(10f);
	}

	@Override
	protected void createButtons() {
		nextLevelBtn = createNextBtn();
		playAgainBtn = createPlayAgainBtn();
		mainMenuBtn = createMainMenuBtn();
	}

	@Override
	protected void createLabels() {
		super.createLabels();

		congrLbl = new Label(Assets.instance.translation.get("ozo.congratulations"), Assets.instance.skin.skin);
		congrLbl.setAlignment(Align.center);

		wonLbl = new Label(Assets.instance.translation.get("ozo.levelCompleted"), Assets.instance.skin.skin);
		wonLbl.setAlignment(Align.center);
	}

	private TextButton createNextBtn() {
		TextButton btn = new TextButton(Assets.instance.translation.get("ozo.nextLevel"), Assets.instance.skin.skin);
		btn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.hidePopup(new ActionExecutor() {
					
					@Override
					public void execute() {
						game.playNextLevel();
					}
				});
			}
		});
		return btn;
	}

}
