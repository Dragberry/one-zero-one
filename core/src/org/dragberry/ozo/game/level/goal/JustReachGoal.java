package org.dragberry.ozo.game.level.goal;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.objects.GoalUnit;
import org.dragberry.ozo.game.objects.Unit;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by maksim on 29.01.17.
 */

public class JustReachGoal extends AbstractGoal {

    public enum Operator {

        LESS("ozo.goal.justReahGoalLE", new OperatorExecutor() {
            @Override
            public boolean execute(int goalValue, int valueToCheck) {
                return valueToCheck <= goalValue;
            }
        }),
        EQUALS("ozo.goal.justReahGoalE", new OperatorExecutor() {
            @Override
            public boolean execute(int goalValue, int valueToCheck) {
                return valueToCheck == goalValue;
            }
        }),
        MORE("ozo.goal.justReahGoalGE", new OperatorExecutor() {
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

    private GoalUnit unit;
    private int goal;
    private Operator operator;

    public JustReachGoal(int goal, Operator operator) {
        this.goal = goal;
        this.operator = operator;
        this.unit = new GoalUnit(goal);
        this.dimension = unit.dimension;
        this.msg = Assets.instance.translation.format(operator.msg, goal);
    }

    @Override
    public boolean isReached(Unit[][] units, Unit selectedUnit, Array<Unit> neighbors) {
        for (Unit[] row : units) {
            for (Unit unit : row) {
                if (operator.executor.execute(goal, unit.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void render(SpriteBatch batch, float x, float y) {
        unit.position.set(x, y);
        unit.render(batch);
    }

}