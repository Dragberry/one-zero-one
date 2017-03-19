package org.dragberry.ozo.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by maksim on 18.02.17.
 */

public class CustomStage extends Stage {

    private final ActionExecutor onBackPressed;

    public CustomStage(Viewport viewport, ActionExecutor onBackPressed) {
        super(viewport);
        this.onBackPressed = onBackPressed;
    }

    @Override
    public boolean keyUp(int keyCode) {
        boolean processed = super.keyUp(keyCode);
        switch (keyCode) {
            case Input.Keys.BACK:
            case Input.Keys.ESCAPE:
                onBackPressed.execute();
                break;
        }
        return processed;
    }
}
