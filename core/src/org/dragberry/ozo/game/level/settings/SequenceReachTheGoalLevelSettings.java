package org.dragberry.ozo.game.level.settings;

import org.dragberry.ozo.game.level.Level;
import org.dragberry.ozo.game.level.ReachTheGoalLevel;
import org.dragberry.ozo.game.level.goal.JustReachGoal;

/**
 * Created by maksim on 03.02.17.
 */
public class SequenceReachTheGoalLevelSettings extends ReachTheGoalLevelSettings {

	public final int initialSequence;

	public SequenceReachTheGoalLevelSettings(Class<? extends Level<? extends LevelSettings>> clazz, String name, int goalToLose, int goal, int initialSequence) {
        super(clazz, name, goalToLose, goal);
        this.initialSequence = initialSequence;
    }

}
