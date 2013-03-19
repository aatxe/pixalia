package us.aaronweiss.pixalia.tools;

public class Constants {
	public static final int TICKS_PER_SECOND = 60;
	public static final int SIZEOF_FLOAT = 4;
	public static final int SIZEOF_INT = 4;
	public static final int MAX_EVENT_THREADS = Runtime.getRuntime().availableProcessors() * 4;
	public static final long EVENT_THREAD_KEEPALIVE = 5000;
}
