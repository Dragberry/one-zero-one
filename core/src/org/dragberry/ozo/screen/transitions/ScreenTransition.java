package org.dragberry.ozo.screen.transitions;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by maksim on 29.01.17.
 */
public interface ScreenTransition {

    float getDuration();

    void render(SpriteBatch batch, Texture currScreen, Texture nextScreen, float alpha);
}
