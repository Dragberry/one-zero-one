package org.dragberry.ozo.game.level.settings;

import org.dragberry.ozo.game.level.Level;
import org.dragberry.ozo.game.level.NoAnnihilationLevel;

/**
 * Created by maksim on 03.02.17.
 */
public class NoAnnihilationLevelSettings extends LevelSettings {

	public final int goalToLose;
	public final int goal;

	public NoAnnihilationLevelSettings(String name, int goalToLose, int goal) {
		this(NoAnnihilationLevel.class, name, goalToLose, goal);
    }

	public NoAnnihilationLevelSettings(Class<? extends Level<? extends LevelSettings>> clazz, String name, int goalToLose, int goal) {
		super(clazz, name);
		this.goalToLose = goalToLose;
		this.goal = goal;
	}

}
