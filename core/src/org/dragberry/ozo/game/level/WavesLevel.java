package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.generator.ConstGenerator;
import org.dragberry.ozo.game.level.generator.Generator;
import org.dragberry.ozo.game.level.settings.ReachTheGoalLevelSettings;

/**
 * Created by maksim on 13.02.17.
 */

public class WavesLevel extends ReachTheGoalLevel {

    public WavesLevel(ReachTheGoalLevelSettings settings) {
        super(settings);
    }

    @Override
    protected Generator getDefaultGenerator(int x, int y) {
        if (steps == 0
                || (x == 0 && y == 0)
                || (x == width - 1 && y == 0)
                || (x == 0 && y == height - 1)
                || (x == width - 1 && y == height - 1)) {
            return ConstGenerator.ZERO;
        }
        if (steps % 2 == 0) {
            return ConstGenerator.POS_ONE;
        } else {
            return ConstGenerator.NEG_ONE;
        }
    }
}
