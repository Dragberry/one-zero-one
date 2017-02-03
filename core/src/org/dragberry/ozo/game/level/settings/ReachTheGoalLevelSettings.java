package org.dragberry.ozo.game.level.settings;

import org.dragberry.ozo.game.level.Level;
import org.dragberry.ozo.game.level.ReachTheGoalLevel;
import org.dragberry.ozo.game.level.goal.JustReachGoal;

/**
 * Created by maksim on 03.02.17.
 */
public class ReachTheGoalLevelSettings extends LevelSettings {

	public final int goalToLose;
	public final int goal;
	public final JustReachGoal.Operator operator;

	public ReachTheGoalLevelSettings(String name, int goalToLose, int goal) {
        this(ReachTheGoalLevel.class, name, goalToLose, goal, JustReachGoal.Operator.EQUALS);
    }

	public ReachTheGoalLevelSettings(Class<? extends Level<? extends LevelSettings>> clazz, String name, int goalToLose, int goal) {
        this(clazz, name, goalToLose, goal, JustReachGoal.Operator.EQUALS);
    }

    public ReachTheGoalLevelSettings(String name, int goalToLose, int goal, JustReachGoal.Operator operator) {
        this(ReachTheGoalLevel.class, name, goalToLose, goal, operator);
    }

    public ReachTheGoalLevelSettings(Class<? extends Level<? extends LevelSettings>> clazz, String name, int goalToLose, int goal, JustReachGoal.Operator operator) {
        super(clazz, name);
        this.goalToLose = goalToLose;
        this.goal = goal;
        this.operator = operator;
    }
}
