package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.generator.ConstGenerator;
import org.dragberry.ozo.game.level.generator.Generator;
import org.dragberry.ozo.game.level.generator.WavesGenerator;
import org.dragberry.ozo.game.level.settings.ReachTheGoalLevelSettings;

/**
 * Created by maksim on 13.02.17.
 */

public class WavesLevel extends ReachTheGoalLevel {

    private WavesGenerator generator = new WavesGenerator(-1, -1, width, height);

    public WavesLevel() {}

    public WavesLevel(ReachTheGoalLevelSettings settings) {
        super(settings);
    }

    @Override
    protected Generator getDefaultGenerator(int x, int y) {
        return steps == 0 ? ConstGenerator.ZERO : generator;
    }

    @Override
    public void reset(boolean cleanState) {
        super.reset(cleanState);
        generator.reset(width, height);
    }
}
