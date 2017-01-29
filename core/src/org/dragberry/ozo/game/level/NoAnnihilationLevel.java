package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.objects.Unit;

/**
 * Created by maksim on 29.01.17.
 */

public class NoAnnihilationLevel extends AbstractLevel {

    protected int annihilationLimit;

    @Override
    public boolean isLost(Unit[][] units, Unit selectedUnit, Unit[] neighbors) {
        return false;
    }

    @Override
    public boolean isWon(Unit[][] units, Unit selectedUnit, Unit[] neighbors) {
        return false;
    }

    @Override
    public String getWinConditionMsg() {
        return null;
    }

    @Override
    public String getLoseConditionMsg() {
        return null;
    }

    @Override
    public String getName() {
        return "No Annihilation";
    }
}
