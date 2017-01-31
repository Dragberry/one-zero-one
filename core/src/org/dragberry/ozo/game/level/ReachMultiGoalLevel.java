package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.goal.JustReachGoal;
import org.dragberry.ozo.game.level.goal.MultiGoal;

/**
 * Created by maksim on 30.01.17.
 */

public class ReachMultiGoalLevel extends Level {

    public ReachMultiGoalLevel(String levelName, Integer goalToLose, Integer... goals) {
        super(levelName);
    	addGoalToLose(new JustReachGoal(goalToLose, JustReachGoal.Operator.LESS));
        addGoalToWin(new MultiGoal(goals));
    }

}
