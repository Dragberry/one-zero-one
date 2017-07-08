package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.generator.ConstGenerator;
import org.dragberry.ozo.game.level.generator.Generator;
import org.dragberry.ozo.game.level.generator.WavesGenerator;
import org.dragberry.ozo.game.level.goal.AnnihilationCounterGoal;
import org.dragberry.ozo.game.level.goal.JustReachGoal;
import org.dragberry.ozo.game.level.settings.NoAnnihilationLevelSettings;
import org.dragberry.ozo.game.objects.Unit;

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
	protected void processPulsation() {
		int maxValue = Integer.MIN_VALUE;
		int minValue = Integer.MAX_VALUE;
		for (Unit[] row : units) {
			for (Unit unit : row) {
				if (unit.getValue() > 1 && maxValue < unit.getValue()) {
					maxValue = unit.getValue();
				}
				if (unit.getValue() < -1 && minValue > unit.getValue()) {
					minValue = unit.getValue();
				}
			}
		}

		processGoalPulsation(maxValue, minValue);

		for (Unit[] row : units) {
			for (Unit unit : row) {
				unit.isPulsated = maxValue == unit.getValue();
			}
		}
	}

	@Override
	protected void processGoalPulsation(int maxValue, int minValue) {
		super.processGoalPulsation(maxValue, minValue);
		AnnihilationCounterGoal goal = (AnnihilationCounterGoal) goalsToLose.get(0);
		JustReachGoal negGoal = (JustReachGoal) goalsToLose.get(1);
		JustReachGoal posGoal = (JustReachGoal) goalsToWin.get(0);
		goal.markAsAlmostReached(goal.isAlmostReached(3));
		negGoal.markAsAlmostReached(negGoal.isAlmostReached(minValue));
		posGoal.markAsAlmostReached(posGoal.isAlmostReached(maxValue));
	}
}
