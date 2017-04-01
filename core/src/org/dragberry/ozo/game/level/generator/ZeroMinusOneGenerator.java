package org.dragberry.ozo.game.level.generator;

/**
 * Created by maksim on 01.04.17.
 */

public class ZeroMinusOneGenerator extends SequenceOf2Generator {

    public ZeroMinusOneGenerator() {}

    public ZeroMinusOneGenerator(int initialValue, int x, int y, ThirdValueState thirdValueState) {
        super(initialValue, x, y, thirdValueState);
    }

    @Override
    protected int defaultValue() {
        return value == 0 ? -1 : 0;
    }
}
