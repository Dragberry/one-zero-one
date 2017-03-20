package org.dragberry.ozo.game.objects;

import org.dragberry.ozo.game.util.Constants;
import org.dragberry.ozo.game.util.DigitUtil;

import com.badlogic.gdx.math.MathUtils;

public class Unit extends AbstractUnit {

	public enum Direction {
		NORTH, SOUTH, EAST, WEST
	}

	private static final float UNIT_INITIAL_SCALE = 0.01f;
	private static final float UNIT_UNSELECTED_SCALE = 0.8f;
	private static final float UNIT_SELECTED_SCALE = 1.0f;

	public int x;
	public int y;

	public transient int previousValue;

	private transient boolean selected;
	private transient boolean selectedNeighbor;

	public enum State {
		FIXED, GROW_UP, GROW_DOWN, INITIAL
	}
	private transient State state;
	private transient float time;
	private transient static final float GROWING_TIME = 0.2f;

	private transient boolean isFluctuated;
	private transient float fluctuationsTime;
	private transient static final float FLUCTUATIONS_TIME = 0.7f;

	public Unit init(int value, int x, int y) {
		this.x = x;
		this.y = y;
		this.value = value;
		return init();
	}

	public Unit init() {
		DigitUtil.resolveDigits(value, valueDigits);
		dimension.set(Constants.UNIT_SIZE, Constants.UNIT_SIZE);
		position.set(x * Constants.UNIT_SIZE, y * Constants.UNIT_SIZE);
		bounds.set(position.x, position.y, dimension.x, dimension.y);
		origin.set(dimension.x / 2, dimension.y / 2);
		scale.set(UNIT_INITIAL_SCALE, UNIT_INITIAL_SCALE);
		state = State.INITIAL;
		flipY = false;
		time = 0;
		selected = false;
		selectedNeighbor = false;
		return this;
	}

	@Override
	public void update(float deltaTime) {
		if (isFluctuated) {
			fluctuationsTime += deltaTime;
			if (fluctuationsTime < FLUCTUATIONS_TIME) {
				rotation = 7 / Math.max(fluctuationsTime * 7, 1) * (MathUtils.sin(50 * fluctuationsTime));
			} else {
				isFluctuated = false;
				fluctuationsTime = 0;
				rotation = 0;
			}
		}
		switch (state) {
			case INITIAL:
				time += deltaTime;
				scale.x += deltaTime * 2;
				scale.y += deltaTime * 2;
				if (scale.x >= UNIT_UNSELECTED_SCALE) {
					time = 0;
					state = State.FIXED;
					scale.x = UNIT_UNSELECTED_SCALE;
					scale.y = UNIT_UNSELECTED_SCALE;
				}
				break;
			case GROW_UP:
				time += deltaTime;
				scale.x += deltaTime;
				scale.y += deltaTime;
				if (time >= GROWING_TIME || scale.x >= UNIT_SELECTED_SCALE) {
					time = 0;
					state = State.FIXED;
					scale.x = UNIT_SELECTED_SCALE;
					scale.y = UNIT_SELECTED_SCALE;
				}
				break;
			case GROW_DOWN:
				time += deltaTime;
				scale.x -= deltaTime;
				scale.y -= deltaTime;
				if (time >= GROWING_TIME || scale.x <= UNIT_UNSELECTED_SCALE) {
					time = 0;
					state = State.FIXED;
					scale.x = UNIT_UNSELECTED_SCALE;
					scale.y = UNIT_UNSELECTED_SCALE;
				}
				break;
			default:
				break;
		}
	}

	public void moveTo(Direction direction, float step) {
		float border;
		switch (direction) {
		case SOUTH:
			border = (y - 1) * Constants.UNIT_SIZE;
			position.y -= step;
			if (position.y < border) {
				position.y = border;
			}
			break;
		case NORTH:
			border = (y + 1) * Constants.UNIT_SIZE;
			position.y += step;
			if (position.y > border) {
				position.y = border;
			}
			break;
		case WEST:
			border = (x - 1) * Constants.UNIT_SIZE;
			position.x -= step;
			if (position.x < border) {
				position.x = border;
			}
			break;
		case EAST:
			border = (x + 1) * Constants.UNIT_SIZE;
			position.x += step;
			if (position.x > border) {
				position.x = border;
			}
			break;
		default:
			throw new IllegalArgumentException();
		}
	}

	public void moveTo(int gameX, int gameY) {
		this.x = gameX;
		this.y = gameY;
		position.set(x * Constants.UNIT_SIZE, y * Constants.UNIT_SIZE);
		bounds.set(position.x, position.y, dimension.x, dimension.y);
	}

	@Override
	public String toString() {
		return "Unit[" + x + "][" + y + "]=" + value;
	}

	public boolean isSelected() {
		return selected;
	}

	public void select() {
		selected = true;
		state = State.GROW_UP;
		time = 0;
	}

	public void unselect() {
		selected = false;
		state = State.GROW_DOWN;
		time = 0;
	}

	public boolean isSelectedNeighbor() {
		return selectedNeighbor;
	}

	public void selectNeighbor() {
		selectedNeighbor = true;
		state = State.GROW_UP;
		time = 0;

	}

	public void unselectNeighbor() {
		selectedNeighbor = false;
		state = State.GROW_DOWN;
		time = 0;
	}

	public void triggerSelectionEffect() {
		isFluctuated = true;
		fluctuationsTime = 0;
	}

}
