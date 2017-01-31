package org.dragberry.ozo.game.level.goal;

import org.dragberry.ozo.game.objects.GoalUnit;
import org.dragberry.ozo.game.objects.Unit;
import org.dragberry.ozo.game.util.Constants;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by maksim on 30.01.17.
 */

public class MultiGoal extends AbstractGoal {

    private static class SimpleGoal {
        int goalValue;
        Unit goalUnit;
        GoalUnit renderUnit;

        SimpleGoal(int goalValue) {
            this.goalValue = goalValue;
            this.renderUnit = new GoalUnit(goalValue);
        }

        boolean isGoalReached() {
            return goalUnit != null;
        }

        @Override
        public String toString() {
            return "Goal[" + goalValue + "] is " + goalUnit != null ? "" : "NOT" + "reached";
        }
    }

    protected SimpleGoal[] goals;

    public MultiGoal(Integer... goalValues) {
        this.goals = new SimpleGoal[goalValues.length];
        for (int i = 0; i < goalValues.length; i++) {
            this.goals[i] = new SimpleGoal(goalValues[i]);
        }
        this.dimension = new Vector2(Constants.UNIT_SIZE * goals.length, Constants.UNIT_SIZE);
    }

    @Override
    public boolean isReached(Unit[][] units, Unit selectedUnit, Unit[] neighbors) {
        clearGoals();
        for (Unit[] row : units) {
            unitLoop: for (Unit unit : row) {
                for (SimpleGoal goal : goals) {
                    if (goal.isGoalReached()) {
                        continue;
                    }
                    if (unit.value == goal.goalValue) {
                        goal.goalUnit = unit;
                        continue unitLoop;
                    }
                }
            }
        }
        boolean goalReached = true;
        for (SimpleGoal goal : goals) {
            if (!goal.isGoalReached()) {
                goalReached = false;
                break;
            }
        }
        return goalReached;
    }

    @Override
    public String getMessage() {
        return null;
    }

    private void clearGoals() {
        for (SimpleGoal goal : goals) {
            goal.goalUnit = null;
        }
    }

	@Override
	public void render(SpriteBatch batch, float x, float y) {
		 for (SimpleGoal goal : goals) {
			 goal.renderUnit.setPosition(x, y);
			 goal.renderUnit.render(batch);
			 x += goal.renderUnit.dimension.x;
		 }
	}

}
