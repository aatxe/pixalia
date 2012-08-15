package us.aaronweiss.pixalia.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

public class TextInputHandler {
	private final Map<Character, Character> keymap = new HashMap<Character, Character>();
	private final List<Character> allowed = new ArrayList<Character>();
	private final String lang = "en-US";
	private String input;
	private boolean isShifted = false;
	
	public TextInputHandler() throws LWJGLException, IOException {
		Keyboard.create();
		this.input = "";
		this.loadKeymap("rsc/lang/" + lang + "/keymap.properties");
		this.loadAllowed("rsc/lang/" + lang + "/allowed.txt");
	}
	
	private void loadKeymap(String path) throws IOException {
		this.loadKeymap(new File(path));
	}
	
	private void loadKeymap(File file) throws IOException {
		if (file.exists() && file.canRead()) {
			Properties keys = new Properties();
			keys.load(new FileInputStream(file));
			for (Object key : keys.keySet()) {
				String skey = (String) key;
				String sval = (String) keys.get(key);
				if (skey.equalsIgnoreCase("equals") && sval.length() == 1) {
					keymap.put('=', sval.charAt(0));
				} else if (skey.length() == 1 && sval.length() == 1) {
					keymap.put(skey.charAt(0), sval.charAt(0));
				}
			}
		}
	}
	
	private void loadAllowed(String path) throws IOException {
		this.loadAllowed(new File(path));
	}
	
	private void loadAllowed(File file) throws IOException {
		if (file.exists() && file.canRead()) {
			FileInputStream fis = new FileInputStream(file);
			Scanner in = new Scanner(fis);
			while (in.hasNextLine()) {
				String line = in.nextLine();
				String[] entries = line.split(",");
				for (String entry : entries) {
					if (entry.length() == 1) {
						allowed.add(entry.charAt(0));
					} else if (entry.equalsIgnoreCase("comma")) {
						allowed.add(',');
					} else {
						throw new IOException("Malformed character allowance file (" + file.getCanonicalPath() + ")");
					}
				}
			}
		}
	}
	
	private char shift(char c) {
		try { 
			return keymap.get(c);
		} catch (NullPointerException e) {
			return c;
		}
	}
	
	public void update() {
		if (Keyboard.getEventKey() == Keyboard.KEY_LSHIFT || Keyboard.getEventKey() == Keyboard.KEY_RSHIFT) {
			isShifted = Keyboard.getEventKeyState(); 
		} else if (Keyboard.isKeyDown(Keyboard.KEY_DELETE)) {
			this.reset();
		} else if (Keyboard.isKeyDown(Keyboard.KEY_BACK) && Keyboard.getEventKeyState()) {
			this.backspace();
		} else {
			char c = (isShifted) ? this.shift(Keyboard.getEventCharacter()) : Keyboard.getEventCharacter();
			if (this.allowed.contains(c)) {
				this.input += c;
			}
		}
	}
	
	public String getText() {
		return this.input;
	}

	public void backspace() {
		try {
			this.input = this.input.substring(0, input.length() - 1);
		} catch (StringIndexOutOfBoundsException e) {}
	}
	
	public void reset() {
		this.input = "";
	}
}
