package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.goal.AnnihilationCounterGoal;
import org.dragberry.ozo.game.level.goal.JustReachGoal;
import org.dragberry.ozo.game.level.goal.JustReachGoal.Operator;
import org.dragberry.ozo.game.level.settings.NoAnnihilationLevelSettings;

public class NoAnnihilationLevel extends Level<NoAnnihilationLevelSettings> {

	public NoAnnihilationLevel() {}

	public NoAnnihilationLevel(NoAnnihilationLevelSettings settings) {
		super(settings);
	}

	@Override
	protected void addGoals(NoAnnihilationLevelSettings settings) {
		addGoalToWin(new JustReachGoal(settings.goal, Operator.EQUALS));
		addGoalToLose(new AnnihilationCounterGoal(settings.goalToLose - lostNumbers));
	}

}
