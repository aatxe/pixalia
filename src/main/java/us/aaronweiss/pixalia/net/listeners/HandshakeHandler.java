package us.aaronweiss.pixalia.net.listeners;

import us.aaronweiss.pixalia.core.Game;
import us.aaronweiss.pixalia.core.Player;
import us.aaronweiss.pixalia.net.packets.HandshakePacket;

import com.google.common.eventbus.Subscribe;
import us.aaronweiss.pixalia.net.packets.Packet;

public class HandshakeHandler extends PacketHandler {
	public static final byte OPCODE = HandshakePacket.OPCODE;

	public HandshakeHandler(Game game) {
		super(game);
	}

	@Override
	public void process(Packet event) {
		HandshakePacket in = (HandshakePacket) event;
		if (!in.status())
			this.game.close();
		game.getPlayer().setColor(in.color());
	}
}
