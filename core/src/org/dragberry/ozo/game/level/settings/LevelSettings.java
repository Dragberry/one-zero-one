package org.dragberry.ozo.game.level.settings;

import org.dragberry.ozo.common.levelresult.LevelResultName;
import org.dragberry.ozo.common.levelresult.LevelResults;
import org.dragberry.ozo.common.levelresult.LevelSingleResult;
import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.level.Level;
import org.dragberry.ozo.game.util.TimeUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.ArrayMap;

import java.util.Map;

/**
 * Created by maksim on 01.02.17.
 */
public class LevelSettings {
	
	private static final String COMPLETED = "completed";
	
	public final Class<? extends Level<? extends LevelSettings>> clazz;
    public final String nameKey;
    public final String name;
    
    public boolean completed;
    public float bestTime;
    public int bestSteps;
	public int lostNumbers;

	public final LevelResults results = new LevelResults();

    public LevelSettings(Class<? extends Level<? extends LevelSettings>> clazz, String nameKey) {
        this.clazz = clazz;
        this.nameKey = nameKey;
        this.name = Assets.instance.translation.get(nameKey);
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
		return Gdx.app.getPreferences(clazz.getName() + nameKey);
	}
	
	public ArrayMap<String, Object> getResults() {
		ArrayMap<String, Object> results = new ArrayMap<String, Object>();
		results.put(Assets.instance.translation.format(LevelResultName.TIME.personal()), TimeUtils.timeToString((int) bestTime));
		results.put(Assets.instance.translation.format(LevelResultName.STEPS.personal()), bestSteps);
		results.put(Assets.instance.translation.format(LevelResultName.LOST_UNITS.personal()), lostNumbers);
		return results;
	}

	public void updateResults(LevelResults results) {
		Preferences prefs = loadPreferences();
		for (Map.Entry<LevelResultName, LevelSingleResult<Integer>> entry : results.getResults().entrySet()) {
			LevelResultName name = entry.getKey();
			LevelSingleResult<Integer> result = entry.getValue()
;
			int personal = prefs.getInteger(name.personal(), -1);
			int worlds = prefs.getInteger(name.worlds(), -1);
			String owner = prefs.getString(name.owner());

			if (personal == -1 || personal < entry.getValue().getPersonal()) {

			}

			// / world result
			// world result owner
			// your result

		}

		update(prefs);
		prefs.flush();
	}
}
