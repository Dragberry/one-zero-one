package org.dragberry.ozo.game.level.generator;

public class QueueGenerator extends Generator {

	private final int initialValue;
	private int value;

	public QueueGenerator(int initialValue, int x, int y) {
		super(x, y);
		this.initialValue = initialValue;
		this.value = initialValue;
	}

	@Override
	public int next(int step, int selectedX, int selectedY) {
		int current = value;
		if (++value > 1) {
			value = -1;
		}
		return current;
	}

	@Override
	public void reset() {
		value = initialValue;
	}
}
