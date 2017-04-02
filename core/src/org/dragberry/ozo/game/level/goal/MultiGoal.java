package org.dragberry.ozo.game.level.goal;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.objects.GoalUnit;
import org.dragberry.ozo.game.objects.Unit;
import org.dragberry.ozo.game.util.StringConstants;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

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

    public MultiGoal(int... goalValues) {
        this.goals = new SimpleGoal[goalValues.length];
        for (int i = 0; i < goalValues.length; i++) {
            this.goals[i] = new SimpleGoal(goalValues[i]);
        }
        this.dimension = new Vector2(GoalUnit.SIZE * goals.length, GoalUnit.SIZE);
        this.msg = buildMessage();
    }

    @Override
    public boolean isReached(Unit[][] units, Unit selectedUnit,Array<Unit> neighbors) {
        clearGoals();
        for (Unit[] row : units) {
            unitLoop: for (Unit unit : row) {
                for (SimpleGoal goal : goals) {
                    if (goal.isGoalReached()) {
                        continue;
                    }
                    if (unit.getValue() == goal.goalValue) {
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

    public String buildMessage() {
        boolean allTheSame = true;
        int previous = 0;
    	StringBuilder values = new StringBuilder(StringConstants.EMPTY);
    	for (int index = 0; index < goals.length; index++) {
            if (allTheSame && previous != 0 && previous != goals[index].goalValue) {
                allTheSame = false;
            }
            previous = goals[index].goalValue;

            values.append(goals[index].goalValue);
            if (index != goals.length - 1 ) {
                values.append(StringConstants.COMMA).append(StringConstants.SPACE);
            }
    	}
        return Assets.instance.translation.format(
                allTheSame ? "ozo.goal.multiGoalAllTheSame" : "ozo.goal.multiGoal", goals.length,
                allTheSame ? goals[0].goalValue : values);
    }

    private void clearGoals() {
        for (SimpleGoal goal : goals) {
            goal.goalUnit = null;
        }
    }

	@Override
	public void render(SpriteBatch batch, float x, float y) {
		 for (SimpleGoal goal : goals) {
			 goal.renderUnit.position.set(x, y);
			 goal.renderUnit.render(batch);
			 x += (goal.renderUnit.dimension.x * GoalUnit.SCALE);
		 }
	}

}
