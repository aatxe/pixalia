package us.aaronweiss.pixalia.lwjgl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import us.aaronweiss.pixalia.tools.Constants;

public class GLString implements Renderable {
	private BinaryFont font;
	private String value;
	private byte buf[] = new byte[0x1000];
	private int vbo = -1;
	private int texture = -1;
	private int width = 0;
	
	public GLString(String value, BinaryFont font) {
		this.value = value;
		this.font = font;
		this.vbo = this.createVBO();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, 8 * Constants.SIZEOF_FLOAT, GL15.GL_DYNAMIC_DRAW);
		ByteBuffer buffer = GL15.glMapBuffer(GL15.GL_ARRAY_BUFFER, GL15.GL_WRITE_ONLY, null);
		FloatBuffer buf = buffer.order(ByteOrder.nativeOrder()).asFloatBuffer();
		buf.put(new float[]{0f, 0f});
		buf.put(new float[]{0f, 1f});
		buf.put(new float[]{1f, 1f});
		buf.put(new float[]{1f, 0f});
		GL15.glUnmapBuffer(GL15.GL_ARRAY_BUFFER);
		this.init();
	}
	
	public String getValue() {
		return this.value;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public void render() {
		this.render(0f, 0f);
	}
	
	public void render(float x, float y) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glVertexPointer(2, GL11.GL_FLOAT, 2 * Constants.SIZEOF_FLOAT, 0);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 2 * Constants.SIZEOF_FLOAT, 0);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glTranslated(x, y, 0f);
		GL11.glScaled(width, -0x10, 1);
		GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);
		GL11.glPopMatrix();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
	}

	public void dispose() {
		if (texture > 0) 
			GL11.glDeleteTextures(texture);
		if (vbo > 0)
			vbo = -1;
	}
	
	private void init() {
		FloatBuffer color = BufferUtils.createFloatBuffer(2);
		color.put(new float[]{0f, 1f});
		color.rewind();
		GL11.glPixelMap(GL11.GL_PIXEL_MAP_I_TO_R, color);
		color.rewind();
		GL11.glPixelMap(GL11.GL_PIXEL_MAP_I_TO_G, color);
		color.rewind();
		GL11.glPixelMap(GL11.GL_PIXEL_MAP_I_TO_B, color);
		color.rewind();
		GL11.glPixelMap(GL11.GL_PIXEL_MAP_I_TO_A, color);
		texture = this.generateTexture();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		for (int i = 0; i < value.length(); i++) {
			if (font.isFull(value.charAt(i))) {
				width += 2;
			} else {
				width += 1;
			}
		}
		for (int i = 0, j = 0; i < value.length(); ++i) {
			int c = value.charAt(i);
			boolean full = font.isFull(c);
			for (int k = 0; k < 16; ++k) {
				if (full) {
					buf[j + k * width + 1] = font.get((c << 5) + (k << 1));
					buf[j + k * width] = font.get((c << 5) + (k << 1) + 1);
				} else {
					buf[j + k * width] = font.get((c << 5) + (k << 1));
				}
			}
			if (full)
				j += 2;
			else
				j += 1;
		}
		width <<= 3;
		ByteBuffer buff = BufferUtils.createByteBuffer(buf.length);
		buff.put(buf);
		buff.rewind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, 0x10, 0, GL11.GL_COLOR_INDEX, GL11.GL_BITMAP, buff);		
	}
	
	private int createVBO() {
		IntBuffer vbof = BufferUtils.createIntBuffer(1);
		GL15.glGenBuffers(vbof);
		return vbof.get(0);
	}
	
	private int generateTexture() {
		IntBuffer buf = BufferUtils.createIntBuffer(1);
		GL11.glGenTextures(buf);
		return buf.get(0);
	}
}
