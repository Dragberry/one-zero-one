package org.dragberry.ozo.game.objects;

import org.dragberry.ozo.game.Assets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Unit extends AbstractGameObject {
	
	private TextureRegion regBall;
	
	private int value;
	
	private int x;
	private int y;
	
	private boolean north;
	private boolean south;
	private boolean east;
	private boolean west;
	
	public Unit() {
		this(0, 0);
	}

	public Unit(int x, int y) {
		this(0, x, y, true, true, true, true);
	}

	public Unit(int value, int x, int y, boolean north, boolean south, boolean east, boolean west) {
		this.value = value;
		this.north = north;
		this.south = south;
		this.east = east;
		this.west = west;
		init();
	}
	
	@Override
	protected void init() {
		regBall = Assets.instance.unit.ball;
		
	}
	
	@Override
	public void render(SpriteBatch batch) {
		batch.draw(regBall.getTexture(),
				position.x + origin.x, position.y + origin.y,
				origin.x, origin.y,
				dimension.x, dimension.y,
				scale.x, scale.y,
				rotation,
				regBall.getRegionX(), regBall.getRegionY(),
				regBall.getRegionWidth(), regBall.getRegionHeight(),
				false, false);
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public void addValue(int value) {
		this.value += value;
	}
	
	public void addValues(Unit...units) {
		for (int i = 0; i < units.length; i++) {
			this.value += units[i].value;
		}
	}
	
	public void moveTo(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getValue() {
		return value;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isNorth() {
		return north;
	}

	public boolean isSouth() {
		return south;
	}

	public boolean isEast() {
		return east;
	}

	public boolean isWest() {
		return west;
	}
}
