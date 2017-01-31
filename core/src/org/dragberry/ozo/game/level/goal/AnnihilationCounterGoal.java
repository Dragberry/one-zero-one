package org.dragberry.ozo.game.level.goal;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.objects.GoalUnit;
import org.dragberry.ozo.game.objects.Unit;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class AnnihilationCounterGoal implements Goal {
	
	private int annihilationCounter;
	private int goal;
	
	private GoalUnit posUnit;
	private GoalUnit negUnit;
	private GlyphLayout layout;
	private Vector2 dimension;

	public AnnihilationCounterGoal(int goal) {
		this.goal = goal;
		this.posUnit = new GoalUnit(goal);
		this.negUnit = new GoalUnit(-goal);
		this.layout = new GlyphLayout(Assets.instance.fonts._19, "+");
		this.dimension = new Vector2(posUnit.dimension.x + layout.width + negUnit.dimension.x, negUnit.dimension.y); 
	}
	
	@Override
	public boolean isReached(Unit[][] units, Unit selectedUnit, Unit[] neighbors) {
		int pos = 0;
		int neg = 0;
		if (selectedUnit.previousValue > 0) {
			pos += selectedUnit.previousValue;
		} else if (selectedUnit.previousValue < 0) {
			neg += -selectedUnit.previousValue;
		}
		for (Unit unit : neighbors) {
			if (unit.value > 0) {
				pos += unit.value;
			} else if (unit.value < 0) {
				neg += -unit.value;
			}
		}
		if (neg != 0 && pos != 0) {
			annihilationCounter += neg;
		}
		return annihilationCounter >= goal;
	}

	@Override
	public String getMessage() {
		return "Annihilate " + annihilationCounter;
	}

	@Override
	public void render(SpriteBatch batch, float x, float y) {
		posUnit.setPosition(x, y);
		posUnit.render(batch);
		Assets.instance.fonts._19.draw(batch, layout, 
				x + posUnit.dimension.x, y + posUnit.dimension.y / 2 - layout.height / 2);
		negUnit.setPosition(x + posUnit.dimension.x + layout.width, y);
		negUnit.render(batch);
	}

	@Override
	public Vector2 getDimension() {
		return dimension;
	}

}
