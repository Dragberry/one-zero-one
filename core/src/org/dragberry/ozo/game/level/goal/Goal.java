package org.dragberry.ozo.game.level.goal;

import org.dragberry.ozo.game.objects.Unit;

/**
 * Created by maksim on 29.01.17.
 */

public interface Goal {

    boolean isReached(Unit[][] units, Unit selectedUnit, Unit[] neighbors);

    String getMessage();
}
