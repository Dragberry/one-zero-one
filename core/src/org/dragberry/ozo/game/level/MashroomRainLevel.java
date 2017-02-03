package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.generator.ConstGenerator;
import org.dragberry.ozo.game.level.generator.Generator;

/**
 * Created by maksim on 31.01.17.
 */

public class MashroomRainLevel extends ReachTheGoalLevel {

    public MashroomRainLevel(ReachTheGoalLevel.ReachTheGoalLevelInfo levelInfo) {
        super(levelInfo);
    }

    @Override
    protected Generator getDefaultGenerator(int x, int y) {
        if (y == 0) {
            return ConstGenerator.POS_ONE;
        }
        if (y == height - 1) {
            return ConstGenerator.NEG_ONE;
        }
        if (x == 0 || x == width - 1) {
            return ConstGenerator.ZERO;
        }
        return super.getDefaultGenerator(x, y);
    }
}
