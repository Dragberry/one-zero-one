package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.settings.ReachTheGoalLevelSettings;

/**
 * Created by maksim on 01.04.17.
 */

public class DoublingLevel extends SequenceOf2Level {

    public DoublingLevel() {}

    public DoublingLevel(ReachTheGoalLevelSettings settings) {
        super(settings);
    }

    @Override
    protected void updateSequence() {
        sequenceValue *= 2;
    }

}
