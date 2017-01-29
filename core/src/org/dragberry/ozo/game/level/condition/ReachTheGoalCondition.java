package org.dragberry.ozo.game.level.condition;

import org.dragberry.ozo.game.objects.Unit;

/**
 * Created by maksim on 29.01.17.
 */

public class ReachTheGoalCondition implements Condition {

    public enum Operator {
        LESS, EQUALS, MORE
    }

    private int goal;

    public ReachTheGoalCondition(int goal, Operator operator) {
        this.goal = goal;
    }

    @Override
    public boolean isMet(Unit[][] units, Unit selectedUnit, Unit[] neighbors) {
        for (Unit[] row : units) {
            for (Unit unit : row) {
                if (unit.value == goal) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
