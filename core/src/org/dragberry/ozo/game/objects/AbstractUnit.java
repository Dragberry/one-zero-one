package org.dragberry.ozo.game.objects;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.util.Constants;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class AbstractUnit extends AbstractGameObject {

	protected TextureRegion regBall;
	public int value;
	
	public AbstractUnit(int value) {
		this.value = value;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		Sign sign = value < 0 ? Sign.MINUS : value == 0 ? Sign.ZERO : Sign.PLUS;
		batch.setColor(sign.color);
		regBall = Assets.instance.unit.ball;

		batch.draw(regBall.getTexture(),
				position.x, position.y,
				origin.x, origin.y,
				dimension.x, dimension.y,
				getScaleX(), getScaleY(),
				rotation,
				regBall.getRegionX(), regBall.getRegionY(),
				regBall.getRegionWidth(), regBall.getRegionHeight(),
				false, false);
		
		BitmapFont font = getFont();
		String valueStr = sign.sign + Math.abs(value);
		GlyphLayout layout = new GlyphLayout(font, valueStr);
		font.setColor(Color.BLACK);
		font.draw(batch, layout,getFontX(layout), getFontY(layout));
	}
	
	protected abstract BitmapFont getFont();
	
	protected abstract float getFontX(GlyphLayout layout);
	
	protected abstract float getFontY(GlyphLayout layout);
	
	protected abstract float getScaleX();
	
	protected abstract float getScaleY();

	protected enum Sign {
		MINUS(Constants.NEGATIVE, "-"),
		ZERO(Constants.NEUTRAL, ""),
		PLUS(Constants.POSITIVE, "+");
		
		public Color color;
		public String sign;
		
		Sign(Color color, String sign) {
			this.color = color;
			this.sign = sign;
		}
	}
}
