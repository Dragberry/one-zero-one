package org.dragberry.ozo.game.objects;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.util.Constants;
import org.dragberry.ozo.game.util.DigitUtil;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.io.Serializable;

public abstract class AbstractUnit extends AbstractGameObject implements Serializable {

	protected int value;
	protected transient boolean flipY;
	protected transient Array<TextureRegion> valueDigits = new Array<TextureRegion>(4);

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

		batch.setColor(sign.color);
		batch.draw(Assets.instance.level.unit.body.getTexture(),
				position.x, position.y,
				origin.x, origin.y,
				dimension.x, dimension.y,
				scale.x * 0.8f, scale.y * 0.8f,
				rotation,
				Assets.instance.level.unit.body.getRegionX(), Assets.instance.level.unit.body.getRegionY(),
				Assets.instance.level.unit.body.getRegionWidth(), Assets.instance.level.unit.body.getRegionHeight(),
				false, flipY);

		batch.setColor(Color.WHITE);
		batch.draw(Assets.instance.level.unit.frame.getTexture(),
				position.x, position.y,
				origin.x, origin.y,
				dimension.x, dimension.y,
				scale.x, scale.y,
				rotation,
				Assets.instance.level.unit.frame.getRegionX(), Assets.instance.level.unit.frame.getRegionY(),
				Assets.instance.level.unit.frame.getRegionWidth(), Assets.instance.level.unit.frame.getRegionHeight(),
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

		batch.setColor(sign.numColor);
		DigitUtil.draw(batch, valueDigits,
				position.x + dimension.x / 2, position.y + dimension.y / 2,
				scale.x * zoomFactor ,
				scale.y * zoomFactor,
				rotation,
				false, flipY);
		batch.setColor(Color.WHITE);


	}
	
	protected enum Sign {
		MINUS(Assets.instance.level.unit.negative, Constants.NEGATIVE, Constants.NEGATIVE_TXT, "-"),
		ZERO(Assets.instance.level.unit.neutral, Constants.NEUTRAL, Constants.NEUTRAL_TXT, ""),
		PLUS(Assets.instance.level.unit.positive, Constants.POSITIVE, Constants.POSITIVE_TXT, "+");

		public TextureRegion regBall;
		public Color color;
		public Color numColor;
		public String sign;
		
		Sign(TextureRegion regBall, Color color, Color numColor, String sign) {
			this.regBall = regBall;
			this.color = color;
			this.numColor = numColor;
			this.sign = sign;
		}
	}
}
