package org.dragberry.ozo.game.objects;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.util.Constants;
import org.dragberry.ozo.game.util.DigitUtil;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public abstract class AbstractUnit extends AbstractGameObject {

	protected TextureRegion regBall;
	protected int value;
	protected boolean flipY;
	protected Array<TextureRegion> valueDigits = new Array<TextureRegion>(4);
	
	public AbstractUnit(int value) {
		this.value = value;
		DigitUtil.resolveDigits(value, valueDigits);
	}
	
	public void addValue(int valueToAdd) {
		value += valueToAdd;
		DigitUtil.resolveDigits(value, valueDigits);
	}
	
	public void setValue(int value) {
		this.value = value;
		DigitUtil.resolveDigits(value, valueDigits);
	}
	
	public int getValue() {
		return value;
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
				scale.x, scale.y,
				rotation,
				regBall.getRegionX(), regBall.getRegionY(),
				regBall.getRegionWidth(), regBall.getRegionHeight(),
				false, false);
		
		DigitUtil.draw(batch, valueDigits,
				position.x + dimension.x / 2, position.y + dimension.y / 2,
				scale.x, scale.y,
				rotation,
				false, flipY);
		
	}
	
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
