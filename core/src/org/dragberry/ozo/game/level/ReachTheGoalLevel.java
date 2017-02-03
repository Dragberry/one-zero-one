package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.goal.JustReachGoal;
import org.dragberry.ozo.screen.LevelInfo;

/**
 * Created by maksim on 30.01.17.
 */

public class ReachTheGoalLevel extends Level<ReachTheGoalLevel.ReachTheGoalLevelInfo> {

    public ReachTheGoalLevel(ReachTheGoalLevel.ReachTheGoalLevelInfo levelInfo) {
    	super(levelInfo.name);
    	addGoalToWin(new JustReachGoal(levelInfo.goal, levelInfo.operator));
        addGoalToLose(new JustReachGoal(levelInfo.goalToLose, JustReachGoal.Operator.LESS));
    }

    public static class ReachTheGoalLevelInfo extends LevelInfo {
    	
    	public final int goalToLose;
    	public final int goal;
    	public final JustReachGoal.Operator operator;

    	public ReachTheGoalLevelInfo(String name, int goalToLose, int goal) {
			this(name, goalToLose, goal, JustReachGoal.Operator.EQUALS);
		}
    	
		public ReachTheGoalLevelInfo(String name, int goalToLose, int goal, JustReachGoal.Operator operator) {
			super(ReachTheGoalLevel.class, name);
			this.goalToLose = goalToLose;
			this.goal = goal;
			this.operator = operator;
		}
    	
    }
    
	@Override
	public LevelInfo getLevelInfo() {
		return null;
	}
}
