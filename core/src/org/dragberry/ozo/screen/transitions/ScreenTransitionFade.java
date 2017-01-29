package org.dragberry.ozo.screen.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

/**
 * Created by maksim on 29.01.17.
 */

public class ScreenTransitionFade implements ScreenTransition {

    private static final ScreenTransitionFade INSTANCE = new ScreenTransitionFade();

    private float duration;

    public static ScreenTransitionFade init(float duration) {
        INSTANCE.duration = duration;
        return INSTANCE;
    }

    @Override
    public float getDuration() {
        return duration;
    }

    @Override
    public void render(SpriteBatch batch, Texture currScreen, Texture nextScreen, float alpha) {
        float width = currScreen.getWidth();
        float height = currScreen.getHeight();
        alpha = Interpolation.fade.apply(alpha);

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.setColor(1, 1, 1, 1);
        batch.draw(currScreen, 0, 0, 0, 0, width, height, 1, 1, 0, 0, 0,
                currScreen.getWidth(), currScreen.getHeight(),
                false, true);
        batch.setColor(1, 1, 1, alpha);
        batch.draw(nextScreen, 0, 0, 0, 0, width, height, 1, 1, 0, 0, 0,
                nextScreen.getWidth(), nextScreen.getHeight(),
                false, true);
        batch.end();
    }
}
