package org.dragberry.ozo.game.level.generator;

import org.dragberry.ozo.game.objects.Unit;

public abstract class Generator {

	public static class Id {
		int x;
		int y;
		
		public Id(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (obj instanceof Id) {
				return x == ((Id) obj).x && y == ((Id) obj).y;
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return x * 101 + y;
		}
	}
	
	public final Id id;

	public Generator(int x, int y) {
		this.id = new Id(x, y);
	}
	
	public abstract int next(int step, int selectedX, int selectedY);

	public void reset() {
		// empty by default
	}
}
