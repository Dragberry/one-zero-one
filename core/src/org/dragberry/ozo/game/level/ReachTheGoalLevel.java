package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.goal.JustReachGoal;
import org.dragberry.ozo.game.level.settings.ReachTheGoalLevelSettings;

/**
 * Created by maksim on 30.01.17.
 */

public class ReachTheGoalLevel extends Level<ReachTheGoalLevelSettings> {

    public ReachTheGoalLevel() {}

    public ReachTheGoalLevel(ReachTheGoalLevelSettings settings) {
    	super(settings);
    }

    @Override
    protected void addGoals(ReachTheGoalLevelSettings settings) {
        addGoalToWin(new JustReachGoal(settings.goal, settings.operator));
        addGoalToLose(new JustReachGoal(settings.goalToLose, JustReachGoal.Operator.LESS));
    }
}
