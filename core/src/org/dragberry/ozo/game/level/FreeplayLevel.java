package org.dragberry.ozo.game.level;

import com.badlogic.gdx.Gdx;

import org.dragberry.ozo.common.levelresult.LevelResultName;
import org.dragberry.ozo.common.levelresult.NewLevelResultRequest;
import org.dragberry.ozo.common.levelresult.NewLevelResultsRequest;
import org.dragberry.ozo.game.GameController;
import org.dragberry.ozo.game.level.goal.DynamicReachGoal;
import org.dragberry.ozo.game.level.goal.JustReachGoal;
import org.dragberry.ozo.game.level.settings.FreeplayReachTheGoalLevelSettings;
import org.dragberry.ozo.game.level.settings.ReachTheGoalLevelSettings;
import org.dragberry.ozo.game.objects.Unit;
import org.dragberry.ozo.screen.popup.AbstractGameFinishedPopup;
import org.dragberry.ozo.screen.popup.AbstractPopup;
import org.dragberry.ozo.screen.popup.FreeplayFinishedPopup;

/**
 * Created by maksim on 30.01.17.
 */

public class FreeplayLevel extends Level<FreeplayReachTheGoalLevelSettings> {

    private final static String TAG = FreeplayLevel.class.getName();

    private int goalValue;
    private transient DynamicReachGoal goal;

    public FreeplayLevel() {}

    public FreeplayLevel(FreeplayReachTheGoalLevelSettings settings) {
    	super(settings);
    }

    @Override
    protected void addGoals(FreeplayReachTheGoalLevelSettings settings) {
        goal = new DynamicReachGoal(goalValue < settings.goal ? goalValue : settings.goal);
        addGoalToLose(goal);
    }

    @Override
    protected boolean isGameFinished() {
        int maxValue = getMaxValue();
        
        if (isLost()) {
            GameController.instance.onGameWon(this);
            return true;
        }
        return false;
    }

    @Override
    public Class<? extends AbstractGameFinishedPopup> getGameFinishedPopup() {
        return FreeplayFinishedPopup.class;
    }

    @Override
    public NewLevelResultsRequest formNewResults() {
        NewLevelResultsRequest results = new NewLevelResultsRequest();
        results.getResults().put(LevelResultName.MAX_VALUE,
                new NewLevelResultRequest<Integer>(getMaxValue()));
        Gdx.app.debug(TAG, "Form new results");
        return results;
    }

    private int getMaxValue() {
        int maxValue = Integer.MIN_VALUE;
        for (Unit[] row : units) {
            for (Unit unit : row) {
                if (maxValue < unit.getValue()) {
                    maxValue = unit.getValue();
                }
            }
        }
        return maxValue;
    }
}
