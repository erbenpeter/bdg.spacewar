package bdg.spacewar.player;

import bdg.spacewar.SpaceWarPlayer;

public class Player implements SpaceWarPlayer {
	boolean asd = false;
	
	@Override
	public void configuration(String csv) {

	}

	@Override
	public String round(String csv) {
		if(!asd){
			asd = true;
			return "0 0.08 true";
		}
		return "0 0.08 false";
	}
}