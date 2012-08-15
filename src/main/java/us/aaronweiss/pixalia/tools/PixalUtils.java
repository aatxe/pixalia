package us.aaronweiss.pixalia.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * A set of static utilities for Pixalia.
 * @author Aaron Weiss
 * @version 1.0
 * @since alpha
 */
public class PixalUtils {
	private static String cachedUsername = "";
	private static String cachedIP = "";
	
	public static String getLocalHostname() {
		return PixalUtils.getUsername() + "@localhost";
	}
	
	public static String getPublicHostname() {
		return PixalUtils.getUsername() + "@" + PixalUtils.getIPAddress();
	}
	
	public static String getUsername() {
		if (cachedUsername.isEmpty()) {
			if (System.getProperty("user.name").isEmpty()) {
				cachedUsername = "Pixal";
			} else {
				cachedUsername = System.getProperty("user.name");
			}
		}
		return cachedUsername;
	}
	
	public static String getIPAddress() {
		if (cachedIP.isEmpty()) {
			String ip = "";
			try {
				URL whatismyip = new URL("http://api.externalip.net/ip/");
				BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
				ip = in.readLine();
			} catch (IOException e) {
				try {
					URL whatismyip = new URL("http://automation.whatismyip.com/n09230945.asp");
					BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
					ip = in.readLine();
				} catch (IOException ex) {
					ip = "0.0.0.0";
				}
			}
			cachedIP = ip;
		}
		return cachedIP;
	}
}
