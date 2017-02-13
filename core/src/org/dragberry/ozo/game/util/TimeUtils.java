package org.dragberry.ozo.game.util;

public final class TimeUtils {
	
	private static final int SECONDS_PER_MINUTE = 60;
	private static final String EMPTY = "";
	private static final String ZERO = "0";
	private static final String COLON = ":";
	
	private TimeUtils() {}
	
	public static String timeToString(int timeInSeconds) {
		int minutes = timeInSeconds / SECONDS_PER_MINUTE;
		int seconds = timeInSeconds % SECONDS_PER_MINUTE;
		return prefixZero(minutes) + COLON + prefixZero(seconds);
	}
	
	private static String prefixZero(int time) {
		return (time < 10 ? ZERO : EMPTY) + time;
	}

}
