package org.dragberry.ozo.game.level.goal;

import org.dragberry.ozo.game.objects.Unit;

public class AnnihilationCounterGoal implements Goal {
	
	private int annihilationCounter;
	private int goal;

	public AnnihilationCounterGoal(int goal) {
		this.goal = goal;
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

}
