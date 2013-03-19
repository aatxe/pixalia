package us.aaronweiss.pixalia.core;

import java.io.IOException;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.aaronweiss.pixalia.input.GameInputHandler;
import us.aaronweiss.pixalia.lwjgl.BinaryFont;
import us.aaronweiss.pixalia.lwjgl.Window;
import us.aaronweiss.pixalia.tools.Constants;
import us.aaronweiss.pixalia.tools.Utils;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

public class Game extends Window {
    private static final Logger logger = LoggerFactory.getLogger(Game.class);
	private static EventBus eventBus;
	private final Pixal player;
	private final World world;
	private final UI ui;
	private final GameInputHandler input;
	
	public Game() throws LWJGLException, IOException {
		super(800, 600);
		this.setTitle("Pixalia");
		BinaryFont.setDefault(new BinaryFont("rsc/Unifont.bin"));
		this.ui = new UI(width, height);
		this.world = new World();
		this.player = new Pixal(Utils.getLocalHostname());
		this.input = new GameInputHandler(this.player, this.ui);
		this.world.put(player.getHostname(), player);
	}

	@Override
	public void init() {
		GL11.glClearColor(0.4f, 0.6f, 0.9f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		this.setupWorldView();
	}

	@Override
	public void update(float ticksPassed) {
		this.ui.update(ticksPassed, this.getFramesPerSecond(), this.player.getPosition());
		if (this.ui.isChatOpen()) {
			this.input.controlChat(ticksPassed);
		} else {
			this.input.controlGame(ticksPassed);
		}
	}

	@Override
	public void render() {
		this.setupWorldView();
		this.world.render();
		this.setupUIView();
		this.ui.render();
	}

	@Override
	public void dispose() {
		this.world.dispose();
		this.ui.dispose();
	}
	
	private void setupWorldView() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(player.getPosition().getX() - (width / 2), player.getPosition().getX() + (width / 2), player.getPosition().getY() - (height / 2), player.getPosition().getY() + (height / 2), -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glViewport(0, 0, width, height);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
	}
	
	private void setupUIView() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, 0, height, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glViewport(0, 0, width, height);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
	}
	
	public World getWorld() {
		return this.world;
	}
	
	public UI getUI() {
		return this.ui;
	}
	
	public static EventBus getEventBus() {
		if (Game.eventBus == null) {
			int cores = Runtime.getRuntime().availableProcessors();
			ThreadPoolExecutor pool = new ThreadPoolExecutor(cores, Constants.MAX_EVENT_THREADS, Constants.EVENT_THREAD_KEEPALIVE, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<Runnable>());
			Game.eventBus = new AsyncEventBus(pool);
			logger.info("AsyncEventBus created with " + cores + " cores.");
		}
		return Game.eventBus;
	}
}
