package org.dragberry.ozo.game.level.settings;

import org.dragberry.ozo.common.levelresult.LevelResultName;
import org.dragberry.ozo.common.levelresult.LevelResults;
import org.dragberry.ozo.common.levelresult.LevelSingleResult;
import org.dragberry.ozo.common.levelresult.NewLevelResultRequest;
import org.dragberry.ozo.common.levelresult.NewLevelResultResponse;
import org.dragberry.ozo.common.levelresult.NewLevelResultsRequest;
import org.dragberry.ozo.common.levelresult.NewLevelResultsResponse;
import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.level.Level;
import org.dragberry.ozo.game.util.TimeUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.ArrayMap;

import java.text.MessageFormat;
import java.util.Map;

/**
 * Created by maksim on 01.02.17.
 */
public class LevelSettings {

	private static final String TAG = LevelSettings.class.getName();

	private static final String COMPLETED = "completed";
	
	public final Class<? extends Level<? extends LevelSettings>> clazz;
    public final String levelId;
    public final String name;
    
    public boolean completed;

	public final LevelResults results = new LevelResults();

    public LevelSettings(Class<? extends Level<? extends LevelSettings>> clazz, String levelId) {
        this.clazz = clazz;
        this.levelId = levelId;
        this.name = Assets.instance.translation.get(levelId);
        load();
    }
    
    public void load() {
    	load(loadPreferences());
    }

	protected void load(Preferences prefs) {
		completed = prefs.getBoolean(COMPLETED, false);

		loadSingleResult(LevelResultName.TIME, prefs);
		loadSingleResult(LevelResultName.STEPS, prefs);
		loadSingleResult(LevelResultName.LOST_UNITS, prefs);
	}

	private void loadSingleResult(LevelResultName name, Preferences prefs) {
		LevelSingleResult<Integer> result = new LevelSingleResult<Integer>();
		result.setPersonal(prefs.getInteger(name.personal(), -1));
		result.setWorlds(prefs.getInteger(name.worlds(), -1));
		result.setOwner(prefs.getString(name.owner()));
		results.getResults().put(name, result);
	}
	
	public void save() {
		Preferences prefs = loadPreferences();
		update(prefs);
		prefs.flush();
	}
    
	protected void update(Preferences prefs) {
		prefs.putBoolean(COMPLETED, completed);

		updateSingleResult(LevelResultName.TIME, prefs);
		updateSingleResult(LevelResultName.STEPS, prefs);
		updateSingleResult(LevelResultName.LOST_UNITS, prefs);
	}

	private void updateSingleResult(LevelResultName name, Preferences prefs) {
		LevelSingleResult<Integer> result = results.getResults().get(name);
		prefs.putInteger(name.personal(), result.getPersonal());
		prefs.putInteger(name.worlds(), result.getWorlds());
		prefs.putString(name.owner(), result.getOwner());
	}

	protected Preferences loadPreferences() {
		return Gdx.app.getPreferences(clazz.getName() + levelId);
	}
	
	public ArrayMap<String, Object> getResults() {
		ArrayMap<String, Object> results = new ArrayMap<String, Object>();
		results.put(Assets.instance.translation.format(LevelResultName.TIME.personal()), TimeUtils.timeToString((int) bestTime));
		results.put(Assets.instance.translation.format(LevelResultName.STEPS.personal()), bestSteps);
		results.put(Assets.instance.translation.format(LevelResultName.LOST_UNITS.personal()), lostNumbers);
		return results;
	}

	/**
	 * Updates results after sending a request to the server after completing the level
	 * @param resultsFromServer
	 * @return <code>true</code> if any result is beaten
     */
	public boolean updateResults(NewLevelResultsResponse resultsFromServer) {
		boolean beaten = false;
		for (Map.Entry<LevelResultName, NewLevelResultResponse<Integer>> entry : resultsFromServer.getResults().entrySet()) {
			if (entry.getValue().isPersonal()) {
				
			}
		}
		return false;
	}

	public void updateResults(LevelResults resultsFromServer) {
		NewLevelResultsRequest newResultsRequest = new NewLevelResultsRequest();
		newResultsRequest.setLevelId(levelId);

		for (Map.Entry<LevelResultName, LevelSingleResult<Integer>> entry : results.getResults().entrySet()) {
			LevelResultName resultName = entry.getKey();
			LevelSingleResult<Integer> resultFromServer = entry.getValue();
			LevelSingleResult<Integer> result = results.getResults().get(resultName);
			Gdx.app.log(TAG, MessageFormat.format("Level [{0}]: result from server: {1}", levelId, resultFromServer));

			if (result.getWorlds() == null || result.getWorlds() > resultFromServer.getWorlds()) {
				result.setWorlds(resultFromServer.getWorlds());
				result.setOwner(resultFromServer.getOwner());
				Gdx.app.log(TAG, MessageFormat.format("Level [{0}]: new worlds record has been received: {1} = {2}",
						levelId, resultName, result.getWorlds()));
			}

			if (result.getPersonal() == null || result.getPersonal() >= resultFromServer.getPersonal()) {
				Gdx.app.log(TAG, MessageFormat.format("Level [{0}]: personal record is not beaten", levelId));
				result.setPersonal(resultFromServer.getPersonal());
			} else {
				Gdx.app.log(TAG, MessageFormat.format("Level [{0}]: personal record is beaten: {1}: old = {2}, new = {3}",
						levelId, resultName, resultFromServer.getPersonal(), result.getPersonal()));
				newResultsRequest.getResults().put(resultName, new NewLevelResultRequest<Integer>(result.getPersonal()));

				if (result.getPersonal() != null && result.getWorlds() != null && result.getPersonal() < result.getWorlds()) {
					Gdx.app.log(TAG, MessageFormat.format("Level [{0}]: world record is beaten: {1}: old = {2}, new = {3}",
							levelId, resultName, result.getWorlds(), result.getPersonal()));
					result.setWorlds(result.getPersonal());
					result.setOwner(resultFromServer.getOwner());
				}
			}
		}
	}
}
