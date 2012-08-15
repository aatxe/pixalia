package us.aaronweiss.pixalia.ui;

public interface Hideable {
	public void show();
	
	public void hide();
	
	public void toggleVisibility();
	
	public void setVisible(boolean visible);

	public boolean isVisible();
}
