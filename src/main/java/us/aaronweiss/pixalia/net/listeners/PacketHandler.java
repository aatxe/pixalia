package us.aaronweiss.pixalia.net.listeners;

import us.aaronweiss.pixalia.core.Network;
import us.aaronweiss.pixalia.net.packets.Packet;

public abstract class PacketHandler {
	protected final Network network;

	public PacketHandler(Network network) {
		this.network = network;
	}

	public abstract void process(Packet event);
}
