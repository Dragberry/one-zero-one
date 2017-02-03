package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.goal.AnnihilationCounterGoal;
import org.dragberry.ozo.game.level.goal.JustReachGoal;
import org.dragberry.ozo.game.level.goal.JustReachGoal.Operator;
import org.dragberry.ozo.screen.LevelInfo;

public class NoAnnihilationLevel extends Level<NoAnnihilationLevel.NoAnnihilationLevelInfo> {
	
	public NoAnnihilationLevel(NoAnnihilationLevel.NoAnnihilationLevelInfo levelInfo) {
		super(levelInfo);
		addGoalToWin(new JustReachGoal(levelInfo.goal, Operator.EQUALS));
		addGoalToLose(new AnnihilationCounterGoal(levelInfo.goalToLose));
	}
	
	 public static class NoAnnihilationLevelInfo extends LevelInfo {
	    	
    	public final int goalToLose;
    	public final int goal;

    	public NoAnnihilationLevelInfo(String name, int goalToLose, int goal) {
    		super(ReachTheGoalLevel.class, name);
			this.goalToLose = goalToLose;
			this.goal = goal;
		}
    	
    }

}
