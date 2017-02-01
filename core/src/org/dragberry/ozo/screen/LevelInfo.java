package org.dragberry.ozo.screen;

import org.dragberry.ozo.game.level.Level;

/**
 * Created by maksim on 01.02.17.
 */
public class LevelInfo {
    public Class<? extends Level> clazz;
    public Object[] params;

    LevelInfo(Class<? extends Level> clazz, String name, Object... params) {
        this.clazz = clazz;
        this.params = new Object[params.length + 1];
        this.params[0] = name;
        System.arraycopy(params, 0, this.params, 1, params.length);
    }

    public String getName() {
        return (String) params[0];
    }
}
