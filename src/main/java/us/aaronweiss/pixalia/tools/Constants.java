package us.aaronweiss.pixalia.tools;

public class Constants {
	public static final String LOG_LOCATION = "System.err";
	public static final String LOG_LEVEL = "info";
	public static final int TICKS_PER_SECOND = 60;
	public static final int SIZEOF_FLOAT = 4;
	public static final int SIZEOF_INT = 4;
	public static final int MAX_EVENT_THREADS = Runtime.getRuntime().availableProcessors() * 4;
	public static final long EVENT_THREAD_KEEPALIVE = 5000;
	public static final boolean OFFLINE_MODE = true;
	public static final double TICKS_FOR_MOVEMENT_UPDATE = 1;
	public static final int MAX_FRAME_LENGTH = 300;
}
