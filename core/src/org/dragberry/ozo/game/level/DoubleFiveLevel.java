package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.objects.Unit;

/**
 * Created by maksim on 28.01.17.
 */

public class DoubleFiveLevel extends Level {

    private static final int TARGET_WIN_VALUE = 5;
    private static final String WIN_CONDITION_MSG = "get 2 units of " + TARGET_WIN_VALUE;
    private static final int TARGET_LOSE_VALUE = -10;
    private static final String LOSE_CONDITION_MSG = "get " + TARGET_LOSE_VALUE;
    private static final int TARGET_UNITS = 2;


    public DoubleFiveLevel() {
        super(6, 8);
    }

    @Override
    public boolean isLost(Unit[][] units) {
        for (Unit[] row : units) {
            for (Unit unit : row) {
                if (unit.value == TARGET_LOSE_VALUE) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isWon(Unit[][] units) {
        int counter = 0;
        for (Unit[] row : units) {
            for (Unit unit : row) {
                if (unit.value == TARGET_WIN_VALUE) {
                    counter++;
                }
            }
        }
        return counter == TARGET_UNITS;
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
    public String getName() {
        return "Double Five";
    }
}
