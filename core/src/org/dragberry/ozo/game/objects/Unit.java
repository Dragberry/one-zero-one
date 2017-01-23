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

	private TextureRegion regBall;
	
	private final int gameWidth;
	private final int gameHeight;
	
	private int gameX;
	private int gameY;
	private int value;
	
	public Unit() {
		this(0, 0, 0, 16, 9);
	}
	
	public Unit(int value, int x, int y, int gameWidth, int gameHeight) {
		this.value = value;
		this.gameWidth = gameWidth;
		this.gameHeight = gameHeight;
		this.gameX = x;
		this.gameY = y;
		this.position = new Vector2(x * Constants.VIEWPORT_WIDTH / gameWidth, y * Constants.VIEWPORT_HEIGHT / gameHeight);
		this.dimension = new Vector2(Constants.VIEWPORT_WIDTH / gameWidth, Constants.VIEWPORT_HEIGHT / gameHeight);
		init();
	}
	
	@Override
	protected void init() {
		regBall = Assets.instance.unit.ball;
		origin.x = dimension.x / 2;
		origin.y = dimension.y / 2;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		Sign sign = value < 0 ? Sign.MINUS : value == 0 ? Sign.ZERO : Sign.PLUS;
		batch.setColor(sign.color);
		batch.draw(regBall.getTexture(),
				position.x, position.y,
				origin.x, origin.y,
				dimension.x, dimension.y,
				scale.x, scale.y,
				rotation,
				regBall.getRegionX(), regBall.getRegionY(),
				regBall.getRegionWidth(), regBall.getRegionHeight(),
				false, false);
		
		BitmapFont font = Assets.instance.fonts.normal;
		
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
