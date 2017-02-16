package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.generator.ConstGenerator;
import org.dragberry.ozo.game.level.generator.Generator;
import org.dragberry.ozo.game.level.settings.ReachTheGoalLevelSettings;

/**
 * Created by maksim on 31.01.17.
 */

public class MushroomRainLevel extends ReachTheGoalLevel {

    public MushroomRainLevel(ReachTheGoalLevelSettings levelInfo) {
        super(levelInfo);
    }

    @Override
    protected Generator getDefaultGenerator(int x, int y) {
    	if (x == 0 || x == width - 1) {
            return ConstGenerator.ZERO;
        }
    	if (y == 0) {
            return ConstGenerator.POS_ONE;
        }
        if (y == height - 1) {
            return ConstGenerator.NEG_ONE;
        }
        return super.getDefaultGenerator(x, y);
    }
}
