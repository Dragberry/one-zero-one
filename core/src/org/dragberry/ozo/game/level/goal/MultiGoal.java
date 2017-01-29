package org.dragberry.ozo.game.level.goal;

import org.dragberry.ozo.game.level.AbstractMultiGoalLevel;
import org.dragberry.ozo.game.objects.Unit;

/**
 * Created by maksim on 30.01.17.
 */

public class MultiGoal implements Goal {

    private static class SimpleGoal {
        int goalValue;
        Unit goalUnit;

        SimpleGoal(int goalValue) {
            this.goalValue = goalValue;
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
}
