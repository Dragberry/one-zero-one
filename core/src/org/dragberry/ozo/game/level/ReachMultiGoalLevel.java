package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.goal.AbstractGoal;
import org.dragberry.ozo.game.level.goal.JustReachGoal;
import org.dragberry.ozo.game.level.goal.MultiGoal;
import org.dragberry.ozo.game.level.settings.ReachMultiGoalLevelSettings;
import org.dragberry.ozo.game.objects.Unit;

/**
 * Created by maksim on 30.01.17.
 */

public class ReachMultiGoalLevel extends Level<ReachMultiGoalLevelSettings> {

    public ReachMultiGoalLevel() {}

    public ReachMultiGoalLevel(ReachMultiGoalLevelSettings settings) {
        super(settings);
    }

    @Override
    protected void addGoals(ReachMultiGoalLevelSettings settings) {
        addGoalToLose(new JustReachGoal(settings.goalToLose, JustReachGoal.Operator.LESS));
        addGoalToWin(new MultiGoal(settings.goals));
    }

    @Override
    protected void processPulsation() {
        MultiGoal multiGoal = (MultiGoal) goalsToWin.get(0);
        multiGoal.resetPulsation();
        for (Unit[] row : units) {
            for (Unit unit : row) {
               multiGoal.check(unit);
            }
        }
    }
}
