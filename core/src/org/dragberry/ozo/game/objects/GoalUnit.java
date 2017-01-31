package org.dragberry.ozo.game.objects;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.util.Constants;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

public class GoalUnit extends AbstractUnit {
	
	private final static float SCALE = 1;

	public GoalUnit(int value) {
		super(value);
		this.dimension = new Vector2(Constants.UNIT_SIZE, Constants.UNIT_SIZE);
	}
	
	public void setPosition(float positionX, float positionY) {
		position.set(positionX, positionY);
	}

	@Override
	protected BitmapFont getFont() {
		return Assets.instance.fonts.gui_24;
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
