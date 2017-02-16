package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.generator.ConstGenerator;
import org.dragberry.ozo.game.level.generator.Generator;
import org.dragberry.ozo.game.level.generator.WavesGenerator;
import org.dragberry.ozo.game.level.goal.JustReachGoal;
import org.dragberry.ozo.game.level.settings.NoAnnihilationLevelSettings;

public class NoAnnihilationWavesLevel extends NoAnnihilationLevel {

	private WavesGenerator generator = new WavesGenerator(-1, -1, width, height);

	public NoAnnihilationWavesLevel(NoAnnihilationLevelSettings levelInfo) {
		super(levelInfo);
		addGoalToLose(new JustReachGoal(-99, JustReachGoal.Operator.LESS));
	}
	

	@Override
	protected Generator getDefaultGenerator(int x, int y) {
		return steps == 0 ? ConstGenerator.ZERO : generator;
	}

	@Override
	public void reset() {
		super.reset();
		generator.reset(width, height);
	}
}
