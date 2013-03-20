package us.aaronweiss.pixalia.core;

import us.aaronweiss.pixalia.lwjgl.Renderable;
import us.aaronweiss.pixalia.tools.Vector;
import us.aaronweiss.pixalia.ui.Chat;
import us.aaronweiss.pixalia.ui.ChatNotifier;
import us.aaronweiss.pixalia.ui.FramerateNotifier;
import us.aaronweiss.pixalia.ui.PositionNotifier;

public class UI implements Renderable {
	private Chat chat;
	private ChatNotifier chatNotifier;
	private FramerateNotifier fpsNotifier;
	private PositionNotifier positionNotifier;
	
	public UI(int width, int height) {
		chat = new Chat(width);
		chatNotifier = new ChatNotifier();
		fpsNotifier = new FramerateNotifier(width, height);
		positionNotifier = new PositionNotifier(height);
	}
	
	public boolean isChatOpen() {
		return chat.isVisible();
	}

	public void toggleChat() {
		chat.toggleVisibility();
		if (chat.isVisible())
			chatNotifier.messageRead();
	}
	
	public void toggleFPS() {
		fpsNotifier.toggleVisibility();
	}
	
	public void togglePosition() {
		positionNotifier.toggleVisibility();
	}
	
	public void render() {
		if (chat.isVisible()) {
			chat.render();
		} else {
			chatNotifier.render();
		}
		if (fpsNotifier.isVisible()) {
			fpsNotifier.render();
		}
		if (positionNotifier.isVisible()) {
			positionNotifier.render();
		}
	}
	
	public void update(float ticksPassed, int fps, Vector position) {
		if (fpsNotifier.isVisible()) {
			fpsNotifier.update(fps);
		}
		if (positionNotifier.isVisible()) {
			positionNotifier.update(ticksPassed, position);
		}
	}
	
	public void updateChat(String chatText) {
		if (chat.isVisible()) {
			chat.update(chatText);
		}
	}
	
	public void addChatLine(String speaker, String line) {
		chat.addChatLine(speaker, line);
		if (!chat.isVisible())
			chatNotifier.newMessage();
	}

	public void dispose() {
		chat.dispose();
		chatNotifier.dispose();
	}

}
