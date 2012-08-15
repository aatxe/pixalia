package us.aaronweiss.pixalia.ui;

import us.aaronweiss.pixalia.lwjgl.GLString;
import us.aaronweiss.pixalia.tools.Constants;
import us.aaronweiss.pixalia.tools.GLStringUtils;
import us.aaronweiss.pixalia.tools.Vector;

public class PositionNotifier extends HideableUIElement {
	private int height;
	private float totalTicksPassed;
	private GLString position;
	
	public PositionNotifier(int height) {
		this.height = height;
		position = GLStringUtils.getString("(?, ?)");
		totalTicksPassed = 0;
		this.hide();
	}
	
	public void render() {
		this.position.render(4, height - 4);
	}
	
	public void update(float ticksPassed, Vector position) {
		totalTicksPassed += ticksPassed;
		if (totalTicksPassed > (Constants.TICKS_PER_SECOND / 6)) {
			this.position = GLStringUtils.getString("(" + position.getX() + ", " + position.getY() + ")");
			totalTicksPassed = 0;
		}
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
