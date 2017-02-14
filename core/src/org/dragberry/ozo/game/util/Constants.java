package org.dragberry.ozo.game.util;

import com.badlogic.gdx.graphics.Color;

public interface Constants {

    String TEXTURE_ATLAS_OBJECTS = "images/onezeroone.atlas";
    String TRANSLATION = "i18n/translation";
    String FONTS = "fonts/UbuntuMono-BI.ttf";
    String TEXTURE_ATLAS_SKIN = "skins/default/skin/uiskin.atlas";
    String SKIN = "skins/default/skin/uiskin.json";
    
    Color BACKGROUND = new Color(0xC3 / 255f, 0xD5 / 255f,  0xDB / 255f, 1);
    
    float VIEWPORT_WIDTH = 1280.0f;
    float VIEWPORT_GUI_WIDTH = 720.0f;

    float UNIT_SIZE = 80.0f;
    float UNIT_MOTION_TIME = 0.3f;
    float UNIT_SPEED = UNIT_SIZE / UNIT_MOTION_TIME;

    Color POSITIVE = Color.GREEN;
    Color NEGATIVE = new Color(0xFF / 255f, 0x45 / 255f, 0x00 / 255, 1);
    Color NEUTRAL = new Color(0x1E / 255f, 0x90 / 255f, 0xFF / 255, 1);
}
