package org.dragberry.ozo.game.level;

import java.util.HashMap;

import org.dragberry.ozo.game.level.generator.AlternationOppositesGenerator;
import org.dragberry.ozo.game.level.generator.ConstGenerator;
import org.dragberry.ozo.game.level.generator.Generator;

public class ChessboardLevel extends ReachTheGoalLevel {
	

	public ChessboardLevel(ReachTheGoalLevel.ReachTheGoalLevelInfo levelInfo) {
		super(levelInfo);
	}
	
	@Override
	protected void createGenerators() {
		generators = new HashMap<Generator.Id, Generator>((width - 2) * (height - 2));
		int index;
		Generator gen;
		for (index = 1; index < width - 1; index++) {
			gen = new AlternationOppositesGenerator(index % 2 == 0 ? 1 : -1, index, 0);
			generators.put(gen.id, gen);
			gen = new AlternationOppositesGenerator((index + (height - 1)) % 2 == 0 ? 1 : -1, index, height - 1);
			generators.put(gen.id, gen);
		}
		for (index = 1; index < height - 1; index++) {
			gen = new AlternationOppositesGenerator(index % 2 == 0 ? 1 : -1, 0, index);
			generators.put(gen.id, gen);
			gen = new AlternationOppositesGenerator((index  + (width - 1)) % 2 == 0 ? 1 : -1, width - 1, index);
			generators.put(gen.id, gen);
		}
	}
	
	@Override
	protected Generator getDefaultGenerator(int x, int y) {
		return x % 2 == y % 2 ? ConstGenerator.NEG_ONE : ConstGenerator.POS_ONE;
	}
	
}
