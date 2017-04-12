package org.dragberry.ozo.game.util;

import com.badlogic.gdx.graphics.Color;

public interface Constants {

    String SETTINGS_PATH = "ozo.settings";

    String ANDROID_PLAY_MARKET_URL = "https://play.google.com/store/apps/details?id=org.dragberry.ozo2";

    String TEXTURE_ATLAS_OBJECTS = "images/default/onezeroone.atlas";
    String TRANSLATION = "i18n/translation";
    String FONTS = "fonts/UbuntuMono-BI.ttf";
    String TEXTURE_ATLAS_SKIN = "skins/default/skin/uiskin.atlas";
    String SKIN = "skins/default/skin/uiskin.json";

    Color BACKGROUND = new Color(0xCC / 255f, 0xCC / 255f,  0x9A / 255f, 1);
    Color BACKGROUND_GAME = new Color(0xCC / 255f, 0xCC / 255f,  0x9A / 255f, 1);

    Color FONT_BLOCK_1 = new Color(70/ 255f, 44/ 255f, 5/ 255f, 1);

    float VIEWPORT_WIDTH = 1280.0f;
    float VIEWPORT_GUI_WIDTH = 720.0f;

    float UNIT_SIZE = 80.0f;
    float UNIT_MOTION_TIME = 0.3f;
    float UNIT_SPEED = UNIT_SIZE / UNIT_MOTION_TIME;

    Color POSITIVE = Color.GREEN;
    Color NEGATIVE = new Color(0xFF / 255f, 0x45 / 255f, 0x00 / 255, 1);
    Color NEUTRAL = new Color(0x1E / 255f, 0x90 / 255f, 0xFF / 255, 1);
}
