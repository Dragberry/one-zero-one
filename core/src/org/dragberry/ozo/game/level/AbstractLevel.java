package org.dragberry.ozo.game.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

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

	public int generateValue() {
		return MathUtils.random(-1, 1);
	}

	public abstract boolean isLost(Unit[][] units, Unit selectedUnit, Unit[] neighbors);
	
	public abstract boolean isWon(Unit[][] units, Unit selectedUnit, Unit[] neighbors);
	
	public abstract String getWinConditionMsg();
	
	public abstract String getLoseConditionMsg();

	public abstract String getName();

	@Override
	protected void finalize() throws Throwable {
		Gdx.app.debug(getName(), " has been collected");
	}

}
