package us.aaronweiss.pixalia.net.listeners;

import us.aaronweiss.pixalia.core.Game;
import us.aaronweiss.pixalia.core.Pixal;
import us.aaronweiss.pixalia.core.Player;
import us.aaronweiss.pixalia.core.World;
import us.aaronweiss.pixalia.net.packets.MovementPacket;
import us.aaronweiss.pixalia.net.packets.Packet;

public class MovementHandler extends PacketHandler {
	public static final byte OPCODE = MovementPacket.OPCODE;
	private final World world;
	private final Player player;

	public MovementHandler(Game game) {
		super(game.getNetwork());
		this.world = game.getWorld();
		player = game.getPlayer();
	}

	@Override
	public void process(Packet event) {
		MovementPacket in = (MovementPacket) event;
		Pixal p = this.world.get(in.hostname());
		if (p == null)
			p = player;
		p.setPosition(in.position());
		this.world.put(in.hostname(), p);
	}
}
