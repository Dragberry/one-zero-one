package org.dragberry.ozo.game.objects;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.util.Constants;

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
		resolveValueDigits();
	}
	
	public void addValue(int valueToAdd) {
		value += valueToAdd;
		resolveValueDigits();
	}
	
	public void setValue(int value) {
		this.value = value;
		resolveValueDigits();
	}
	
	public int getValue() {
		return value;
	}
	
	private void resolveValueDigits() {
		valueDigits.clear();
		resolveNextDigit(value);
		if (value < 0) {
			valueDigits.add(Assets.instance.digits.minus);
		}
		if (value > 0) {
			valueDigits.add(Assets.instance.digits.plus);
		}
		valueDigits.reverse();
	}
	
	private void resolveNextDigit(int value) {
		int digit = value % 10;
		valueDigits.add(Assets.instance.digits.digits[Math.abs(digit)]);
		value /= 10;
		if (value != 0) {
			resolveNextDigit(value);
		}
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
		
		int digitWidth = Assets.instance.digits.plus.getRegionWidth();
		int digitHeight= Assets.instance.digits.plus.getRegionHeight();
		float offsetX = digitWidth * valueDigits.size / 2;
		float posY = position.y + dimension.y / 2 - digitHeight / 2;
		TextureRegion digit;
		for (int i = 0; i < valueDigits.size; i++) {
			digit = valueDigits.get(i);
			batch.draw(digit.getTexture(),
					position.x + dimension.x / 2 - offsetX * scale.x, 
					posY,
					0, 0,
					digitWidth, digitHeight,
					scale.x, scale.y,
					rotation,
					digit.getRegionX(), digit.getRegionY(),
					digit.getRegionWidth(), digit.getRegionHeight(),
					false, flipY);
			offsetX -= digitWidth;
		}
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
