package org.dragberry.ozo.game.level.goal;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.objects.GoalUnit;
import org.dragberry.ozo.game.objects.Unit;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class AnnihilationCounterGoal extends AbstractGoal {
	
	private int annihilationCounter;
	private int goal;
	
	private GoalUnit posUnit;
	private GoalUnit negUnit;
	private GoalUnit zeroUnit;
	private GlyphLayout layout;

	private float offset = 0;
	private float animationTime = 0;
	private boolean animationAfter = false;

	public AnnihilationCounterGoal(int goal) {
		this.goal = goal;
		this.posUnit = new GoalUnit(goal);
		this.negUnit = new GoalUnit(-goal);
		this.zeroUnit = new GoalUnit(0);
		this.layout = new GlyphLayout(Assets.instance.fonts.gui_xs, "+");
		this.dimension = new Vector2(posUnit.dimension.x + layout.width + negUnit.dimension.x, negUnit.dimension.y); 
	}

	@Override
	public void update(float deltaTime) {
		animationTime += deltaTime;
		if (animationTime > 3) {
			animationTime = 0;
			offset = 0;
		}
		float newOffset = offset + deltaTime * GoalUnit.SIZE / 2;
		if (newOffset < dimension.x / 4) {
			offset = newOffset;
			animationAfter = false;
		} else {
			newOffset = dimension.x / 4;
			animationAfter = true;
		}
		offset = MathUtils.clamp(newOffset, newOffset, dimension.x / 4);
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
			annihilationCounter += pos < neg ? pos : neg;
		}
		return annihilationCounter >= goal;
	}

	@Override
	public String getMessage() {
		return "Lost " + goal + " positive numbers";
	}

	@Override
	public void render(SpriteBatch batch, float x, float y) {
		posUnit.value = goal - annihilationCounter;
		negUnit.value = -(goal - annihilationCounter);
		if (animationAfter) {
			zeroUnit.position.set(x + offset, y);
			zeroUnit.render(batch);
		} else {
			posUnit.position.set(x + offset, y);
			posUnit.render(batch);
			Assets.instance.fonts.gui_xs.draw(batch, layout,
					x + posUnit.dimension.x, y + posUnit.dimension.y / 2 - layout.height / 2);
			negUnit.position.set(x + posUnit.dimension.x + layout.width - offset, y);
			negUnit.render(batch);
		}
	}

}
