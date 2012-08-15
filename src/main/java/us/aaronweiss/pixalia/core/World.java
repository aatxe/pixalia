package us.aaronweiss.pixalia.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import us.aaronweiss.pixalia.lwjgl.Renderable;
import us.aaronweiss.pixalia.tools.Constants;

public class World extends HashMap<String, Pixal> implements Renderable {
	private static final long serialVersionUID = 3713274279007718811L;
	private int vbo = -1;
	
	public void render() {
		GL11.glClearColor(0.4f, 0.6f, 0.9f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		if (this.size() > 0) {
			if (vbo <= 0) {
				vbo = this.createVBO();
				this.vboBufferData(vbo);
			}
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
			ByteBuffer buffer = GL15.glMapBuffer(GL15.GL_ARRAY_BUFFER, GL15.GL_WRITE_ONLY, null);
			FloatBuffer buf = buffer.order(ByteOrder.nativeOrder()).asFloatBuffer();
			for (Pixal pixal : this.values()) {
				buf.put(pixal.getPosition().asFloatArray(2));
				buf.put(pixal.getColor().asFloatArray(4));
			}
			GL15.glUnmapBuffer(GL15.GL_ARRAY_BUFFER);
			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
			GL11.glVertexPointer(2, GL11.GL_FLOAT, 6 * Constants.SIZEOF_FLOAT, 0);
			GL11.glColorPointer(4, GL11.GL_FLOAT, 6 * Constants.SIZEOF_FLOAT, 2 * Constants.SIZEOF_FLOAT);
			GL11.glPointSize(8);
			GL11.glDrawArrays(GL11.GL_POINTS, 0, this.size());
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
		}
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
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, this.size() * 6 * Constants.SIZEOF_FLOAT, GL15.GL_DYNAMIC_DRAW);
	}

	@Override
	public Pixal put(String key, Pixal value) {
		vbo = -1;
		return super.put(key, value);
	}
	
}
