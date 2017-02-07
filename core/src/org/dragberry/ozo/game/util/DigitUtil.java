package org.dragberry.ozo.game.util;

import org.dragberry.ozo.game.Assets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public final class DigitUtil {

	private DigitUtil(){}
	
	public static void draw(SpriteBatch batch, Array<TextureRegion> texRegions,
			float positionX, float positionY,
			float scaleX, float scaleY,
			float rotation,
			boolean flipX, boolean flipY) {
		int digitWidth = Assets.instance.digits.plus.getRegionWidth();
		int digitHeight= Assets.instance.digits.plus.getRegionHeight();
		float offsetX = digitWidth * texRegions.size / 2;
		float posY = positionY - digitHeight / 2;
		TextureRegion digit;
		for (int i = 0; i < texRegions.size; i++) {
			digit = texRegions.get(i);
			batch.draw(digit.getTexture(),
					positionX - offsetX * scaleX, 
					posY,
					0, 0,
					digitWidth, digitHeight,
					scaleX, scaleY,
					rotation,
					digit.getRegionX(), digit.getRegionY(),
					digit.getRegionWidth(), digit.getRegionHeight(),
					flipX, flipY);
			offsetX -= digitWidth;
		}
	}
	
	public static void resolveDigits(int value, Array<TextureRegion> texRegions) {
		texRegions.clear();
		resolveNextDigit(value, texRegions);
		if (value < 0) {
			texRegions.add(Assets.instance.digits.minus);
		}
		if (value > 0) {
			texRegions.add(Assets.instance.digits.plus);
		}
		texRegions.reverse();
	}

	private static void resolveNextDigit(int value, Array<TextureRegion> texRegions) {
		int digit = value % 10;
		texRegions.add(Assets.instance.digits.digits[Math.abs(digit)]);
		value /= 10;
		if (value != 0) {
			resolveNextDigit(value, texRegions);
		}
	}
}
