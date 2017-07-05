package org.dragberry.ozo.game.level.goal;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by maksim on 01.02.17.
 */

public abstract class AbstractGoal implements Goal {

    public Vector2 dimension = new Vector2();
    
    protected String msg;
    protected float time;

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

    public boolean isAlmostReached(int minValue) {
        return false;
    }

    public void markAsAlmostReached(boolean flag) {
        // empty by default
    }
}
