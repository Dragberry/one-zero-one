package org.dragberry.ozo.screen.popup;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;

import org.dragberry.ozo.common.levelresult.LevelResultName;
import org.dragberry.ozo.common.levelresult.NewLevelResultResponse;
import org.dragberry.ozo.common.levelresult.NewLevelResultsResponse;
import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.screen.ActionExecutor;
import org.dragberry.ozo.screen.DirectedGame;

import java.util.Map;

public class VictoryPopup extends AbstractPopup {

	private NewLevelResultsResponse results;

	public VictoryPopup(DirectedGame game, NewLevelResultsResponse results) {
		super(game);
		this.results = results;
	}

	@Override
	protected void rebuildStage(float viewportWidth, float viewportHeight) {

		popupWindow.setWidth(viewportWidth * 0.75f);
		popupWindow.setHeight(viewportHeight * 0.75f);

		Skin skin = Assets.instance.skin.skin;
		Label congrLbl = new Label(Assets.instance.translation.get("ozo.congratulations"), skin);
		congrLbl.setAlignment(Align.center);
		popupWindow.add(congrLbl).fill().expand();
		popupWindow.row().pad(10f);

		if (results.getResults().isEmpty()) {
			Label wonLbl = new Label(Assets.instance.translation.get("ozo.levelCompleted"), skin);
			wonLbl.setAlignment(Align.center);
			popupWindow.add(wonLbl).fill().expand();
			popupWindow.row();
		} else {
			Label bestResultLbl = new Label(Assets.instance.translation.get("ozo.newRecord"), skin);
			bestResultLbl.setAlignment(Align.center);
			bestResultLbl.setWrap(true);
			popupWindow.add(bestResultLbl).fill().expand();
			popupWindow.row().expand().fill();

			Table resultTable = new Table();

			ArrayMap<LevelResultName, Integer> worlds = new ArrayMap<LevelResultName, Integer>(results.getResults().size());
			ArrayMap<LevelResultName, Integer> personal = new ArrayMap<LevelResultName, Integer>(results.getResults().size());

			for (Map.Entry<LevelResultName, NewLevelResultResponse<Integer>> result : results.getResults().entrySet()) {
				if (result.getValue().isWorlds()) {
					worlds.put(result.getKey(), result.getValue().getValue());
				} else if (result.getValue().isPersonal()) {
					personal.put(result.getKey(), result.getValue().getValue());;
				}
			}

			populateRecords("ozo.world", worlds, resultTable);
			populateRecords("ozo.personal", personal, resultTable);

			popupWindow.add(resultTable).row();
		}

		popupWindow.add(createNextBtn()).fill().expand().pad(10f);
		popupWindow.row();
		popupWindow.add(createPlayAgainBtn()).fill().expand().pad(10f);
		popupWindow.row();
		popupWindow.add(createMainMenuBtn()).fill().expand().pad(10f);
	}

	private static void populateRecords(String titleKey, ArrayMap<LevelResultName, Integer> records, Table table) {
		if (records.size > 0) {
			Label lbl = null;
			Skin skin = Assets.instance.skin.skin;
			lbl = new Label(Assets.instance.translation.get(titleKey) + ":", skin);
			lbl.setWrap(true);
			table.add(lbl).fill().expand().pad(5f);
			table.row();
			for (ObjectMap.Entry<LevelResultName, Integer> record : records) {
				lbl = new Label(Assets.instance.translation.get(record.key.key()), skin);
				lbl.setWrap(true);
				table.add(lbl).fill().expand().pad(5f, 75f, 5f, 5f);

				lbl = new Label(record.key.toString(record.value), skin);
				lbl.setWrap(true);
				lbl.setAlignment(Align.center);
				table.add(lbl).fill().expand().pad(5f, 0f, 5f, 10f);
				table.row();
			}

		}
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
