package us.aaronweiss.pixalia.net.listeners;

import us.aaronweiss.pixalia.core.Game;
import us.aaronweiss.pixalia.core.Network;
import us.aaronweiss.pixalia.net.packets.Packet;

public abstract class PacketHandler {
	protected final Game game;

	public PacketHandler(Game game) {
		this.game = game;
	}

	public abstract void process(Packet event);
}
