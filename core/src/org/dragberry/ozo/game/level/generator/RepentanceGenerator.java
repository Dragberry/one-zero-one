package org.dragberry.ozo.game.level.generator;

/**
 * Created by maksim on 25.03.17.
 */

public class RepentanceGenerator extends SequenceOf2Generator {

    public RepentanceGenerator(int initialValue, int x, int y, ThirdValueState thirdValueState) {
        super(initialValue, x, y, thirdValueState);
    }

    @Override
    protected int defaultValue() {
        return value == 0 ? -1 : 0;
    }
}
