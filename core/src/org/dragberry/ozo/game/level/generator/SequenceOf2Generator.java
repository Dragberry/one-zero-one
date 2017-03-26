package org.dragberry.ozo.game.level.generator;

import com.badlogic.gdx.math.MathUtils;

/**
 * Generator of -1,0,-1,0,...
 * @author Maksim Drahun
 *
 */
public abstract class SequenceOf2Generator extends Generator {

	public static class ThirdValueState {

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

	protected int initialValue;
	protected int value;

	private ThirdValueState thirdValueState;

	public SequenceOf2Generator() {}

	public SequenceOf2Generator(int initialValue, int x, int y, ThirdValueState thirdValueState) {
		super(x, y);
		this.initialValue = initialValue;
		this.value = initialValue;
		this.thirdValueState = thirdValueState;
	}

	@Override
	public int next(int step, int selectedX, int selectedY) {
		value = value == 0 ? -1 : 0;
		if (thirdValueState.isReady()) {
			return 1;
		}
		return value;
	}

	protected abstract int defaultValue();

	@Override
	public void reset() {
		value = initialValue;
	}

	public void setThirdValueState(ThirdValueState thirdValueState) {
		this.thirdValueState = thirdValueState;
	}
}
