package org.dragberry.ozo.game.level;

import com.badlogic.gdx.math.MathUtils;

import java.text.MessageFormat;

/**
 * Created by maksim on 31.01.17.
 */

public class MashroomRainLevel extends ReachTheGoalLevel {

    public MashroomRainLevel(Integer goalToLose, Integer goal) {
        super(goalToLose, goal);
    }

    @Override
    public int generateValue(int x, int y) {
        if (y == 0) {
            return 1;
        }
        if (y == height - 1) {
            return -1;
        }
        if (x == 0 || x == width - 1) {
            return 0;
        }
        return MathUtils.random(-1, 1);
//        throw new IllegalArgumentException(MessageFormat.format("Cannot generate value for Unit[{0}][{1}]!", x, y));
    }
}
