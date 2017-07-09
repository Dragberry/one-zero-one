package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.goal.AbstractGoal;
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
		addGoalToLose(new AnnihilationCounterGoal(settings.goalToLose, lostNumbers));
	}

	@Override
	protected void processGoalPulsation(MinAndMax minAndMax) {
		AnnihilationCounterGoal acGoal = (AnnihilationCounterGoal) goalsToLose.get(0);
		acGoal.markAsAlmostReached(acGoal.isAlmostReached(3));

		for (AbstractGoal goal : goalsToWin) {
			goal.markAsAlmostReached(goal.isAlmostReached(minAndMax.max));
		}
	}
}


