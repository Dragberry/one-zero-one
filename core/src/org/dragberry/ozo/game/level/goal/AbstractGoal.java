package org.dragberry.ozo.game.level.goal;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by maksim on 01.02.17.
 */

public abstract class AbstractGoal implements Goal {

    public Vector2 dimension;

    @Override
    public void update(float deltaTime) {
        // empty by default
    }

}
