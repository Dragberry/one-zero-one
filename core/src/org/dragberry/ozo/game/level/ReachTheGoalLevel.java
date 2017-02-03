package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.goal.JustReachGoal;
import org.dragberry.ozo.game.level.settings.ReachTheGoalLevelSettings;

/**
 * Created by maksim on 30.01.17.
 */

public class ReachTheGoalLevel extends Level<ReachTheGoalLevelSettings> {

    public ReachTheGoalLevel(ReachTheGoalLevelSettings levelInfo) {
    	super(levelInfo);
    	addGoalToWin(new JustReachGoal(levelInfo.goal, levelInfo.operator));
        addGoalToLose(new JustReachGoal(levelInfo.goalToLose, JustReachGoal.Operator.LESS));
    }

}
