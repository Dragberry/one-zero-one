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
    private int goal;

    public DynamicReachGoal(int goal) {
        this.goal = goal;
        this.unit = new GoalUnit(goal);
        this.dimension = unit.dimension;
        this.msg = Assets.instance.translation.format("ozo.goal.justReahGoalLE", goal);
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

}