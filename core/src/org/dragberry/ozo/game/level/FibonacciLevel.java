package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.goal.FibonacciTipGoal;
import org.dragberry.ozo.game.level.settings.ReachTheGoalLevelSettings;

public class FibonacciLevel extends SequenceOf2Level {

	private int previousSequenceValue = 0;
	private transient FibonacciTipGoal tip;

	public FibonacciLevel() {}

	public FibonacciLevel(ReachTheGoalLevelSettings settings) {
		super(settings);
	}

	@Override
	protected void addGoals(ReachTheGoalLevelSettings settings) {
		super.addGoals(settings);
		tip = new FibonacciTipGoal();
		addGoalToWin(tip);
	}

	@Override
	protected void updateSequence() {
		int prev = previousSequenceValue;
		previousSequenceValue = sequenceValue;
		sequenceValue += prev;
		tip.updateSequence(getSequence());
	}

	@Override
	public void reset(boolean restore) {
		super.reset(restore);
		if (!restore) {
			previousSequenceValue = 0;
			tip.updateSequence(null);
		} else {
			tip.updateSequence(getSequence());
		}
	}

	private String getSequence() {
		StringBuilder seq = new StringBuilder();
		int prev = 0;
		int value = -1;
		while (prev != previousSequenceValue || value != sequenceValue) {
			if (prev != 0) {
				seq.append(", ");
			}
			seq.append(-value);
			int temp = prev;
			prev = value;
			value += temp;
		}
		return seq.toString();
	}
}
