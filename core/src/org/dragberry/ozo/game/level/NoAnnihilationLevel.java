package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.goal.AnnihilationCounterGoal;
import org.dragberry.ozo.game.level.goal.JustReachGoal;
import org.dragberry.ozo.game.level.goal.JustReachGoal.Operator;

public class NoAnnihilationLevel extends Level {
	
	public NoAnnihilationLevel(Integer goalToLose, Integer goal) {
		addGoalToWin(new JustReachGoal(goal.intValue(), Operator.EQUALS));
		addGoalToLose(new AnnihilationCounterGoal(goalToLose.intValue()));
	}

}
