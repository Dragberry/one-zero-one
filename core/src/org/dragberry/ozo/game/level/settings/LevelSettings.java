package org.dragberry.ozo.game.level.settings;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.level.Level;
import org.dragberry.ozo.game.util.TimeUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.ArrayMap;

/**
 * Created by maksim on 01.02.17.
 */
public class LevelSettings {
	
    private static final String BEST_STEPS = "bestSteps";
	private static final String BEST_TIME = "bestTime";
	private static final String COMPLETED = "completed";
	
	public final Class<? extends Level<? extends LevelSettings>> clazz;
    public final String nameKey;
    public final String name;
    
    public boolean completed;
    public float bestTime;
    public int bestSteps;

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
    	bestTime = prefs.getFloat(BEST_TIME, 0);
    	bestSteps = prefs.getInteger(BEST_STEPS, 0);
	}
	
	public void save() {
		Preferences prefs = loadPreferences();
		update(prefs);
		prefs.flush();
	}
    
	private void update(Preferences prefs) {
		prefs.putBoolean(COMPLETED, completed);
		prefs.putFloat(BEST_TIME, bestTime);
		prefs.putInteger(BEST_STEPS, bestSteps);
	}

	protected Preferences loadPreferences() {
		return Gdx.app.getPreferences(clazz.getName() + nameKey);
	}
	
	public ArrayMap<String, Object> getResults() {
		ArrayMap<String, Object> results = new ArrayMap<String, Object>();
		results.put(Assets.instance.translation.format("ozo.bestTime"), TimeUtils.timeToString((int) bestTime));
		results.put(Assets.instance.translation.format("ozo.bestSteps"), bestSteps);
		return results;
	}
}
