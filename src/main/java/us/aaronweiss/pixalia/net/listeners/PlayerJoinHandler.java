package us.aaronweiss.pixalia.net.listeners;

import us.aaronweiss.pixalia.core.Game;
import us.aaronweiss.pixalia.core.Pixal;
import us.aaronweiss.pixalia.net.packets.Packet;
import us.aaronweiss.pixalia.net.packets.PlayerJoinPacket;

public class PlayerJoinHandler extends PacketHandler {
	public static final byte OPCODE = PlayerJoinPacket.OPCODE;

	public PlayerJoinHandler(Game game) {
		super(game);
	}

	@Override
	public void process(Packet event) {
		PlayerJoinPacket in = (PlayerJoinPacket) event;
		Pixal p;
		if (in.position() != null) {
			p = new Pixal(in.hostname(), in.position(), in.playerColor());
		} else {
			p = new Pixal(in.hostname(), in.playerColor());
		}
		game.getWorld().put(p.getHostname(), p);
	}
}
