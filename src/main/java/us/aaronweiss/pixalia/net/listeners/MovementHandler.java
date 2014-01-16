package us.aaronweiss.pixalia.net.listeners;

import us.aaronweiss.pixalia.core.Game;
import us.aaronweiss.pixalia.core.Pixal;
import us.aaronweiss.pixalia.core.Player;
import us.aaronweiss.pixalia.core.World;
import us.aaronweiss.pixalia.net.packets.MovementPacket;
import us.aaronweiss.pixalia.net.packets.Packet;

public class MovementHandler extends PacketHandler {
	public static final byte OPCODE = MovementPacket.OPCODE;

	public MovementHandler(Game game) {
		super(game);
	}

	@Override
	public void process(Packet event) {
		MovementPacket in = (MovementPacket) event;
		Pixal p = game.getWorld().get(in.hostname());
		if (p == null)
			p = game.getPlayer();
		p.setPosition(in.position());
		game.getWorld().put(in.hostname(), p);
	}
}
