package org.dragberry.ozo.game.level.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import org.dragberry.ozo.common.levelresult.LevelResultName;
import org.dragberry.ozo.common.levelresult.NewLevelResultsRequest;
import org.dragberry.ozo.common.levelresult.NewLevelResultsResponse;
import org.dragberry.ozo.game.level.Level;

/**
 * Created by maksim on 03.02.17.
 */
public class FreeplayReachTheGoalLevelSettings extends LevelSettings {

    private static final String TAG = FreeplayReachTheGoalLevelSettings.class.getName();

	public final float ratio;
    public final int posGoalValue;

    public FreeplayReachTheGoalLevelSettings(Class<? extends Level<? extends LevelSettings>> clazz, String name, float ratio, int posGoal) {
        super(clazz, name);
        this.ratio = ratio;
        this.posGoalValue = posGoal;
    }

    @Override
    protected void load(Preferences prefs) {
        Gdx.app.debug(TAG, "load results for " + levelId);
        loadSingleResult(LevelResultName.MAX_VALUE, prefs);
    }

    @Override
    protected void update(Preferences prefs) {
        Gdx.app.debug(TAG, "update results for " + levelId);
        updateSingleResult(LevelResultName.MAX_VALUE, prefs);
    }

    @Override
    public NewLevelResultsResponse checkLocalResults(NewLevelResultsRequest newResults) {
        NewLevelResultsResponse response = new NewLevelResultsResponse();
        checkSingleLocalResult(newResults, response, LevelResultName.MAX_VALUE, GREATER_RESULT_COMPORATOR);
        return response;
    }
}
