package org.dragberry.ozo.screen.popup;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.screen.ActionExecutor;
import org.dragberry.ozo.screen.DirectedGame;
import org.dragberry.ozo.screen.MenuSkin;

public class VictoryPopup extends AbstractPopup {

	public VictoryPopup(DirectedGame game) {
		super(game);
	}

	@Override
	protected void rebuildStage(float viewportWidth, float viewportHeight) {
		popupWindow.setWidth(viewportWidth * 0.75f);
		popupWindow.setHeight(viewportHeight * 0.75f);
		Label congrLbl = new Label(Assets.instance.translation.get("ozo.congratulations"), MenuSkin.getSkin());
		congrLbl.setAlignment(Align.center);
		popupWindow.add(congrLbl).fill().expand();
		popupWindow.row();
		Label wonLbl = new Label(Assets.instance.translation.get("ozo.levelCompleted"), MenuSkin.getSkin());
		wonLbl.setAlignment(Align.center);
		popupWindow.add(wonLbl).fill().expand();
		popupWindow.row();
		popupWindow.add(createNextBtn()).fill().expand().pad(10f);
		popupWindow.row();
		popupWindow.add(createPlayAgainBtn()).fill().expand().pad(10f);
		popupWindow.row();
		popupWindow.add(createMainMenuBtn()).fill().expand().pad(10f);
	}

	private TextButton createNextBtn() {
		TextButton btn = new TextButton(Assets.instance.translation.get("ozo.nextLevel"), MenuSkin.getSkin());
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

	private TextButton createMainMenuBtn() {
		TextButton btn = new TextButton(Assets.instance.translation.get("ozo.goToMenu"), MenuSkin.getSkin());
		btn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.hidePopup(new ActionExecutor() {
					
					@Override
					public void execute() {
						game.back();
					}
				});
			}
		});
		return btn;
	}

	private TextButton createPlayAgainBtn() {
		TextButton btn = new TextButton(Assets.instance.translation.get("ozo.playAgain"), MenuSkin.getSkin());
		btn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.hidePopup(new ActionExecutor() {
					
					@Override
					public void execute() {
						game.playLevel();
					}
				});
			}
		});
		return btn;
	}

}
