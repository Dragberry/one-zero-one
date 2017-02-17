package org.dragberry.ozo.screen.popup;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.level.settings.LevelSettings;
import org.dragberry.ozo.screen.ActionExecutor;
import org.dragberry.ozo.screen.DirectedGame;

public class VictoryPopup extends AbstractPopup {

	private LevelSettings settings;

	public VictoryPopup(DirectedGame game, LevelSettings settings) {
		super(game);
		this.settings = settings;
	}

	public VictoryPopup(DirectedGame game) {
		this(game, null);
	}

	@Override
	protected void rebuildStage(float viewportWidth, float viewportHeight) {
		popupWindow.setWidth(viewportWidth * 0.75f);
		popupWindow.setHeight(viewportHeight * 0.75f);
		Label congrLbl = new Label(Assets.instance.translation.get("ozo.congratulations"), Assets.instance.skin.skin);
		congrLbl.setAlignment(Align.center);
		popupWindow.add(congrLbl).fill().expand();
		popupWindow.row().pad(10f);

		if (settings == null) {
			Label wonLbl = new Label(Assets.instance.translation.get("ozo.levelCompleted"), Assets.instance.skin.skin);
			wonLbl.setAlignment(Align.center);
			popupWindow.add(wonLbl).fill().expand();
			popupWindow.row();
		} else {
			Label bestResultLbl = new Label(Assets.instance.translation.get("ozo.newRecord"), Assets.instance.skin.skin);
			bestResultLbl.setAlignment(Align.center);
			bestResultLbl.setWrap(true);
			popupWindow.add(bestResultLbl).fill().expand();
			popupWindow.row();
			Table resultTable = new Table();
			for (ObjectMap.Entry<String, Object> result : settings.getResults().entries()) {
				resultTable.add(new Label(result.key, Assets.instance.skin.skin)).fill().expand().pad(10f);
				resultTable.add(new Label(result.value.toString(), Assets.instance.skin.skin)).fill().expand().pad(10f);
				resultTable.row();
			}
			popupWindow.add(resultTable).row();
		}

		popupWindow.add(createNextBtn()).fill().expand().pad(10f);
		popupWindow.row();
		popupWindow.add(createPlayAgainBtn()).fill().expand().pad(10f);
		popupWindow.row();
		popupWindow.add(createMainMenuBtn()).fill().expand().pad(10f);
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

	private TextButton createMainMenuBtn() {
		TextButton btn = new TextButton(Assets.instance.translation.get("ozo.goToMenu"), Assets.instance.skin.skin);
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
		TextButton btn = new TextButton(Assets.instance.translation.get("ozo.playAgain"), Assets.instance.skin.skin);
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
