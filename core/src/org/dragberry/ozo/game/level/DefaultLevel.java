package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.objects.Unit;

public class DefaultLevel extends Level {
	
	private static final int TARGET_VALUE = 5;
	private static final String WIN_CONDITION_MSG = "collect +" + TARGET_VALUE;
	private static final String LOSE_CONDITION_MSG = "collect -" + TARGET_VALUE;
	
	public DefaultLevel(int width, int height) {
		super(width, height);
	}
	
	@Override
	public String getWinConditionMsg() {
		return WIN_CONDITION_MSG;
	}
	
	@Override
	public String getLoseConditionMsg() {
		return LOSE_CONDITION_MSG;
	}

	@Override
	public boolean isLost(Unit[][] units) {
		for (Unit[] row : units) {
			for (Unit unit : row) {
				if (unit.value < -TARGET_VALUE) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isWon(Unit[][] units) {
		for (Unit[] row : units) {
			for (Unit unit : row) {
				if (unit.value > TARGET_VALUE) {
					return true;
				}
			}
		}
		return false;
	}

}
