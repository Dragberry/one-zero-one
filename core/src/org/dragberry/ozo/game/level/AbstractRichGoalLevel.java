package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.objects.Unit;

/**
 * Created by maksim on 29.01.17.
 */

public abstract class AbstractRichGoalLevel extends Level {

    protected int goal;
    protected int loseCondition;
    protected String winConditionMsg;
    protected String loseConditionMsg;

    public AbstractRichGoalLevel(int goal) {
        this(goal, -goal);
    }

    public AbstractRichGoalLevel(int goal, int loseCondition) {
        super();
        this.goal = goal;
        this.loseCondition = loseCondition;
        winConditionMsg = "get +" + goal;
        loseConditionMsg = "get -" + loseCondition;
    }

    @Override
    public String getWinConditionMsg() {
        return winConditionMsg;
    }

    @Override
    public String getLoseConditionMsg() {
        return loseConditionMsg;
    }

    @Override
    public boolean isLost(Unit[][] units) {
        for (Unit[] row : units) {
            for (Unit unit : row) {
                if (unit.value == loseCondition) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isWon(Unit[][] units) {
        for (Unit[] row : units) {
            for (Unit unit : row) {
                if (unit.value == goal) {
                    return true;
                }
            }
        }
        return false;
    }

}
