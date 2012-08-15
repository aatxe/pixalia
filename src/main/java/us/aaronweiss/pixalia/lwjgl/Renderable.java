package us.aaronweiss.pixalia.lwjgl;

/**
 * An interface describing renderable objects.
 * @author Aaron Weiss
 * @version 1.0
 * @since alpha
 */
public interface Renderable {
	/**
	 * Renders this object.
	 */
	public void render();
	
	/**
	 * Cleans up this object.
	 */
	public void dispose();
}
