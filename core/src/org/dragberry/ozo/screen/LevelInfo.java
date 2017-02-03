package org.dragberry.ozo.screen;

import org.dragberry.ozo.game.level.Level;

/**
 * Created by maksim on 01.02.17.
 */
public class LevelInfo {
    public final Class<? extends Level<? extends LevelInfo>> clazz;
    public final String name;
    
    public boolean completed;
    public float bestTime;
    public float bestSteps;

    public LevelInfo(Class<? extends Level<? extends LevelInfo>> clazz, String name) {
        this.clazz = clazz;
        this.name = name;
    }

}
