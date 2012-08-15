package us.aaronweiss.pixalia.core;

import java.util.Random;
import us.aaronweiss.pixalia.tools.Vector;

public class Pixal {
	private final String hostname;
	private Random randomizer;
	private Vector position;
	private Vector color;
	private float angle = 0;
	
	public Pixal(String hostname) {
		this.hostname = hostname;
		this.randomizer = new Random();
		// this.position = new Vector(0f, 0f);
		this.position = new Vector(this.randomizer.nextInt(100) * 8, this.randomizer.nextInt(75) * 8);
		this.color = new Vector(randomizer.nextFloat(), randomizer.nextFloat(), randomizer.nextFloat(), 1f);
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
	
	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	public float getAngle() {
		return this.angle;
	}
	
	public void move(float x, float y) {
		this.position = new Vector(this.position.getX() + x, this.position.getY() + y);
	}
}
