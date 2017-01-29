package org.dragberry.ozo.game.level;

import com.badlogic.gdx.Gdx;

import org.dragberry.ozo.game.objects.Unit;

public abstract class AbstractLevel {

	protected final static int DEFAULT_WIDTH = 6;
	protected final static int DEFAULT_HEIGHT = 8;

	public final int width;
	public final int height;
	
	public float time = 0;
	public int steps = 0;

	public AbstractLevel() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public AbstractLevel(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public abstract boolean isLost(Unit[][] units);
	
	public abstract boolean isWon(Unit[][] units);
	
	public abstract String getWinConditionMsg();
	
	public abstract String getLoseConditionMsg();

	public abstract String getName();

	@Override
	protected void finalize() throws Throwable {
		Gdx.app.debug(getName(), " has been collected");
	}

}
