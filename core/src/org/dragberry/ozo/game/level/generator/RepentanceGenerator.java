package org.dragberry.ozo.game.level.generator;

import com.badlogic.gdx.math.MathUtils;

/**
 * Generator of -1,0,1,0,...
 * @author Maksim Drahun
 *
 */
public class RepentanceGenerator extends Generator {

	public static class PositivePosition {

		private int position = -1;
		private boolean enabled = false;

		private boolean isReady() {
			if (enabled && --position == 0) {
				enabled = false;
				return true;
			}
			return false;
		}

		public void updatePosition() {
			enabled = true;
			position = MathUtils.random(1, 4);
		}
	}

	private int initialValue;
	private int value;

	private PositivePosition positivePosition;

	public RepentanceGenerator() {}

	public RepentanceGenerator(int initialValue, int x, int y, PositivePosition positivePosition) {
		super(x, y);
		this.initialValue = initialValue;
		this.value = initialValue;
		this.positivePosition = positivePosition;
	}

	@Override
	public int next(int step, int selectedX, int selectedY) {
		value = value == 0 ? -1 : 0;
		if (positivePosition.isReady()) {
			return 1;
		}
		return value;
	}

	@Override
	public void reset() {
		value = initialValue;
		positivePosition.position = -1;
		positivePosition.enabled = false;
	}

	public void setPositivePosition(PositivePosition positivePosition) {
		this.positivePosition = positivePosition;
	}
}
