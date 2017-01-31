package org.dragberry.ozo.game.level;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import org.dragberry.ozo.game.level.goal.Goal;
import org.dragberry.ozo.game.objects.Unit;
import org.dragberry.ozo.game.util.Constants;

/**
 * Created by maksim on 30.01.17.
 */

public abstract class Level implements Disposable {

	protected final static int DEFAULT_WIDTH = 6;
    protected final static int DEFAULT_HEIGHT = 8;
	
    private Array<Goal> goalsToWin = new Array<Goal>();
    private Array<Goal> goalsToLose = new Array<Goal>();

    private FrameBuffer goalsFbo;

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
        this.goalsFbo = new FrameBuffer(Pixmap.Format.RGBA8888, 
        		(int) Constants.VIEWPORT_GUI_WIDTH, (int) Constants.VIEWPORT_GUI_HEIGHT, false);
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

    public FrameBuffer renderGoalsFbo(SpriteBatch batch) {
    	goalsFbo.begin();
    	batch.setColor(1, 1, 1, 1);
    	renderGoals(batch);
    	goalsFbo.end();
    	return goalsFbo;
    }

	protected void renderGoals(SpriteBatch batch) {
		for (Goal goal : goalsToWin) {
//			goal.render(batch);
		}
	}
    
    @Override
    public void dispose() {
    	goalsFbo.dispose();
    }
}
