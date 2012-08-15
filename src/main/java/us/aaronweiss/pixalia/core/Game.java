package us.aaronweiss.pixalia.core;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import us.aaronweiss.pixalia.input.TextInputHandler;
import us.aaronweiss.pixalia.lwjgl.BinaryFont;
import us.aaronweiss.pixalia.lwjgl.Window;
import us.aaronweiss.pixalia.tools.Constants;

public class Game extends Window {
	private Pixal player;
	private World world;
	private UI ui;
	private TextInputHandler chatInput;
	// jt stands for just Toggled/Typed.
	private boolean jtChat = false, jtUtil = false, jtSend = false;
	
	public Game() throws LWJGLException, IOException {
		super(800, 600);
		this.setTitle("Pixalia");
		BinaryFont.setDefault(new BinaryFont("Unifont.bin"));
		this.chatInput = new TextInputHandler();
		this.ui = new UI(width, height);
		this.world = new World();
		this.player = new Pixal("Aaron@localhost");
		this.world.put(player.getHostname(), player);
		for (int i = 0; i < 100; i++) {
			this.world.put(String.valueOf(i), new Pixal(String.valueOf(i)));
		}
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
			this.chatControls(ticksPassed);
		} else {
			this.standardControls(ticksPassed);
		}
	}
	
	private void chatControls(float ticksPassed) {
		while (Keyboard.next()) {
			if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
				if (!jtSend && this.chatInput.getText().isEmpty() || this.chatInput.getText().equalsIgnoreCase(" ")) {
					this.ui.toggleChat();
				} else {
					String message = this.player.getHostname() + ": " + this.chatInput.getText();
					if (!message.equals(this.player.getHostname() + ": ")) {
						System.out.println(message);
						this.ui.addChatLine(message);
						this.chatInput.reset();
						this.jtSend = true;
					}
				}
			} else if (!Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
				this.jtSend = false;
			} else {
				this.chatInput.update();
			}
			this.ui.updateChat(this.chatInput.getText());
		}
	}
	
	private void standardControls(float ticksPassed) {
		int rate = 48;
		float tps = Constants.TICKS_PER_SECOND;
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.player.move(0, (rate / tps) * ticksPassed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.player.move(-(rate / tps) * ticksPassed, 0);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.player.move(0, -(rate / tps) * ticksPassed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.player.move((rate / tps) * ticksPassed, 0);
		}
		while (Keyboard.next()) {
			switch (Keyboard.getEventKey()) {
				case Keyboard.KEY_T:
					if (Keyboard.getEventKeyState() && !jtChat) {
				    	this.ui.toggleChat();
				    	jtChat = true;
				    } else {
				    	jtChat = false;
				    }
					break;
				case Keyboard.KEY_U:
					if (Keyboard.getEventKeyState() && !jtUtil) {
				    	this.ui.toggleFPS();
				    	this.ui.togglePosition();
				    	jtUtil = true;
				    } else {
				    	jtUtil = false;
				    }
					break;
			}
		}
	}

	@Override
	public void render() {
		this.setupWorldView();
		world.render();
		this.setupUIView();
		ui.render();
	}

	@Override
	public void dispose() {
		world.dispose();
		ui.dispose();
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
}
