package us.aaronweiss.pixalia.net.listeners;

import us.aaronweiss.pixalia.core.Game;
import us.aaronweiss.pixalia.core.Player;
import us.aaronweiss.pixalia.net.packets.HandshakePacket;

import com.google.common.eventbus.Subscribe;

public class HandshakeListener {
	private final Player player;
	private final Game game;
	
	public HandshakeListener(Game game) {
		this.game = game;
		this.player = game.getPlayer();
	}
	
	@Subscribe
	public void preparePlayer(HandshakePacket in) {
		if (!in.status())
			this.game.close();
		this.player.setColor(in.color());
	}
}
