package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.settings.SequenceReachTheGoalLevelSettings;

public class FibonacciLevel extends SequenceOf2Level {

	private int previousSequenceValue = 0;

	public FibonacciLevel() {}

	public FibonacciLevel(SequenceReachTheGoalLevelSettings settings) {
		super(settings);
	}

	@Override
	protected void updateSequenceValue() {
		int prev = previousSequenceValue;
		previousSequenceValue = sequenceValue;
		sequenceValue += prev;
	}

	@Override
	public void reset(boolean restore) {
		if (!restore) {
			previousSequenceValue = 0;
		}
		super.reset(restore);
	}

	@Override
	protected String getSequence() {
		StringBuilder seq = new StringBuilder();
		int prev = sequenceValue - previousSequenceValue;
		int value = previousSequenceValue;
		int counter = 0;
		while (counter < 5 && value < 0) {
			if (counter != 0) {
				seq.append(DELIMITER);
			}
			seq.append(-value);
			prev = value - prev;
			value = value - prev;
			counter++;
		}
		return seq.toString();
	}
}
