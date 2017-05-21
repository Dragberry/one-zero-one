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

    Color BACKGROUND = new Color(0xCCCC9AFF);
    Color BACKGROUND_GAME = new Color(0xCCCC9AFF);

    Color FONT_BLOCK_1 = new Color(0x462C05FF);

    float VIEWPORT_WIDTH = 1280.0f;
    float VIEWPORT_GUI_WIDTH = 720.0f;

    float UNIT_SIZE = 80.0f;
    float UNIT_MOTION_TIME = 0.3f;
    float UNIT_SPEED = UNIT_SIZE / UNIT_MOTION_TIME;

    Color POSITIVE = new Color(0x3FBD1EFF);
    Color POSITIVE_TXT = new Color(0x003200FF);
    Color NEGATIVE = new Color(0xD52805FF);
    Color NEGATIVE_TXT = new Color(0xFFDC10FF);
    Color NEUTRAL = new Color(0x7B7B7BFF);
    Color NEUTRAL_TXT = Color.DARK_GRAY;
}
