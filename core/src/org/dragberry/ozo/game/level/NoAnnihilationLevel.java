package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.goal.AnnihilationCounterGoal;
import org.dragberry.ozo.game.level.goal.JustReachGoal;
import org.dragberry.ozo.game.level.goal.JustReachGoal.Operator;
import org.dragberry.ozo.game.level.settings.NoAnnihilationLevelSettings;

public class NoAnnihilationLevel extends Level<NoAnnihilationLevelSettings> {

	public NoAnnihilationLevel(NoAnnihilationLevelSettings levelInfo) {
		super(levelInfo);
		addGoalToWin(new JustReachGoal(levelInfo.goal, Operator.EQUALS));
		addGoalToLose(new AnnihilationCounterGoal(levelInfo.goalToLose));
	}

}
