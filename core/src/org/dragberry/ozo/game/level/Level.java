package org.dragberry.ozo.game.level;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Collections;
import java.util.Map;

import org.dragberry.ozo.game.level.generator.Generator;
import org.dragberry.ozo.game.level.generator.RandomGenerator;
import org.dragberry.ozo.game.level.goal.AbstractGoal;
import org.dragberry.ozo.game.objects.Unit;
import org.dragberry.ozo.game.util.CameraHelper;
import org.dragberry.ozo.game.level.settings.LevelSettings;

/**
 * Created by maksim on 30.01.17.
 */

public abstract class Level<LI extends LevelSettings> {

	protected final static int DEFAULT_WIDTH = 6;
    protected final static int DEFAULT_HEIGHT = 8;
	
    public final Array<AbstractGoal> goalsToWin = new Array<AbstractGoal>();
    public final Array<AbstractGoal> goalsToLose = new Array<AbstractGoal>();
    
    protected Map<Generator.Id, Generator> generators = Collections.emptyMap();
    
    public final LI settings;
    public final int width;
    public final int height;

    public float time = 0;
    public int steps = 0;
	public boolean started = false;
    
    public Level(LI settings) {
        this(settings, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Level(LI settings, int width, int height) {
        this.width = width;
        this.height = height;
        this.settings = settings;
        createGenerators();
    }
    
    protected void createGenerators() {
    	generators = Collections.emptyMap();
    }

    public Unit generateUnit(int x, int y) {
    	Generator gen = null;
    	if (!generators.isEmpty()) {
    		gen = generators.get(new Generator.Id(x, y));
    	}
    	if (gen == null) {
    		gen = getDefaultGenerator(x, y);
    	}
        return new Unit(gen.next(), x, y);
    }
    
    protected Generator getDefaultGenerator(int x, int y) {
    	 return RandomGenerator.DEFAUTL;
    }

    protected void addGoalToWin(AbstractGoal goalToWin) {
        this.goalsToWin.add(goalToWin);
    }

    protected void addGoalToLose(AbstractGoal goalToLose) {
        this.goalsToLose.add(goalToLose);
    }

    public boolean isLost(Unit[][] units, Unit selectedUnit, Unit[] neighbors) {
        return checkGoals(units, selectedUnit, neighbors, goalsToLose);
    }

    public boolean isWon(Unit[][] units, Unit selectedUnit, Unit[] neighbors) {
        return checkGoals(units, selectedUnit, neighbors, goalsToWin);
    }

    public static boolean checkGoals(Unit[][] units, Unit selectedUnit, Unit[] neighbors, Array<AbstractGoal> goals) {
        boolean reached = true;
        for (AbstractGoal goal : goals) {
            if (!goal.isReached(units, selectedUnit, neighbors)) {
                reached = false;
            }
        }
        return reached;
    }

    public void renderGoals(SpriteBatch batch) {
    	Vector2 goalPosition = new Vector2(25.0f, 65.0f);
		for (AbstractGoal goal : goalsToWin) {
			goal.render(batch, goalPosition.x, goalPosition.y);
			goalPosition.x += (goal.dimension.x + 10);
		}
		goalPosition.x = CameraHelper.INSTANCE.cameraGui.viewportWidth - 25.0f;
		for (AbstractGoal goal : goalsToLose) {
			goalPosition.x -= (goal.dimension.x + 10);
			goal.render(batch, goalPosition.x, goalPosition.y);
		}
    }

    public void update(float deltaTime) {
        for (AbstractGoal goal : goalsToWin) {
            goal.update(deltaTime);
        }
        for (AbstractGoal goal : goalsToLose) {
            goal.update(deltaTime);
        }
    }

	public void save() {
		settings.bestSteps =
				Math.min(settings.bestSteps == 0 ? steps : settings.bestSteps, steps);
		settings.bestTime =
				Math.min(settings.bestTime == 0 ? time : settings.bestTime, time);
		settings.completed = true;
		settings.save();
	}
}
