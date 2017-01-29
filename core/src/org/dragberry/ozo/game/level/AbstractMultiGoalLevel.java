package org.dragberry.ozo.game.level;

import com.badlogic.gdx.utils.ArrayMap;

import org.dragberry.ozo.game.objects.Unit;

/**
 * Created by maksim on 29.01.17.
 */

public abstract class AbstractMultiGoalLevel extends AbstractLevel {

    private static class Goal {
        int goalValue;
        Unit goalUnit;

        Goal(int goalValue) {
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

    protected Goal[] goals;
    protected int loseCondition;
    protected String winConditionMsg;
    protected String loseConditionMsg;

    public AbstractMultiGoalLevel(int loseCondition, int... goalValues) {
        super();
        this.loseCondition = loseCondition;
        this.goals = new Goal[goalValues.length];
        winConditionMsg = "get ";
        for (int i = 0; i < goalValues.length; i++) {
            this.goals[i] = new Goal(goalValues[i]);
            winConditionMsg += goalValues[i];
            if (i != goalValues.length - 1) {
                winConditionMsg += " and ";
            }
        }
        loseConditionMsg = "get " + loseCondition;
    }

    @Override
    public boolean isLost(Unit[][] units) {
        for (Unit[] row : units) {
            for (Unit unit : row) {
                if (unit.value == loseCondition) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isWon(Unit[][] units) {
        clearGoals();
        for (Unit[] row : units) {
            unitLoop: for (Unit unit : row) {
                for (Goal goal : goals) {
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
        for (Goal goal : goals) {
            if (!goal.isGoalReached()) {
                goalReached = false;
                break;
            }
        }
        return goalReached;
    }

    @Override
    public String getWinConditionMsg() {
        return winConditionMsg;
    }

    @Override
    public String getLoseConditionMsg() {
        return loseConditionMsg;
    }

    private void clearGoals() {
        for (Goal goal : goals) {
            goal.goalUnit = null;
        }
    }
}
