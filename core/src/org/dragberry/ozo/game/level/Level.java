package org.dragberry.ozo.game.level;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import org.dragberry.ozo.game.level.goal.Goal;
import org.dragberry.ozo.game.objects.Unit;

/**
 * Created by maksim on 30.01.17.
 */

public abstract class Level {

    private Array<Goal> goalsToWin = new Array<Goal>();
    private Array<Goal> goalsToLose = new Array<Goal>();

    protected final static int DEFAULT_WIDTH = 6;
    protected final static int DEFAULT_HEIGHT = 8;

    public final int width;
    public final int height;

    public float time = 0;
    public int steps = 0;

    public int generateValue(int x, int y) {
        return MathUtils.random(-1, 1);
    }

    public Level() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Level(int width, int height) {
        this.width = width;
        this.height = height;
    }

    protected void addGoalToWin(Goal goalToWin) {
        this.goalsToWin.add(goalToWin);
    }

    protected void addGoalToLose(Goal goalToLose) {
        this.goalsToLose.add(goalToLose);
    }

    public boolean isLost(Unit[][] units, Unit selectedUnit, Unit[] neighbors) {
        return checkGoals(units, selectedUnit, neighbors, goalsToLose);
    }

    public boolean isWon(Unit[][] units, Unit selectedUnit, Unit[] neighbors) {
        return checkGoals(units, selectedUnit, neighbors, goalsToWin);
    }

    public static boolean checkGoals(Unit[][] units, Unit selectedUnit, Unit[] neighbors, Array<Goal> goals) {
        boolean reached = true;
        for (Goal goal : goals) {
            if (!goal.isReached(units, selectedUnit, neighbors)) {
                reached = false;
            }
        }
        return reached;
    }
}
