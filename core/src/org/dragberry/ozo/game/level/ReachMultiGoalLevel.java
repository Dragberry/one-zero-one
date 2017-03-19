package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.goal.JustReachGoal;
import org.dragberry.ozo.game.level.goal.MultiGoal;
import org.dragberry.ozo.game.level.settings.ReachMultiGoalLevelSettings;

/**
 * Created by maksim on 30.01.17.
 */

public class ReachMultiGoalLevel extends Level<ReachMultiGoalLevelSettings> {

    public ReachMultiGoalLevel() {}

    public ReachMultiGoalLevel(ReachMultiGoalLevelSettings settings) {
        super(settings);
    }

    @Override
    protected void addGoals(ReachMultiGoalLevelSettings settings) {
        addGoalToLose(new JustReachGoal(settings.goalToLose, JustReachGoal.Operator.LESS));
        addGoalToWin(new MultiGoal(settings.goals));
    }


}
