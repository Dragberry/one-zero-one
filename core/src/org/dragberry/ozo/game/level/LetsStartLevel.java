package org.dragberry.ozo.game.level;

import com.badlogic.gdx.Game;

import org.dragberry.ozo.game.objects.Unit;
import org.dragberry.ozo.screen.AbstractGameScreen;

/**
 * Created by maksim on 29.01.17.
 */

public class LetsStartLevel extends AbstractRichGoalLevel {

    public LetsStartLevel() {
        super(5, 10);
    }

    @Override
    public String getName() {
        return "Let's start!";
    }
}
