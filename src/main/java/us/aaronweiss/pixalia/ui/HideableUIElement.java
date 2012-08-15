package us.aaronweiss.pixalia.ui;

import us.aaronweiss.pixalia.lwjgl.Renderable;

public abstract class HideableUIElement implements Hideable, Renderable {
	private boolean visible;
	
	public void show() {
		visible = true;
	}

	public void hide() {
		visible = false;
	}
	
	public void toggleVisibility() {
		visible = !visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}

}
