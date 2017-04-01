package org.dragberry.ozo.screen;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import org.dragberry.ozo.game.level.settings.LevelSettings;
import org.dragberry.ozo.screen.popup.BestResultsPopup;

/**
 * Created by maksim on 28.01.17.
 */
public class BestResultScreen extends AbstractSelectLevelMenuScreen {

    public BestResultScreen(org.dragberry.ozo.game.DirectedGame game) {
        super(game, "ozo.bestResults");
    }

    @Override
    protected void addButtonListener(LevelState state, TextButton btn, final LevelSettings levelSettings) {
        btn.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setPopup(game.getScreen(BestResultsPopup.class).init(levelSettings));
            }
        });
    }

    @Override
    protected void addLevelsAtTop(Table scrollTable) {
        scrollTable.add(createLevelBtn(LevelState.COMPLETED, game.levelProvider.freeplayLevel))
                .fillX().expand(true, false).pad(5, 10, 5, 10);
        scrollTable.row();
    }
}
