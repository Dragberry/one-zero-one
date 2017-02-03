package org.dragberry.ozo.screen;

import org.dragberry.ozo.game.level.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by maksim on 01.02.17.
 */
public class LevelInfo {
	
    private static final String BEST_STEPS = "bestSteps";
	private static final String BEST_TIME = "bestTime";
	private static final String COMPLETED = "completed";
	
	public final Class<? extends Level<? extends LevelInfo>> clazz;
    public final String name;
    
    public boolean completed;
    public float bestTime;
    public int bestSteps;

    public LevelInfo(Class<? extends Level<? extends LevelInfo>> clazz, String name) {
        this.clazz = clazz;
        this.name = name;
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
		return Gdx.app.getPreferences(clazz.getName() + name);
	}
}
