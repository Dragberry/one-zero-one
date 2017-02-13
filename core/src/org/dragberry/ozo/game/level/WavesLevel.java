package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.generator.Generator;
import org.dragberry.ozo.game.level.generator.WavesGeneratorHelper;
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
        return WavesGeneratorHelper.getGenerator(x, y, width - 1, height - 1, steps);
    }
}
