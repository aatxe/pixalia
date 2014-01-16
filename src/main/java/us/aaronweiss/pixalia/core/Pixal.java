package us.aaronweiss.pixalia.core;

import us.aaronweiss.pixalia.tools.Vector;

public class Pixal {
	private final String hostname;
	private Vector position;
	protected Vector color;
	
	public Pixal(String hostname) {
		this(hostname, new Vector(0f, 0f), new Vector(0f, 0f, 0f, 1f));
	}

	public Pixal(String hostname, Vector color) {
		this(hostname, new Vector(0f, 0f), color);
	}

	public Pixal(String hostname, Vector position, Vector color) {
		this.hostname = hostname;
		this.position = position;
		this.color = color;
	}
	
	public String getHostname() {
		return this.hostname;
	}
	
	public Vector getPosition() {
		return this.position;
	}
	
	public Vector getColor() {
		return this.color;
	}
	
	public void setPosition(Vector position) {
		this.position = position;
	}
	
	public void move(float x, float y) {
		this.position = new Vector(this.position.getX() + x, this.position.getY() + y);
	}
}
