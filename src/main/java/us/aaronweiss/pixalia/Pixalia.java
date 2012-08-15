package us.aaronweiss.pixalia;

import us.aaronweiss.pixalia.core.Game;

public class Pixalia {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Game game = new Game();
			game.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
