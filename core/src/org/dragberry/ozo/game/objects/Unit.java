package org.dragberry.ozo.game.objects;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.util.Constants;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;

public class Unit extends AbstractUnit {

	public enum Direction {
		NORTH, SOUTH, EAST, WEST
	}
	
	private static final float UINIT_SELECTED_SCALE = 0.8f;

	public int x;
	public int y;
	public int previousValue;
	
	public boolean selected;
	public boolean selectedNeighbor;
	
	public Unit() {
		this(0, 0, 0);
	}
	
	public Unit(int value, int x, int y) {
		super(value);
		this.previousValue = value;
		this.dimension = new Vector2(Constants.UNIT_SIZE, Constants.UNIT_SIZE);
		this.x = x;
		this.y = y;
		origin.x = dimension.x / 2;
		origin.y = dimension.y / 2;
		init();
	}
	
	@Override
	protected void init() {
		position = new Vector2(x * Constants.UNIT_SIZE, y * Constants.UNIT_SIZE);
		bounds.set(position.x, position.y, dimension.x, dimension.y);
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
		init();
	}
	
	@Override
	protected BitmapFont getFont() {
		return selected || selectedNeighbor ? Assets.instance.fonts.game_68 : Assets.instance.fonts.game_58;
	}
	
	@Override
	protected float getScaleX() {
		return selected || selectedNeighbor ? scale.x : scale.x * UINIT_SELECTED_SCALE;
	}
	
	@Override
	protected float getScaleY() {
		return selected || selectedNeighbor ? scale.y : scale.y * UINIT_SELECTED_SCALE;
	}
	
	@Override
	protected float getFontX(GlyphLayout layout) {
		return position.x + (dimension.x - layout.width) * 0.4f;
	}

	@Override
	protected float getFontY(GlyphLayout layout) {
		return position.y + dimension.y / 2 + layout.height / 2;
	}
	
	@Override
	public String toString() {
		return "Unit[" + x + "][" + y + "]=" + value;
	}
}
