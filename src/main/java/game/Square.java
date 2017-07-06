package game;

public abstract class Square{
	protected String name;
	
	public String getName() {
		return name;
	}
	
	public abstract boolean act(Player player);
}