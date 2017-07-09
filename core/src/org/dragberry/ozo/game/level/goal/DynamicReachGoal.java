package org.dragberry.ozo.game.level.goal;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.objects.GoalUnit;
import org.dragberry.ozo.game.objects.Unit;

/**
 * Created by maksim on 29.01.17.
 */

public class DynamicReachGoal extends AbstractGoal {

    private GoalUnit unit;
    public int goal;

    private final int initialGoal;

    public DynamicReachGoal(int initialGoal, int goal) {
        this.initialGoal = initialGoal;
        this.goal = goal;
        this.unit = new GoalUnit(goal);
        this.dimension = unit.dimension;
    }

    @Override
    public boolean isReached(Unit[][] units, Unit selectedUnit, Array<Unit> neighbors) {
        for (Unit[] row : units) {
            for (Unit unit : row) {
                if (goal >= unit.getValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void render(SpriteBatch batch, float x, float y) {
        unit.position.set(x, y);
        unit.render(batch);
    }

    public void updateGoal(int goalValue) {
        goal = goalValue;
        unit.setValue(goalValue);
    }

    @Override
    public void reset(boolean restore) {
        if (!restore) {
            goal = initialGoal;
            unit.setValue(goal);
        }
    }

    @Override
    public void update(float deltaTime) {
        unit.update(deltaTime);
    }

    @Override
    public boolean isAlmostReached(int value) {
        return Math.abs(value - goal) < 3;
    }

    @Override
    public void markAsAlmostReached(boolean flag) {
        unit.isPulsated = flag;
    }
}