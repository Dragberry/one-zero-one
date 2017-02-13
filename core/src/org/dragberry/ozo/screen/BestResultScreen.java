package org.dragberry.ozo.screen;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.dragberry.ozo.game.level.settings.LevelSettings;
import org.dragberry.ozo.screen.popup.BestResultsPopup;

/**
 * Created by maksim on 28.01.17.
 */
public class BestResultScreen extends AbstractSelectLevelMenuScreen {

    public BestResultScreen(DirectedGame game) {
        super(game, "ozo.bestResults");
    }

    @Override
    protected ClickListener getActionListener(final LevelSettings levelSettings) {
    	return new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setPopup(new BestResultsPopup(game, levelSettings));
            }
        };
    }

}
