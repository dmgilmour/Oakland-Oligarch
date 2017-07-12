package game;

/**
 * This object is used to keep track of the game time. Particularly, for saving and loading the game.
 *
 * @author Woodrow Fulmer
 */
public class Time{
	private int startTime;
	private int loadTime;
	private int time;
	
	public Time() {
		loadTime = 0;
	}
	
	public Time(int lt) {
		loadTime = lt;
		
	}
	
	public String toString() {
		int hours = time / 3600;
		int minutes = time / 60 % 60;
		int seconds = time % 60;
		return String.format("Time Played: %02d:%02d:%02d", hours, minutes, seconds);
	}
	
	public void start() {
		startTime = (int)(System.currentTimeMillis() / 1000);
	}
	public void update() {
		time = (int)(System.currentTimeMillis()/1000 - startTime) + loadTime;
	}
	
	public int getTime() {
		return time;
	}
}