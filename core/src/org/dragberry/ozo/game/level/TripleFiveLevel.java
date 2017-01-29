package org.dragberry.ozo.game.level;

/**
 * Created by maksim on 29.01.17.
 */

public class TripleFiveLevel extends AbstractMultiGoalLevel {


    public TripleFiveLevel() {
        super(-10, 5, 5, 5);
    }

    @Override
    public String getName() {
        return "Triple Five";
    }
}
