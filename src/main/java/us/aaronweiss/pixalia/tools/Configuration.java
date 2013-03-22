package us.aaronweiss.pixalia.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration {
    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);
	private static Properties config;
	
	public static void load() throws IOException {
		config = new Properties();
		try {
			config.load(new FileInputStream(new File("rsc/pixalia.cfg")));
			logger.info("Loaded rsc/pixalia.cfg");
		} catch (FileNotFoundException e) {
			logger.info("Could not find rsc/pixalia.cfg");
			config.put("offline_mode", String.valueOf(Constants.OFFLINE_MODE));
			config.put("log_location", String.valueOf(Constants.LOG_LOCATION));
			config.put("log_level", String.valueOf(Constants.LOG_LEVEL));
			config.put("max_event_threads", String.valueOf(Constants.MAX_EVENT_THREADS));
			config.put("event_thread_keepalive", String.valueOf(Constants.EVENT_THREAD_KEEPALIVE));
			config.store(new FileOutputStream(new File("rsc/pixalia.cfg")), " Pixalia Configuration");
			logger.info("Created new rsc/pixalia.cfg");
		}
	}
	
	public static boolean offlineMode() {
		return Boolean.parseBoolean(config.getProperty("offline_mode"));
	}
	
	public static String logLocation() {
		return config.getProperty("log_location");
	}
	
	public static String logLevel() {
		return config.getProperty("log_level");
	}
	
	public static int maxEventThreads() {
		return Integer.parseInt(config.getProperty("max_event_threads"));
	}
	
	public static long eventThreadKeepAlive() {
		return Long.parseLong(config.getProperty("event_thread_keepalive"));
	}
}
