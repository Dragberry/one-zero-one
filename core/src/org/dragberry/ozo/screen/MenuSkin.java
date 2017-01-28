package org.dragberry.ozo.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import org.dragberry.ozo.game.Assets;

/**
 * Created by maksim on 28.01.17.
 */
public class MenuSkin {

    public static final Color BACKGROUND_COLOR = new Color(0xC3 / 255f, 0xD5 / 255f,  0xDB / 255f, 1);

    private static Skin SKIN;

    private MenuSkin() {}

    public static Skin getSkin() {
        if (SKIN == null) {
            SKIN = new Skin();
            SKIN.add("default", Assets.instance.fonts._34);

            // Create a texture
            Pixmap pixmap = new Pixmap((int) Gdx.graphics.getWidth() / 4, (int) Gdx.graphics.getHeight() / 10, Pixmap.Format.RGB888);
            pixmap.setColor(BACKGROUND_COLOR);
            pixmap.fill();
            SKIN.add("background", new Texture(pixmap));

            // Create a button style
            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
            textButtonStyle.up = SKIN.newDrawable("background", Color.GRAY);
            textButtonStyle.down = SKIN.newDrawable("background", Color.DARK_GRAY);
            textButtonStyle.checked = SKIN.newDrawable("background", Color.DARK_GRAY);
            textButtonStyle.over = SKIN.newDrawable("background", Color.LIGHT_GRAY);
            textButtonStyle.font = SKIN.getFont("default");
            SKIN.add("default", textButtonStyle);
        }
        return SKIN;
    }


}
