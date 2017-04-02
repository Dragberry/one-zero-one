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
    protected void updateSequenceValue() {
        sequenceValue *= 2;
    }

    @Override
    protected String getSequence() {
        StringBuilder seq = new StringBuilder();
        int seqValue = sequenceValue / 2;
        int counter = 0;
        while (counter < 5 && seqValue < 0) {
            if (counter != 0) {
                seq.append(DELIMITER);
            }
            seq.append(-seqValue);
            seqValue /= 2;
            counter++;
        }

        return seq.toString();
    }

}
