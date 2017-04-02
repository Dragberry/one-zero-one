package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.generator.ConstGenerator;
import org.dragberry.ozo.game.level.generator.Generator;
import org.dragberry.ozo.game.level.settings.SequenceReachTheGoalLevelSettings;
import org.dragberry.ozo.game.objects.Unit;

/**
 * Created by maksim on 01.04.17.
 */

public class RepentanceSequenceLevel extends SequenceOf2Level {

    public RepentanceSequenceLevel() {}

    public RepentanceSequenceLevel(SequenceReachTheGoalLevelSettings settings) {
        super(settings);
    }

    @Override
    protected Generator getDefaultGenerator(int x, int y) {
        if (x == 1 && y == 2) {
            return  ConstGenerator.NEG_ONE;
        }
        if (x == 4 && y == 4) {
            return ConstGenerator.ZERO;
        }
        return super.getDefaultGenerator(x, y);
    }

    @Override
    protected void updateSequenceValue() {
        sequenceValue--;
    }

    @Override
    protected String getSequence() {
        StringBuilder seq = new StringBuilder();
        int seqValue = sequenceValue;
        int counter = 0;
        while (counter < 5 && seqValue < 0) {
            if (counter != 0) {
                seq.append(DELIMITER);
            }
            seq.append(-seqValue);
            seqValue++;
            counter++;
        }

        return seq.toString();
    }

    @Override
    protected void updateGeneratorsBeforeStepCalculation() {
        if (isStepResultMatchedSequnceValue() && isCross()) {
            thirdValueState.updatePosition();
            updateSequence();
        }
    }

    private boolean isCross() {
        if (neighbors.size < 4) {
            return false;
        }
        int prevValue = selectedUnit.getValue();
        for (Unit neighbor : neighbors) {
            if (prevValue !=  neighbor.getValue()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void updateGeneratorsAfterStepCalculation() {}
}
