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

		float zoomFactor;
		switch (valueDigits.size) {
			case 1:
			case 2:
			case 3:
				zoomFactor = 1;
				break;
			case 4:
				zoomFactor = 0.8f;
				break;
			case 5:
				zoomFactor = 0.6f;
				break;
			default:
				zoomFactor = 0.4f;
		}
		DigitUtil.draw(batch, valueDigits,
				position.x + dimension.x / 2, position.y + dimension.y / 2,
				scale.x * zoomFactor ,
				scale.y * zoomFactor,
				rotation,
				false, flipY);
		
	}
	
	protected enum Sign {
		MINUS(Assets.instance.level.unit.negative, Constants.NEGATIVE, "-"),
		ZERO(Assets.instance.level.unit.neutral, Constants.NEUTRAL, ""),
		PLUS(Assets.instance.level.unit.positive, Constants.POSITIVE, "+");

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
