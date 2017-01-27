package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.objects.Unit;

public abstract class Level {
	
	public final int width;
	public final int height;
	
	public float time = 0;
	public int steps = 0;
	
	public Level(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public abstract boolean isLost(Unit[][] units);
	
	public abstract boolean isWon(Unit[][] units);

}
