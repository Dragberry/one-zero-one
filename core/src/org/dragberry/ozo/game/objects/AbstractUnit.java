package org.dragberry.ozo.game.objects;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.util.Constants;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractUnit extends AbstractGameObject {

	protected TextureRegion regBall;
	public int value;
	
	public AbstractUnit(int value) {
		this.value = value;
		this.dimension = new Vector2(Constants.UNIT_SIZE, Constants.UNIT_SIZE);
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
				getScaleX(), getScaleY(),
				rotation,
				regBall.getRegionX(), regBall.getRegionY(),
				regBall.getRegionWidth(), regBall.getRegionHeight(),
				false, false);
		BitmapFont font = getFont();
		String valueStr = sign.sign + Math.abs(value);
		GlyphLayout layout = new GlyphLayout(font, valueStr);
		font.setColor(Color.BLACK);
		font.draw(batch, 
				layout,
				(position.x + (dimension.x - layout.width) * 0.375f) * getScaleX(),
				(position.y + dimension.y / 2 + layout.height / 2) * getScaleX());
	}
	
	protected abstract BitmapFont getFont();
	
	protected abstract float getScaleX();
	
	protected abstract float getScaleY();

	protected enum Sign {
		MINUS(Color.RED, "-"), ZERO(Color.BLUE, ""), PLUS(Color.GREEN, "+");
		
		public Color color;
		public String sign;
		
		Sign(Color color, String sign) {
			this.color = color;
			this.sign = sign;
		}
	}
}
