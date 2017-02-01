package org.dragberry.ozo.game.objects;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.util.Constants;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;

public class GoalUnit extends AbstractUnit {
	
	public static final float SIZE = Constants.UNIT_SIZE * 0.6f;
	
	public GoalUnit(int value) {
		super(value);
		this.dimension = new Vector2(SIZE, SIZE);
	}
	
	@Override
	protected BitmapFont getFont() {
		return Assets.instance.fonts.gui_24;
	}

	@Override
	protected float getScaleX() {
		return scale.x;
	}

	@Override
	protected float getScaleY() {
		return scale.y;
	}
	
	@Override
	protected float getFontX(GlyphLayout layout) {
		return position.x + (dimension.x - layout.width) * 0.375f;
	}

	@Override
	protected float getFontY(GlyphLayout layout) {
		return position.y + dimension.y / 2 - layout.height / 2;
	}

}
