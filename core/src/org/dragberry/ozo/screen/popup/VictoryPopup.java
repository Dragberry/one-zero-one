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
import org.dragberry.ozo.game.DirectedGame;

import java.util.Map;

public class VictoryPopup extends AbstractPopup {

	public VictoryPopup init(NewLevelResultsResponse results) {
		this.results = results;
		return this;
	}

	private NewLevelResultsResponse results;

	private Label congrLbl;
	private Label wonLbl;
	private Label bestResultLbl;

	private TextButton nextLevelBtn;
	private TextButton playAgainBtn;
	private TextButton mainMenuBtn;

	private Table resultTable;

	public VictoryPopup(DirectedGame game) {
		super(game);
	}

	@Override
	protected void rebuildStage() {
		popupWindow.clear();
		resultTable.clear();

		popupWindow.add(congrLbl).fill().expand();
		popupWindow.row().pad(10f);

		if (results.getResults().isEmpty()) {
			popupWindow.add(wonLbl).fill().expand();
			popupWindow.row();
		} else {
			popupWindow.add(bestResultLbl).fill().expand();
			popupWindow.row().expand().fill();

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

		popupWindow.add(nextLevelBtn).fill().expand().pad(10f);
		popupWindow.row();
		popupWindow.add(playAgainBtn).fill().expand().pad(10f);
		popupWindow.row();
		popupWindow.add(mainMenuBtn).fill().expand().pad(10f);
	}

	@Override
	protected void buildStage(float viewportWidth, float viewportHeight) {
		popupWindow.setWidth(viewportWidth * 0.75f);
		popupWindow.setHeight(viewportHeight * 0.75f);

		Skin skin = Assets.instance.skin.skin;
		congrLbl = new Label(Assets.instance.translation.get("ozo.congratulations"), skin);
		congrLbl.setAlignment(Align.center);

		wonLbl = new Label(Assets.instance.translation.get("ozo.levelCompleted"), skin);
		wonLbl.setAlignment(Align.center);

		bestResultLbl = new Label(Assets.instance.translation.get("ozo.newRecord"), skin);
		bestResultLbl.setAlignment(Align.center);
		bestResultLbl.setWrap(true);

		resultTable = new Table();

		popupWindow.add(congrLbl).fill().expand();
		popupWindow.row().pad(10f);

		nextLevelBtn = createNextBtn();
		playAgainBtn = createPlayAgainBtn();
		mainMenuBtn = createMainMenuBtn();

		rebuildStage();
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

}
