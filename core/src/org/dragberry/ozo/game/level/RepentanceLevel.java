package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.generator.ConstGenerator;
import org.dragberry.ozo.game.level.generator.Generator;
import org.dragberry.ozo.game.level.generator.RepentanceGenerator;
import org.dragberry.ozo.game.level.settings.ReachTheGoalLevelSettings;
import org.dragberry.ozo.game.objects.Unit;

import java.util.HashMap;

public class RepentanceLevel extends ReachTheGoalLevel {

	private transient RepentanceGenerator.PositivePosition positivePosition;

	public RepentanceLevel() {}

	public RepentanceLevel(ReachTheGoalLevelSettings settings) {
		super(settings);
	}
	
	@Override
	protected void createGenerators() {
		positivePosition = new RepentanceGenerator.PositivePosition();

		generators = new HashMap<String, Generator>((width - 2) * (height - 2));
		int index;
		Generator gen;
		for (index = 0; index < width; index++) {
			gen = new RepentanceGenerator(index % 2 == 0 ? 0 : -1, index, 0, positivePosition);
			generators.put(gen.id, gen);
			gen = new RepentanceGenerator((index + (height - 1)) % 2 == 0 ? 0 : -1, index, height - 1, positivePosition);
			generators.put(gen.id, gen);
		}
		for (index = 0; index < height; index++) {
			gen = new RepentanceGenerator(index % 2 == 0 ? 0 : -1, 0, index, positivePosition);
			generators.put(gen.id, gen);
			gen = new RepentanceGenerator((index  + (width - 1)) % 2 == 0 ? 0 : -1, width - 1, index, positivePosition);
			generators.put(gen.id, gen);
		}
	}
	
	@Override
	protected Generator getDefaultGenerator(int x, int y) {
		return x % 2 == y % 2 ? ConstGenerator.NEG_ONE : ConstGenerator.ZERO;
	}

	private boolean isCross() {
		if (neighbors.size < 4) {
			return false;
		}
		float previousSign = Math.signum(selectedUnit.previousValue);
		for (Object obj : neighbors) {
			Unit neighbor = (Unit) obj;
			float sign = Math.signum(neighbor.getValue());
			if (previousSign != sign) {
				return false;
			}
			previousSign = sign;
		}
		return true;
	}

	@Override
	protected void updateGeneratorsState() {
		if (isCross()) {
			positivePosition.updatePosition();
		}
	}

	@Override
	public void reset(boolean restore) {
		super.reset(restore);
		if (positivePosition == null) {
			positivePosition = new RepentanceGenerator.PositivePosition();
		}
		for (Generator generator : generators.values()) {
			if (generator instanceof RepentanceGenerator) {
				((RepentanceGenerator) generator).setPositivePosition(positivePosition);
			}
		}
	}
}
