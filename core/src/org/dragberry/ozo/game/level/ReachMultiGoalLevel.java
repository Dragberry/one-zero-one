package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.goal.JustReachGoal;
import org.dragberry.ozo.game.level.goal.MultiGoal;
import org.dragberry.ozo.game.level.settings.ReachMultiGoalLevelSettings;

/**
 * Created by maksim on 30.01.17.
 */

public class ReachMultiGoalLevel extends Level<ReachMultiGoalLevelSettings> {

    public ReachMultiGoalLevel(ReachMultiGoalLevelSettings levelInfo) {
        super(levelInfo);
    	addGoalToLose(new JustReachGoal(levelInfo.goalToLose, JustReachGoal.Operator.LESS));
        addGoalToWin(new MultiGoal(levelInfo.goals));
    }

}
