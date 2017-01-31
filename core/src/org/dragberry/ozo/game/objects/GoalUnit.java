package org.dragberry.ozo.game.objects;

import org.dragberry.ozo.game.Assets;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class GoalUnit extends AbstractUnit {
	
	private final static float SCALE = 0.5f;

	public GoalUnit(int value, int positionX, int positionY) {
		super(value);
		position.set(positionX, positionY);
	}

	@Override
	protected BitmapFont getFont() {
		return Assets.instance.fonts._29;
	}

	@Override
	protected float getScaleX() {
		return SCALE;
	}

	@Override
	protected float getScaleY() {
		return SCALE;
	}


}
