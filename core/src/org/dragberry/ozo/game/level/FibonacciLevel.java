package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.settings.ReachTheGoalLevelSettings;

public class FibonacciLevel extends SequenceOf2Level {

	private int previousSequenceValue = 0;

	public FibonacciLevel() {}

	public FibonacciLevel(ReachTheGoalLevelSettings settings) {
		super(settings);
	}

	@Override
	protected void updateSequence() {
		int prev = previousSequenceValue;
		previousSequenceValue = sequenceValue;
		sequenceValue += prev;
	}

	@Override
	public void reset(boolean restore) {
		super.reset(restore);
		if (!restore) {
			previousSequenceValue = 0;
		}
	}
}
