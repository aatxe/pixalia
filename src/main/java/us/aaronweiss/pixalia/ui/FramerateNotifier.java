package us.aaronweiss.pixalia.ui;

import us.aaronweiss.pixalia.lwjgl.GLString;
import us.aaronweiss.pixalia.tools.GLStringUtils;

public class FramerateNotifier extends HideableUIElement {
	private GLString notifier;
	private int width, height;
	
	public FramerateNotifier(int width, int height) {
		this.width = width;
		this.height = height;
		this.notifier = GLStringUtils.getString("(0)");
		this.hide();
	}
	
	public void render() {
		notifier.render(width - notifier.getWidth() - 4, height - 4);
	}

	public void update(int fps) {
		this.notifier = GLStringUtils.getString("(" + fps + ")");
	}
	
	public void dispose() {
		notifier.dispose();
	}
}
