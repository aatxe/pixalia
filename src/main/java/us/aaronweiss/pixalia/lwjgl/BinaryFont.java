package us.aaronweiss.pixalia.lwjgl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BinaryFont {
	private static BinaryFont defaultFont;
	private byte font[] = new byte[0x200000];
	private boolean full[] = new boolean[0x10000];
	
	public BinaryFont(String path) throws IOException {
		this(new File(path));
	}
	
	public BinaryFont(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		if (file.canRead() && fis.available() >= 0x210000) {
			for (int i = 0; i < full.length; i++) {
				full[i] = fis.read() == 0x01;
			}
			fis.read(font);
		} else {
			fis.close();
			throw new IOException("File is malformed or inaccessible.");
		}
		fis.close();
	}
	
	public byte get(int index) {
		return font[index];
	}
	
	public boolean isFull(int index) {
		return full[index];
	}
	
	public void dump(String path) throws IOException {
		this.dump(new File(path));
	}
	
	public void dump(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(font);
		fos.close();
	}
	
	public static void setDefault(BinaryFont defaultFont) {
		BinaryFont.defaultFont = defaultFont;
	}
	
	public static BinaryFont getDefault() {
		return BinaryFont.defaultFont;
	}
}
