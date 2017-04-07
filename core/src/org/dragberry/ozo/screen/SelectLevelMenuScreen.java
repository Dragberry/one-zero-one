package org.dragberry.ozo.screen;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import org.dragberry.ozo.game.level.settings.LevelSettings;

/**
 * Created by maksim on 28.01.17.
 */
public class SelectLevelMenuScreen extends AbstractSelectLevelMenuScreen {

    public SelectLevelMenuScreen(org.dragberry.ozo.game.DirectedGame game) {
        super(game, "ozo.selectLevel");
    }

    @Override
    protected void addButtonListener(final TextButton btn, final LevelSettings levelSettings) {
        btn.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!btn.isDisabled()) {
                    game.playLevel(levelSettings, SelectLevelMenuScreen.this.getClass());
                }
            }

        });
    }

    protected boolean isButtonDisabled(LevelState state) {
        return state == LevelState.CLOSED;
    }
}
