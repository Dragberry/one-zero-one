package org.dragberry.ozo.game.objects;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.util.Constants;
import org.dragberry.ozo.game.util.DigitUtil;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public abstract class AbstractUnit extends AbstractGameObject {

	protected int value;
	protected boolean flipY;
	protected Array<TextureRegion> valueDigits = new Array<TextureRegion>(4);
	
	public AbstractUnit(int value) {
		this.value = value;
		DigitUtil.resolveDigits(value, valueDigits);
	}

	public AbstractUnit() {
		this(0);
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

		batch.setColor(Color.WHITE);
		batch.draw(sign.regBall.getTexture(),
				position.x, position.y,
				origin.x, origin.y,
				dimension.x, dimension.y,
				scale.x, scale.y,
				rotation,
				sign.regBall.getRegionX(), sign.regBall.getRegionY(),
				sign.regBall.getRegionWidth(), sign.regBall.getRegionHeight(),
				false, flipY);

		batch.setColor(Constants.COLOR_UNIT_TEXT);
		DigitUtil.draw(batch, valueDigits,
				position.x + dimension.x / 2, position.y + dimension.y / 2,
				valueDigits.size > 3 ? scale.x * 0.8f : scale.y,
				valueDigits.size > 3 ? scale.x * 0.8f : scale.y,
				rotation,
				false, flipY);
		
	}
	
	protected enum Sign {
		MINUS(Assets.instance.unit.ballRed, Constants.NEGATIVE, "-"),
		ZERO(Assets.instance.unit.ballBlue, Constants.NEUTRAL, ""),
		PLUS(Assets.instance.unit.ballGreen, Constants.POSITIVE, "+");

		public TextureRegion regBall;
		public Color color;
		public String sign;
		
		Sign(TextureRegion regBall, Color color, String sign) {
			this.regBall = regBall;
			this.color = color;
			this.sign = sign;
		}
	}
}
