package org.dragberry.ozo.game.objects;

import org.dragberry.ozo.game.util.Constants;

public class GoalUnit extends AbstractUnit {
	
	private static final float SCALE = 0.8f;
	public static final float SIZE = Constants.UNIT_SIZE;
	
	public GoalUnit(int value) {
		super(value);
		flipY = true;
		dimension.set(SIZE, SIZE);
		origin.set(dimension.x / 2, dimension.y / 2);
		scale.set(SCALE, 0.8f);
	}
	
}
