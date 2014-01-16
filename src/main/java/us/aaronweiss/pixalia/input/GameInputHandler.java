package us.aaronweiss.pixalia.input;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.aaronweiss.pixalia.core.Game;
import us.aaronweiss.pixalia.core.Network;
import us.aaronweiss.pixalia.core.Player;
import us.aaronweiss.pixalia.core.UI;
import us.aaronweiss.pixalia.net.packets.MessagePacket;
import us.aaronweiss.pixalia.net.packets.MovementPacket;
import us.aaronweiss.pixalia.tools.Configuration;
import us.aaronweiss.pixalia.tools.Constants;
import us.aaronweiss.pixalia.tools.Vector;

public class GameInputHandler {
    private static final Logger logger = LoggerFactory.getLogger(GameInputHandler.class);
	private boolean jtChat = false, jtUtil = false, jtSend = false;
	private Vector lastPos;
	private float ticksSinceUpdate = 0;
	private final TextInputHandler chatInput;
	private final Player player;
	private final Network network;
	private final UI ui;

	public GameInputHandler(Game game) throws LWJGLException, IOException {
		this.chatInput = new TextInputHandler();
		this.player = game.getPlayer();
		this.network = game.getNetwork();
		this.ui = game.getUI();
		lastPos = this.player.getPosition();
	}
	
	public void controlChat(float ticksPassed) {
		while (Keyboard.next()) {
			if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
				if (!jtSend && this.chatInput.getText().isEmpty() || this.chatInput.getText().equalsIgnoreCase(" ")) {
					this.ui.toggleChat();
				} else {
					if (Configuration.offlineMode()) {
						this.offlineChat(ticksPassed);
					} else {
						this.onlineChat(ticksPassed);
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
	
	private void onlineChat(float ticksPassed) {
		String message = this.chatInput.getText();
		if (!message.equals(" ") && !message.equals("")) {
			logger.debug(this.player.getHostname() + ": " + message);
			network.write(MessagePacket.newOutboundPacket(this.chatInput.getText()));
			this.chatInput.reset();
			this.jtSend = true;
		}
	}
	
	private void offlineChat(float ticksPassed) {
		String message = this.chatInput.getText();
		if (!message.equals(" ")) {
			logger.debug(this.player.getHostname() + ": " + message);
			this.ui.addChatLine(this.player.getHostname(), message);
			this.chatInput.reset();
			this.jtSend = true;
		}
	}
	
	public void controlGame(float ticksPassed) {
		this.ticksSinceUpdate += ticksPassed;
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
		if (!this.player.getPosition().equals(this.lastPos) && !Configuration.offlineMode() && this.ticksSinceUpdate >= Constants.TICKS_FOR_MOVEMENT_UPDATE) {
			network.write(MovementPacket.newOutboundPacket(this.player.getPosition()));
			this.lastPos = this.player.getPosition();
			this.ticksSinceUpdate = 0;
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
