package org.dragberry.ozo.game.level.generator;

/**
 * Created by maksim on 16.02.17.
 */

public class WavesGenerator extends Generator {

    private int xBorder;
    private int yBorder;

    private int previousStep;

    private int corner;
    private int border;
    private int mid;

    public WavesGenerator(int x, int y, int width, int height) {
        super(x, y);
        reset(width, height);
    }

    public void reset(int width, int height) {
        this.xBorder = width - 1;
        this.yBorder = height - 1;
        reset();
    }

    @Override
    public int next(int step, int selectedX, int selectedY) {
        boolean stepChanged = step != previousStep;
        if (stepChanged) {
            previousStep = step;
        }
        if (selectedX == 0 && selectedY == 0
                || selectedX == 0 && selectedY == yBorder
                || selectedX == xBorder && selectedY == 0
                || selectedX == xBorder && selectedY == yBorder) {
            if (stepChanged) {
                corner = -corner;
            }
            return corner;
        } else if (selectedX == 0 || selectedX == xBorder || selectedY == 0 || selectedY == yBorder) {
            if (stepChanged) {
                border = -border;
            }
            return border;
        } else {
            if (stepChanged) {
                mid = -mid;
            }
            return mid;
        }
    }

    @Override
    public void reset() {
        previousStep = 0;
        corner = 1;
        border = 1;
        mid = 1;
    }
}
