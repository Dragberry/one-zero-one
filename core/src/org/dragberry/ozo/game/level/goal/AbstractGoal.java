package org.dragberry.ozo.game.level.goal;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by maksim on 01.02.17.
 */

public abstract class AbstractGoal implements Goal {

    public Vector2 dimension;
    
    protected String msg;

    @Override
    public String getMessage() {
    	return msg;
    }

    @Override
    public void update(float deltaTime) {
        // empty by default
    }

    @Override
    public void reset(boolean restore) {
        // empty by default
    }
}
