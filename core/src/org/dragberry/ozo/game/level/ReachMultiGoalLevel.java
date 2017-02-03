package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.goal.JustReachGoal;
import org.dragberry.ozo.game.level.goal.MultiGoal;
import org.dragberry.ozo.screen.LevelInfo;

/**
 * Created by maksim on 30.01.17.
 */

public class ReachMultiGoalLevel extends Level<ReachMultiGoalLevel.ReachMultiGoalLevelInfo> {

    public ReachMultiGoalLevel(ReachMultiGoalLevel.ReachMultiGoalLevelInfo levelInfo) {
        super(levelInfo.name);
    	addGoalToLose(new JustReachGoal(levelInfo.goalToLose, JustReachGoal.Operator.LESS));
        addGoalToWin(new MultiGoal(levelInfo.goals));
    }

    public static class ReachMultiGoalLevelInfo extends LevelInfo {
    	
    	public final int goalToLose;
    	public final int[] goals;

		public ReachMultiGoalLevelInfo(String name, int goalToLose, int... goals) {
			super(ReachTheGoalLevel.class, name);
			this.goalToLose = goalToLose;
			this.goals = goals;
		}
    	
    }

	@Override
	public LevelInfo getLevelInfo() {
		return null;
	}
}
