package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.goal.JustReachGoal;

/**
 * Created by maksim on 30.01.17.
 */

public class ReachTheGoalLevel extends Level {

    public ReachTheGoalLevel(Integer goalToLose, Integer goalToWin) {
        this(goalToLose, goalToWin, JustReachGoal.Operator.EQUALS);
    }

    public ReachTheGoalLevel(Integer goalToLose, Integer goalToWin, JustReachGoal.Operator operator) {
        addGoalToWin(new JustReachGoal(goalToWin.intValue(), operator));
        addGoalToLose(new JustReachGoal(goalToLose.intValue(), JustReachGoal.Operator.LESS));
    }

}
