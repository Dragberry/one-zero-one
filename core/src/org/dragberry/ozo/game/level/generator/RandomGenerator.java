package org.dragberry.ozo.game.level.generator;

import com.badlogic.gdx.math.MathUtils;

public class RandomGenerator extends Generator {
	
	public static final RandomGenerator DEFAUTL = new RandomGenerator(-1, -1);

	private int total;

	public RandomGenerator(int x, int y) {
		super(x, y);
	}

	public RandomGenerator adjust(int total) {
		this.total = total;
		return this;
	}

	@Override
	public int next(int step, int selectedX, int selectedY) {
		return MathUtils.random(total < -5 ? 0 : -1, total > 5 ? 0 : 1);
	}

}
