package org.dragberry.ozo.game.level.settings;

import org.dragberry.ozo.game.level.ReachMultiGoalLevel;

/**
 * Created by maksim on 03.02.17.
 */
public class ReachMultiGoalLevelSettings extends LevelSettings {

	public final int goalToLose;
	public final int[] goals;

    public ReachMultiGoalLevelSettings(String name, int goalToLose, int... goals) {
        super(ReachMultiGoalLevel.class, name);
        this.goalToLose = goalToLose;
        this.goals = goals;
    }

}
