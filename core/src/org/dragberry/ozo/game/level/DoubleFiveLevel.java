package org.dragberry.ozo.game.level;

/**
 * Created by maksim on 28.01.17.
 */

public class DoubleFiveLevel extends AbstractMultiGoalLevel {


    public DoubleFiveLevel() {
        super(-10, 5, 5);
    }

    @Override
    public String getName() {
        return "Double Five";
    }
}
