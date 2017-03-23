package org.dragberry.ozo.game.level.generator;

import java.io.Serializable;

public abstract class Generator implements Serializable {

	public String id;

	protected int x;

	protected int y;

	public Generator() {}

	public Generator(int x, int y) {
		this.id = getId(x, y);
		this.x = x;
		this.y = y;
	}

	public static final String getId(int x, int y) {
		return x + "_" + y;
	}
	
	public abstract int next(int step, int selectedX, int selectedY);

	public void reset() {
		// empty by default
	}
}
