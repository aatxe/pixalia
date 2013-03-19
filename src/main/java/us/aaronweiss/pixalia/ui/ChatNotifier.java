package us.aaronweiss.pixalia.ui;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import us.aaronweiss.pixalia.lwjgl.Renderable;
import us.aaronweiss.pixalia.tools.Constants;

public class ChatNotifier implements Renderable {
	private final float[] stdColor = new float[]{0.4f, 0.4f, 0.4f, 0.4f};
	private final float[] newColor = new float[]{0.2f, 0.8f, 0.2f, 0.6f};
	private boolean newMessage = true;
	private int vbo = -1;
	
	public void render() {
		if (vbo <= 0) {
			vbo = this.createVBO();
			this.vboBufferData(vbo);
		}
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		ByteBuffer buffer = GL15.glMapBuffer(GL15.GL_ARRAY_BUFFER, GL15.GL_WRITE_ONLY, null);
		FloatBuffer buf = buffer.order(ByteOrder.nativeOrder()).asFloatBuffer();
		buf.put(new float[]{8f, 8f});
		buf.put((newMessage) ? newColor : stdColor);
		buf.put(new float[]{8f + 16f, 8f});
		buf.put((newMessage) ? newColor : stdColor);
		buf.put(new float[]{8f + 16f, 8f + 16f});
		buf.put((newMessage) ? newColor : stdColor);
		buf.put(new float[]{8f, 8f + 16f});
		buf.put((newMessage) ? newColor : stdColor);
		GL15.glUnmapBuffer(GL15.GL_ARRAY_BUFFER);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glVertexPointer(2, GL11.GL_FLOAT, 6 * Constants.SIZEOF_FLOAT, 0);
		GL11.glColorPointer(4, GL11.GL_FLOAT, 6 * Constants.SIZEOF_FLOAT, 2 * Constants.SIZEOF_FLOAT);
		GL11.glDrawArrays(GL11.GL_QUADS, 0, 8);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
	}

	public void dispose() {
		vbo = -1;
	}
	
	/**
	 * Creates a vertex buffer object ex nihilo.
	 * @return the id of the new vbo.
	 */
	private int createVBO() {
		IntBuffer vbof = BufferUtils.createIntBuffer(1);
		GL15.glGenBuffers(vbof);
		return vbof.get(0);
	}
	
	/**
	 * Buffers data into the specified vbo.
	 * @param id the id of the vbo
	 * @param buf the data to buffer into it
	 */
	private void vboBufferData(int id) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, 4 * 6 * Constants.SIZEOF_FLOAT, GL15.GL_DYNAMIC_DRAW);
	}
}
