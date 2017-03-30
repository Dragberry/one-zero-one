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
import org.dragberry.ozo.game.DirectedGame;
import org.dragberry.ozo.screen.ActionExecutor;

import java.util.Map;

/**
 * Created by maksim on 30.03.17.
 */

public abstract class AbstractGameFinishedPopup extends AbstractPopup {

    private NewLevelResultsResponse results;

    protected Label bestResultLbl;

    private TextButton playAgainBtn;
    private TextButton mainMenuBtn;

    private Table resultTable;

    public AbstractGameFinishedPopup(DirectedGame game) {
        super(game);
    }

    public AbstractGameFinishedPopup init(NewLevelResultsResponse results) {
        this.results = results;
        return this;
    }

    @Override
    protected void rebuildStage() {
        popupWindow.clear();
        resultTable.clear();

        popupWindow.add(getTitleLabel()).fill().expand();
        popupWindow.row().pad(10f);

        if (results.getResults().isEmpty()) {
            popupWindow.add(getNoNewResultsLabel()).fill().expand();
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

        addButtons();
    }

    protected abstract Label getTitleLabel();

    protected abstract Label getNoNewResultsLabel();

    protected void addButtons() {
        popupWindow.add(playAgainBtn).fill().expand().pad(10f);
        popupWindow.row();
        popupWindow.add(mainMenuBtn).fill().expand().pad(10f);
    }

    @Override
    protected void buildStage(float viewportWidth, float viewportHeight) {
        popupWindow.setWidth(viewportWidth * 0.75f);
        popupWindow.setHeight(viewportHeight * 0.75f);

        createLabels();
        createButtons();

        resultTable = new Table();

        rebuildStage();
    }

    protected void createLabels() {
        bestResultLbl = new Label(Assets.instance.translation.get("ozo.newRecord"), Assets.instance.skin.skin);
        bestResultLbl.setAlignment(Align.center);
        bestResultLbl.setWrap(true);
    }

    protected void createButtons() {
        playAgainBtn = createPlayAgainBtn();
        mainMenuBtn = createMainMenuBtn();
    }

    protected static void populateRecords(String titleKey, ArrayMap<LevelResultName, Integer> records, Table table) {
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

    protected TextButton createPlayAgainBtn() {
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

    protected TextButton createMainMenuBtn() {
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
