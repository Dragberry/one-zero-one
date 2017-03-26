package org.dragberry.ozo.game.level.generator;

/**
 * Created by maksim on 25.03.17.
 */

public class FibonacciGenerator extends SequenceOf2Generator {

    public FibonacciGenerator(int initialValue, int x, int y, ThirdValueState thirdValueState) {
        super(initialValue, x, y, thirdValueState);
    }

    @Override
    protected int defaultValue() {
        return value == 0 ? 1 : 0;
    }
}
