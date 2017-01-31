package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.goal.JustReachGoal;

/**
 * Created by maksim on 30.01.17.
 */

public class ReachTheGoalLevel extends Level {

    public ReachTheGoalLevel(String levelName, Integer goalToLose, Integer goalToWin) {
    	this(levelName, goalToLose, goalToWin, JustReachGoal.Operator.EQUALS);
    }

    public ReachTheGoalLevel(String levelName, Integer goalToLose, Integer goalToWin, JustReachGoal.Operator operator) {
        super(levelName);
    	addGoalToWin(new JustReachGoal(goalToWin.intValue(), operator));
        addGoalToLose(new JustReachGoal(goalToLose.intValue(), JustReachGoal.Operator.LESS));
    }

}
