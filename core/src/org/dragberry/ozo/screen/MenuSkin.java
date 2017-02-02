package org.dragberry.ozo.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import org.dragberry.ozo.game.Assets;

/**
 * Created by maksim on 28.01.17.
 */
public class MenuSkin {

    public static final Color BACKGROUND_COLOR = new Color(0xC3 / 255f, 0xD5 / 255f,  0xDB / 255f, 1);
    public static final String DEFAULT = "default";
    public static final String BACKGROUND = "background";

    private static Skin SKIN;

    private MenuSkin() {}

    public static Skin getSkin() {
        if (SKIN == null) {
            SKIN = new Skin();
            SKIN.add(DEFAULT, Assets.instance.fonts.game_68);

            // Create a texture
            Pixmap pixmap = new Pixmap((int) Gdx.graphics.getWidth() / 4, (int) Gdx.graphics.getHeight() / 10, Pixmap.Format.RGB888);
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
            SKIN.add(BACKGROUND, new Texture(pixmap));

            // Create a button style
            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
            textButtonStyle.up = SKIN.newDrawable(BACKGROUND, Color.GRAY);
            textButtonStyle.down = SKIN.newDrawable(BACKGROUND, Color.DARK_GRAY);
            textButtonStyle.checked = SKIN.newDrawable(BACKGROUND, Color.DARK_GRAY);
            textButtonStyle.over = SKIN.newDrawable(BACKGROUND, Color.LIGHT_GRAY);
            textButtonStyle.font = SKIN.getFont(DEFAULT);
            SKIN.add(DEFAULT, textButtonStyle);

            // Create a label skin
            Label.LabelStyle labelStyle = new Label.LabelStyle();
            labelStyle.background = SKIN.newDrawable(BACKGROUND, Color.GRAY);
            labelStyle.font = SKIN.getFont(DEFAULT);
            SKIN.add(DEFAULT, labelStyle);
        }
        return SKIN;
    }

    public static void dispose() {
        SKIN.dispose();
        SKIN = null;
    }
}
