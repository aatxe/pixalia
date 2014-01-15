package us.aaronweiss.pixalia.tools;

import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * A set of static utilities for Pixalia.
 * @author Aaron Weiss
 * @version 1.0
 * @since alpha
 */
public class Utils {
	private static final Logger logger = LoggerFactory.getLogger(Utils.class);
	private static final CharsetDecoder utfDecoder = Charset.forName("UTF-8").newDecoder();
	private static String cachedUsername = "";
	private static String cachedIP = "";
	
	public static String getLocalHostname() {
		return Utils.getUsername() + "@localhost";
	}
	
	public static String getPublicHostname() {
		return Utils.getUsername() + "@" + Utils.getIPAddress();
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

	public static String readString(int length, ByteBuf b) {
		try {
			byte[] data = new byte[length];
			b.readBytes(data);
			return utfDecoder.decode(ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN)).toString();
		} catch (CharacterCodingException e) {
			logger.error("Failed to load UTF String in buffer.", e);
		}
		return null;
	}
	
	public static String toHexString(byte[] array) {
		String ret = "";
		for (byte b : array) {
			String s = Integer.toHexString(b);
			ret += ((s.length() == 1) ? "0" : "") + s + " ";
		}
		return ret;
	}
}
