package org.dragberry.ozo.game.level;

import com.badlogic.gdx.Gdx;

import org.dragberry.ozo.common.levelresult.LevelResultName;
import org.dragberry.ozo.common.levelresult.NewLevelResultRequest;
import org.dragberry.ozo.common.levelresult.NewLevelResultsRequest;
import org.dragberry.ozo.game.GameController;
import org.dragberry.ozo.game.level.goal.DynamicReachGoal;
import org.dragberry.ozo.game.level.settings.FreeplayReachTheGoalLevelSettings;
import org.dragberry.ozo.game.objects.Unit;
import org.dragberry.ozo.screen.popup.AbstractGameFinishedPopup;
import org.dragberry.ozo.screen.popup.FreeplayFinishedPopup;

/**
 * Created by maksim on 30.01.17.
 */

public class FreeplayLevel extends Level<FreeplayReachTheGoalLevelSettings> {

    private final static String TAG = FreeplayLevel.class.getName();

    private final static float MIN_RATIO = 0.1f;
    private final static float RATIO_STEP = 0.06f;

    private int posGoalValue;
    private float ratio;
    private transient DynamicReachGoal negGoal;
    private transient DynamicReachGoal posGoal;

    public FreeplayLevel() {}

    public FreeplayLevel(FreeplayReachTheGoalLevelSettings settings) {
    	super(settings);
    }

    @Override
    protected void addGoals(FreeplayReachTheGoalLevelSettings settings) {
        if (posGoalValue == 0) {
            posGoalValue = settings.posGoalValue;
            ratio = settings.ratio;
        }
        posGoal = new DynamicReachGoal(settings.posGoalValue, posGoalValue > settings.posGoalValue ? posGoalValue : settings.posGoalValue);
        negGoal = new DynamicReachGoal((int) -(settings.posGoalValue * ratio), (int) -(posGoalValue * ratio));
        addGoalToLose(negGoal);
        addGoalToWin(posGoal);
    }

    @Override
    protected boolean isGameFinished() {
        int maxValue = getMaxValue();
        if (maxValue >= posGoalValue) {
            posGoalValue = posGoalValue * 2;
            posGoal.updateGoal(posGoalValue);
            ratio -= RATIO_STEP;
            ratio = ratio < MIN_RATIO ? MIN_RATIO : ratio;
            negGoal.updateGoal((int) -(posGoalValue * ratio));
        }
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
        int maxValue = getMaxValue();
        results.getResults().put(LevelResultName.MAX_VALUE,
                new NewLevelResultRequest<Integer>(maxValue));
        results.getResults().put(LevelResultName.MAX_AND_LOST,
                new NewLevelResultRequest<Integer>(maxValue - lostNumbers));
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

    @Override
    public void reset(boolean restore) {
        super.reset(restore);
        if (!restore) {
            posGoalValue = settings.posGoalValue;
            ratio = settings.ratio;
        }
    }
}
