package bdg.spacewar.player;

import bdg.spacewar.SpaceWarPlayer;

public class Player implements SpaceWarPlayer {
	@Override
	public void configuration(String csv) {

	}
	
	@Override
	public String round(String csv) {
		return "0 0 0";
	}
}
