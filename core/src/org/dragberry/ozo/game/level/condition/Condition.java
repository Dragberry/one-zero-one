package org.dragberry.ozo.game.level.condition;

import org.dragberry.ozo.game.objects.Unit;

/**
 * Created by maksim on 29.01.17.
 */

public interface Condition {

    boolean isMet(Unit[][] units, Unit selectedUnit, Unit[] neighbors);

    String getMessage();
}
