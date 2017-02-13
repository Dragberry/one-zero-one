package org.dragberry.ozo.game.level.generator;

/**
 * Created by maksim on 13.02.17.
 */

public final class WavesGeneratorHelper {

    private WavesGeneratorHelper() {}

    public static final Generator getGenerator(int x, int y, int xEnd, int yEnd, int steps) {
        if (steps == 0
                || (x == 0 && y == 0)
                || (x == xEnd && y == 0)
                || (x == 0 && y == yEnd)
                || (x == xEnd && y == yEnd)) {
            return ConstGenerator.ZERO;
        }
        if (steps % 2 == 0) {
            return ConstGenerator.POS_ONE;
        } else {
            return ConstGenerator.NEG_ONE;
        }
    }
}
