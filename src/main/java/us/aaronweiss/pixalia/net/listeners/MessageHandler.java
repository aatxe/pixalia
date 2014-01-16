package us.aaronweiss.pixalia.net.listeners;

import us.aaronweiss.pixalia.core.Game;
import us.aaronweiss.pixalia.core.UI;
import us.aaronweiss.pixalia.net.packets.HandshakePacket;
import us.aaronweiss.pixalia.net.packets.MessagePacket;

import us.aaronweiss.pixalia.net.packets.Packet;

public class MessageHandler extends PacketHandler {
	public static final byte OPCODE = MessagePacket.OPCODE;
	private final UI ui;
	
	public MessageHandler(Game game) {
		super(game);
		this.ui = game.getUI();
	}

	@Override
	public void process(Packet event) {
		MessagePacket in = (MessagePacket) event;
		ui.addChatLine(in.hostname(), in.message());
	}
}
