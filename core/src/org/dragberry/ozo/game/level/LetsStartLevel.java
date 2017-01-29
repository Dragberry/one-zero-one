package org.dragberry.ozo.game.level;

/**
 * Created by maksim on 29.01.17.
 */

public class LetsStartLevel extends AbstractRichGoalLevel {

    public LetsStartLevel() {
        super(5, -10);
    }

    @Override
    public String getName() {
        return "Let's start!";
    }
}
