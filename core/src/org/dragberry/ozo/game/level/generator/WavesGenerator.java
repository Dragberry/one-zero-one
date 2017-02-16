package org.dragberry.ozo.game.level.generator;

/**
 * Created by maksim on 16.02.17.
 */

public class WavesGenerator extends Generator {

    private int step;
    private int previousStep;

    private int unitPerStepCnt = 0;

    private int cornerCnt;
    private int borderCnt;
    private int defaultCnt;

    public WavesGenerator(int x, int y) {
        super(x, y);
    }

    @Override
    public int next() {
        return 0;
    }

    public void update(int step) {
        if (step != previousStep) {
            previousStep = step;
        }
        this.step = step;
        unitPerStepCnt++;
    }
}
