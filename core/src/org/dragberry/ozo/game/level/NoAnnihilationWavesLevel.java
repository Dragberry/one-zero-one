package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.generator.Generator;
import org.dragberry.ozo.game.level.generator.WavesGeneratorHelper;
import org.dragberry.ozo.game.level.goal.JustReachGoal;
import org.dragberry.ozo.game.level.settings.NoAnnihilationLevelSettings;

public class NoAnnihilationWavesLevel extends NoAnnihilationLevel {

	public NoAnnihilationWavesLevel(NoAnnihilationLevelSettings levelInfo) {
		super(levelInfo);
		addGoalToLose(new JustReachGoal(-99, JustReachGoal.Operator.LESS));
	}
	

	@Override
	protected Generator getDefaultGenerator(int x, int y) {
		return WavesGeneratorHelper.getGenerator(x, y, width - 1, height - 1, steps);
	}
}
