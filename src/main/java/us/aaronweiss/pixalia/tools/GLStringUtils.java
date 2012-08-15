package us.aaronweiss.pixalia.tools;

import java.util.HashMap;
import java.util.Map;

import us.aaronweiss.pixalia.lwjgl.BinaryFont;
import us.aaronweiss.pixalia.lwjgl.GLString;

public class GLStringUtils {
	private static Map<String, GLString> cache = new HashMap<String, GLString>();
	
	public static GLString getString(String value) {
		GLString ret = cache.get(value);
		if (ret != null) {
			return ret;
		} else {
			GLString string = new GLString(value, BinaryFont.getDefault());
			cache.put(value, string);
			return string;
		}
	}
}
