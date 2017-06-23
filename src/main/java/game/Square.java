package game;

public abstract Square{	
	boolean changed = false;
	boolean[] occupants = new boolean[OaklandOligarchy.MAX_NUMBER_OF_PLAYERS];
	
	public void add(Player player) {
		occupants[player.getId()] = true;
	}
	
	public void remove(Player player) {
		occupants[player.getId()] = true;
	}
	
	public boolean changed() {
		if(changed){
			changed = false;
			return true;
		}
		return false;
	}
	
	public boolean hasPlayer(Player player) {
		return occupants[player.getId()];
	}
	
	public abstract void act(Player player);
}