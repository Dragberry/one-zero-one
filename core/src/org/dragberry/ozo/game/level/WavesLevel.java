package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.generator.Generator;
import org.dragberry.ozo.game.level.generator.WavesGenerator;
import org.dragberry.ozo.game.level.generator.WavesGeneratorHelper;
import org.dragberry.ozo.game.level.settings.ReachTheGoalLevelSettings;
import org.dragberry.ozo.game.objects.Unit;

/**
 * Created by maksim on 13.02.17.
 */

public class WavesLevel extends ReachTheGoalLevel {

    private WavesGenerator generator = new WavesGenerator(-1, -1);

    public WavesLevel(ReachTheGoalLevelSettings settings) {
        super(settings);
    }

    @Override
    protected Generator getDefaultGenerator(int x, int y) {
        generator.update(steps);
        return generator;
    }
}
