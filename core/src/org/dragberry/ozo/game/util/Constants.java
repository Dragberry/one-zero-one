package org.dragberry.ozo.game.util;

import com.badlogic.gdx.graphics.Color;

public interface Constants {

    String TEXTURE_ATLAS_OBJECTS = "images/onezeroone.atlas";
    String TRANSLATION = "i18n/translation";
    
    float VIEWPORT_WIDTH = 1280.0f;
    float VIEWPORT_GUI_WIDTH = 720.0f;

    float UNIT_SIZE = 80.0f;
    float UNIT_MOTION_TIME = 0.3f;
    float UNIT_SPEED = UNIT_SIZE / UNIT_MOTION_TIME;

    Color POSITIVE = Color.GREEN;
    Color NEGATIVE = new Color(0xFF / 255f, 0x45 / 255f, 0x00 / 255, 1);
    Color NEUTRAL = new Color(0x1E / 255f, 0x90 / 255f, 0xFF / 255, 1);
}
