package org.dragberry.ozo.game.level.generator;

/**
 * Generator of -1,1,-1,1,...
 * @author Maksim Drahun
 *
 */
public class AlternationOppositesGenerator extends Generator {

	private int value;
	
	public AlternationOppositesGenerator(int initialValue, int x, int y) {
		super(x, y);
		this.value = initialValue;
	}

	@Override
	public int next() {
		value = -value;
		return value;
	}

}
