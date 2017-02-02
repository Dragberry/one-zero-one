package org.dragberry.ozo.game.level.generator;

public class QueueGenerator extends Generator {
	
	private int value;

	public QueueGenerator(int initialValue, int x, int y) {
		super(x, y);
		this.value = initialValue;
	}

	@Override
	public int next() {
		int current = value;
		if (++value > 1) {
			value = -1;
		}
		return current;
	}

}
