package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.generator.Generator;
import org.dragberry.ozo.game.level.generator.QueueGeneratorHelper;
import org.dragberry.ozo.game.level.settings.NoAnnihilationLevelSettings;

public class NoAnnihilationQueueLevel extends NoAnnihilationLevel {

	public NoAnnihilationQueueLevel() {}

	public NoAnnihilationQueueLevel(NoAnnihilationLevelSettings settings) {
		super(settings);
	}
	
	@Override
	protected void createGenerators() {
		generators = QueueGeneratorHelper.createGenerators(width, height);
	}
	
	@Override
	protected Generator getDefaultGenerator(int x, int y) {
		return QueueGeneratorHelper.getDefaultGenerator(x, y);
	}
}
