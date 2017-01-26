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

	private static final float UINIT_SELECTED_SCALE = 1.2f;

	private TextureRegion regBall;
	
	public int gameX;
	public int gameY;
	public int value;
	
	public boolean selected;
	
	public Unit() {
		this(0, 0, 0);
	}
	
	public Unit(int value, int x, int y) {
		this.value = value;
		this.dimension = new Vector2(Constants.UNIT_SIZE, Constants.UNIT_SIZE);
		this.gameX = x;
		this.gameY = y;
		init();
	}
	
	@Override
	protected void init() {
		regBall = Assets.instance.unit.ball;
		position = new Vector2(gameX * Constants.UNIT_SIZE, gameY * Constants.UNIT_SIZE);
		origin.x = dimension.x / 2;
		origin.y = dimension.y / 2;
		bounds.set(position.x, position.y, dimension.x, dimension.y);
	}
	
	public void moveTo(int gameX, int gameY) {
		this.gameX = gameX;
		this.gameY = gameY;
		init();
	}
	
	@Override
	public void render(SpriteBatch batch) {
		Sign sign = value < 0 ? Sign.MINUS : value == 0 ? Sign.ZERO : Sign.PLUS;
		batch.setColor(sign.color);
		batch.draw(regBall.getTexture(),
				position.x, position.y,
				origin.x, origin.y,
				dimension.x, dimension.y,
				selected ? scale.x * UINIT_SELECTED_SCALE : scale.x,
				selected ? scale.y * UINIT_SELECTED_SCALE : scale.y,
				rotation,
				regBall.getRegionX(), regBall.getRegionY(),
				regBall.getRegionWidth(), regBall.getRegionHeight(),
				false, false);
		
		BitmapFont font = Assets.instance.fonts._29;
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

}
