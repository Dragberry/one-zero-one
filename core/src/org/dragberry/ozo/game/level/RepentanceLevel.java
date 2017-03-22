package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.generator.ConstGenerator;
import org.dragberry.ozo.game.level.generator.Generator;
import org.dragberry.ozo.game.level.generator.NegativeNeutralGenerator;
import org.dragberry.ozo.game.level.settings.ReachTheGoalLevelSettings;

import java.util.HashMap;

public class RepentanceLevel extends ReachTheGoalLevel {

	public RepentanceLevel() {}

	public RepentanceLevel(ReachTheGoalLevelSettings settings) {
		super(settings);
	}
	
	@Override
	protected void createGenerators() {
		generators = new HashMap<Generator.Id, Generator>((width - 2) * (height - 2));
		int index;
		Generator gen;
		for (index = 0; index < width; index++) {
			gen = new NegativeNeutralGenerator(index % 2 == 0 ? 0 : -1, index, 0);
			generators.put(gen.id, gen);
			gen = new NegativeNeutralGenerator((index + (height - 1)) % 2 == 0 ? 0 : -1, index, height - 1);
			generators.put(gen.id, gen);
		}
		for (index = 0; index < height; index++) {
			gen = new NegativeNeutralGenerator(index % 2 == 0 ? 0 : -1, 0, index);
			generators.put(gen.id, gen);
			gen = new NegativeNeutralGenerator((index  + (width - 1)) % 2 == 0 ? 0 : -1, width - 1, index);
			generators.put(gen.id, gen);
		}
	}
	
	@Override
	protected Generator getDefaultGenerator(int x, int y) {
		return x % 2 == y % 2 ? ConstGenerator.NEG_ONE : ConstGenerator.ZERO;
	}
	
}
