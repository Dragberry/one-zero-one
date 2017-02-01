package org.dragberry.ozo.game.level.generator;

import com.badlogic.gdx.math.MathUtils;

public class RandomGenerator extends Generator {
	
	public static final RandomGenerator DEFAUTL = new RandomGenerator(-1, -1);

	public RandomGenerator(int x, int y) {
		super(x, y);
	}

	@Override
	public int next() {
		return MathUtils.random(-1, 1);
	}

}
