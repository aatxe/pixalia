package us.aaronweiss.pixalia.lwjgl;

import java.util.concurrent.TimeUnit;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Util;
import us.aaronweiss.pixalia.tools.Constants;
import com.google.common.base.Stopwatch;

public abstract class Window {
	protected final int width, height;
	protected boolean isRunning = false;
	private int fps = 0;
	private int frameCount = 0;
	
	public Window(int width, int height) throws LWJGLException {
		this.width = width;
		this.height = height;
		Display.setDisplayMode(new DisplayMode(width, height));
		Display.create();
		Keyboard.create();
	}
	
	public void setTitle(String title) { 
		Display.setTitle(title);
	}
	
	public abstract void init();
	
	public abstract void update(float ticksPassed);
	
	public abstract void render();
	
	public abstract void dispose();
	
	public void open() {
		this.init();
		Util.checkGLError();
		Display.update();
		isRunning = true;
		this.run();
	}
	
	protected void run() {
		Stopwatch fpsTimer = new Stopwatch();
		Stopwatch updateTimer = new Stopwatch();
		fpsTimer.start();
		while (isRunning && !Display.isCloseRequested()) {
			this.update((updateTimer.elapsedMillis() / 1000f) * Constants.TICKS_PER_SECOND);
			updateTimer.reset();
			updateTimer.start();
			this.render();
			frameCount++;
			if (fpsTimer.elapsedTime(TimeUnit.SECONDS) >= 1) {
				fps = frameCount;
				frameCount = 0;
				fpsTimer.reset();
				fpsTimer.start();
			}
			Util.checkGLError();
			Display.update();
			Display.sync(60);
		}
		fpsTimer.stop();
		this.dispose();
		Display.destroy();
	}
	
	public int getFramesPerSecond() {
		return this.fps;
	}
	
	public void close() {
		isRunning = false;
	}
}
