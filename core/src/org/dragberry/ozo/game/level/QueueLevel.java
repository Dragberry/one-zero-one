package org.dragberry.ozo.game.level;

import java.util.HashMap;

import org.dragberry.ozo.game.level.generator.ConstGenerator;
import org.dragberry.ozo.game.level.generator.Generator;
import org.dragberry.ozo.game.level.generator.QueueGenerator;

public class QueueLevel extends ReachTheGoalLevel {

	public QueueLevel(String levelName, Integer goalToLose, Integer goal) {
		super(levelName, goalToLose, goal);
	}

	@Override
	protected void createGenerators() {
		generators = new HashMap<Generator.Id, Generator>((width - 2) * (height - 2));
		int index;
		Generator gen;
		for (index = 1; index < width - 1; index++) {
			
			gen = new QueueGenerator(getInitialValue(index, 0), index, 0);
			generators.put(gen.id, gen);
			gen = new QueueGenerator(getInitialValue(index, height -1), index, height - 1);
			generators.put(gen.id, gen);
		}
		for (index = 1; index < height - 1; index++) {
			gen = new QueueGenerator(getInitialValue(0, index), 0, index);
			generators.put(gen.id, gen);
			gen = new QueueGenerator(getInitialValue(width - 1, index), width - 1, index);
			generators.put(gen.id, gen);
		}
	}
	
	private static int getInitialValue(int x, int y) {
		switch ((x + y) % 3) {
		case 0:
			return -1;
		case 1:
			return 0;
		case 2:
			return 1;
		default:
			throw new IllegalArgumentException();
		}
	}
	
	@Override
	protected Generator getDefaultGenerator(int x, int y) {
		switch ((x + y) % 3) {
		case 0:
			return ConstGenerator.NEG_ONE;
		case 1:
			return ConstGenerator.ZERO;
		case 2:
			return ConstGenerator.POS_ONE;
		default:
			throw new IllegalArgumentException();
		}
	}
}
