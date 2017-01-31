package org.dragberry.ozo.game.level;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import org.dragberry.ozo.game.level.goal.Goal;
import org.dragberry.ozo.game.objects.Unit;
import org.dragberry.ozo.game.util.Constants;

/**
 * Created by maksim on 30.01.17.
 */

public abstract class Level {

	protected final static int DEFAULT_WIDTH = 6;
    protected final static int DEFAULT_HEIGHT = 8;
	
    private Array<Goal> goalsToWin = new Array<Goal>();
    private Array<Goal> goalsToLose = new Array<Goal>();

    public final int width;
    public final int height;
    public final String levelName;

    public float time = 0;
    public int steps = 0;

    public int generateValue(int x, int y) {
        return MathUtils.random(-1, 1);
    }

    public Level(String levelName) {
        this(levelName, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Level(String levelName, int width, int height) {
        this.width = width;
        this.height = height;
        this.levelName = levelName;
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

    public void renderGoals(SpriteBatch batch) {
    	Vector2 goalPosition = new Vector2(25.0f, 60.0f);
		for (Goal goal : goalsToWin) {
			goal.render(batch, goalPosition.x, goalPosition.y);
			goalPosition.x += (goal.getDimension().x + 10);
		}
		goalPosition.x = Constants.VIEWPORT_GUI_WIDTH - 25.0f;
		for (Goal goal : goalsToLose) {
			goalPosition.x -= (goal.getDimension().x + 10);
			goal.render(batch, goalPosition.x, goalPosition.y);
		}
    }

}
