package us.aaronweiss.pixalia.core;

import us.aaronweiss.pixalia.tools.Vector;

public class Player extends Pixal {
	public Player(String hostname) {
		super(hostname);
	}
	
	public void setColor(Vector color) {
		this.color = color;
	}
}
