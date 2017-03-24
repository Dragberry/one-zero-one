package org.dragberry.ozo.game.level.generator;

/**
 * Generator of -1,0,-1,0,...
 * @author Maksim Drahun
 *
 */
public class HighwayGenerator extends Generator {

	public static class PositiveState {

		private boolean vertical;
		private boolean horizontal;

		private int width;
		private int height;

		public PositiveState() {}

		public PositiveState(int width, int height) {
			this.width = width;
			this.height = height;
		}

		public void updateState(boolean vState, boolean hState) {
			vertical = vState;
			horizontal = hState;
		}
	}

	private int initialValue;
	private int value;

	private PositiveState positiveState;

	public HighwayGenerator() {}

	public HighwayGenerator(int initialValue, int x, int y, PositiveState positiveState) {
		super(x, y);
		this.initialValue = initialValue;
		this.value = initialValue;
		this.positiveState = positiveState;
	}

	@Override
	public int next(int step, int selectedX, int selectedY) {
		value = value == 0 ? -1 : 0;
		if (positiveState.vertical && selectedX == x) {
			return 1;
		}
		if (positiveState.horizontal && selectedY == y) {
			return 1;
		}
		return value;
	}

	@Override
	public void reset() {
		value = initialValue;
	}

	public void setPositiveState(PositiveState positiveState) {
		this.positiveState = positiveState;
	}
}
