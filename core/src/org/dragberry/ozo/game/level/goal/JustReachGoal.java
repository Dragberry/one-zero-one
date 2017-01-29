package org.dragberry.ozo.game.level.goal;

import org.dragberry.ozo.game.objects.Unit;

/**
 * Created by maksim on 29.01.17.
 */

public class JustReachGoal implements Goal {

    public enum Operator {

        LESS("reach less than ", new OperatorExecutor() {
            @Override
            public boolean execute(int goalValue, int valueToCheck) {
                return valueToCheck <= goalValue;
            }
        }),
        EQUALS("reach ", new OperatorExecutor() {
            @Override
            public boolean execute(int goalValue, int valueToCheck) {
                return valueToCheck == goalValue;
            }
        }),
        MORE("reach more than", new OperatorExecutor() {
            @Override
            public boolean execute(int goalValue, int valueToCheck) {
                return valueToCheck >= goalValue;
            }
        });

        private String msg;
        private OperatorExecutor executor;

        Operator(String msg, OperatorExecutor executor) {
            this.msg = msg;
            this.executor = executor;
        }

    }

    private interface OperatorExecutor {
        boolean execute(int goalValue, int valueToCheck);
    }

    private int goal;
    private Operator operator;

    public JustReachGoal(int goal, Operator operator) {
        this.goal = goal;
        this.operator = operator;
    }

    @Override
    public boolean isReached(Unit[][] units, Unit selectedUnit, Unit[] neighbors) {
        for (Unit[] row : units) {
            for (Unit unit : row) {
                if (operator.executor.execute(goal, unit.value)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getMessage() {
        return operator.msg + goal;
    }
}
