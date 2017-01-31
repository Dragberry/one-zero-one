package org.dragberry.ozo.game.objects;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.util.Constants;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Unit extends AbstractGameObject {

	public enum Direction {
		NORTH, SOUTH, EAST, WEST
	}
	
	private static final float UINIT_SELECTED_SCALE = 0.8f;

	private TextureRegion regBall;
	
	public int x;
	public int y;
	public int value;
	public int previousValue;
	
	public boolean selected;
	public boolean selectedNeighbor;
	
	public Unit() {
		this(0, 0, 0);
	}
	
	public Unit(int value, int x, int y) {
		this.value = value;
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
	public void render(SpriteBatch batch) {
		Sign sign = value < 0 ? Sign.MINUS : value == 0 ? Sign.ZERO : Sign.PLUS;
		batch.setColor(sign.color);
		switch (sign) {
			case MINUS:
				regBall = Assets.instance.unit.redBall;
				break;
			case ZERO:
				regBall = Assets.instance.unit.blueBall;
				break;
			case PLUS:
				regBall = Assets.instance.unit.greenBall;
				break;
		}
		batch.draw(regBall.getTexture(),
				position.x, position.y,
				origin.x, origin.y,
				dimension.x, dimension.y,
				!(selected || selectedNeighbor) ? scale.x * UINIT_SELECTED_SCALE : scale.x,
				!(selected || selectedNeighbor) ? scale.y * UINIT_SELECTED_SCALE : scale.y,
				rotation,
				regBall.getRegionX(), regBall.getRegionY(),
				regBall.getRegionWidth(), regBall.getRegionHeight(),
				false, false);
		
		BitmapFont font = selected || selectedNeighbor ? 
				Assets.instance.fonts.game_68 : Assets.instance.fonts.game_58;
		String valueStr = sign.sign + Math.abs(value);
		GlyphLayout layout = new GlyphLayout(font, valueStr);
		font.setColor(Color.BLACK);
		font.draw(batch, 
				layout,
				position.x + (dimension.x - layout.width) * 0.375f,
				position.y + dimension.y / 2 + layout.height / 2);
	}
	
	private enum Sign {
		MINUS(Color.RED, "-"), ZERO(Color.BLUE, ""), PLUS(Color.GREEN, "+");
		
		public Color color;
		public String sign;
		
		Sign(Color color, String sign) {
			this.color = color;
			this.sign = sign;
		}
	}

	@Override
	public String toString() {
		return "Unit[" + x + "][" + y + "]=" + value;
	}
}
