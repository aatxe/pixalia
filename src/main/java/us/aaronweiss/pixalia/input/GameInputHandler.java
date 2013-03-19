package us.aaronweiss.pixalia.input;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import us.aaronweiss.pixalia.core.Pixal;
import us.aaronweiss.pixalia.core.UI;
import us.aaronweiss.pixalia.tools.Constants;

public class GameInputHandler {
	private boolean jtChat = false, jtUtil = false, jtSend = false;
	private final TextInputHandler chatInput;
	private final Pixal player;
	private final UI ui;

	public GameInputHandler(Pixal player, UI ui) throws LWJGLException, IOException {
		this.chatInput = new TextInputHandler();
		this.player = player;
		this.ui = ui;
	}
	
	public void controlChat(float ticksPassed) {
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
	
	public void controlGame(float ticksPassed) {
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
}
