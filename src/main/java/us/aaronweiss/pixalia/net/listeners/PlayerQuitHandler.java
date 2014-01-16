package us.aaronweiss.pixalia.net.listeners;

import us.aaronweiss.pixalia.core.Game;
import us.aaronweiss.pixalia.net.packets.Packet;
import us.aaronweiss.pixalia.net.packets.PlayerQuitPacket;

public class PlayerQuitHandler extends PacketHandler {
	public static final byte OPCODE = PlayerQuitPacket.OPCODE;

	public PlayerQuitHandler(Game game) {
		super(game);
	}

	@Override
	public void process(Packet event) {
		PlayerQuitPacket in = (PlayerQuitPacket) event;
		game.getWorld().remove(in.hostname());
	}
}
