package org.dragberry.ozo.game.level.goal;

import org.dragberry.ozo.game.objects.Unit;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by maksim on 29.01.17.
 */

public interface Goal {

    boolean isReached(Unit[][] units, Unit selectedUnit, Unit[] neighbors);

    String getMessage();

	void render(SpriteBatch batch, float x, float y);

	Vector2 getDimension();
}
