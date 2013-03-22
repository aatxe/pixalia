package us.aaronweiss.pixalia;

import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.aaronweiss.pixalia.core.Game;
import us.aaronweiss.pixalia.tools.Configuration;

public class Pixalia {
    private static final Logger logger = LoggerFactory.getLogger(Pixalia.class);
	
    /**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Configuration.load();
			if (Configuration.logLocation().equals("System.out") && Configuration.logLocation().equals("System.err"))
				System.out.println("Logging to " + Configuration.logLocation());
			System.setProperty("org.slf4j.simpleLogger.logFile", Configuration.logLocation());
			System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", Configuration.logLevel());
			Game game = new Game();
			logger.info("OpenGL Version: " + GL11.glGetString(GL11.GL_VERSION));
			game.open();
		} catch (Exception e) {
			logger.error("An unexpected error has occurred.", e);
		}
	}
}
