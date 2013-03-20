package us.aaronweiss.pixalia.net.listeners;

import us.aaronweiss.pixalia.core.Game;
import us.aaronweiss.pixalia.core.UI;
import us.aaronweiss.pixalia.net.packets.MessagePacket;

import com.google.common.eventbus.Subscribe;

public class MessageListener {
	private final UI ui;
	
	public MessageListener(Game game) {
		this.ui = game.getUI();
	}

	@Subscribe
	public void newMessage(MessagePacket in) {
		ui.addChatLine(in.hostname(), in.message());
	}
}
