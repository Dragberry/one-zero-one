package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.generator.ConstGenerator;
import org.dragberry.ozo.game.level.generator.Generator;
import org.dragberry.ozo.game.level.generator.WavesGenerator;
import org.dragberry.ozo.game.level.goal.AnnihilationCounterGoal;
import org.dragberry.ozo.game.level.goal.JustReachGoal;
import org.dragberry.ozo.game.level.settings.NoAnnihilationLevelSettings;

public class NoAnnihilationWavesLevel extends NoAnnihilationLevel {

	public NoAnnihilationWavesLevel() {}

	private WavesGenerator generator = new WavesGenerator(-1, -1, width, height);

	public NoAnnihilationWavesLevel(NoAnnihilationLevelSettings settings) {
		super(settings);
	}

	@Override
	protected void addGoals(NoAnnihilationLevelSettings settings) {
		super.addGoals(settings);
		addGoalToLose(new JustReachGoal(-99, JustReachGoal.Operator.LESS));
	}

	@Override
	protected Generator getDefaultGenerator(int x, int y) {
		return steps == 0 ? ConstGenerator.ZERO : generator;
	}

	@Override
	public void reset(boolean cleanState) {
		super.reset(cleanState);
		generator.reset(width, height);
	}

	@Override
	protected void processGoalPulsation(MinAndMax minAndMax) {
		super.processGoalPulsation(minAndMax);
		AnnihilationCounterGoal goal = (AnnihilationCounterGoal) goalsToLose.get(0);
		JustReachGoal negGoal = (JustReachGoal) goalsToLose.get(1);
		JustReachGoal posGoal = (JustReachGoal) goalsToWin.get(0);
		goal.markAsAlmostReached(goal.isAlmostReached(3));
		boolean flag = negGoal.isAlmostReached(minAndMax.min);
		negGoal.markAsAlmostReached(flag);
		minAndMax.minEnabled = flag;
		posGoal.markAsAlmostReached(posGoal.isAlmostReached(minAndMax.max));
	}
}
