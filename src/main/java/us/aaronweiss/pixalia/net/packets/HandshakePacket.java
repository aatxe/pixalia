package us.aaronweiss.pixalia.net.packets;

import us.aaronweiss.pixalia.tools.Vector;

public class HandshakePacket extends Packet {
	public static final byte OPCODE = 0x01;
	
	private HandshakePacket(boolean status, Vector playerColor) {
		super(OPCODE);
		this.packetType = PacketType.INBOUND;
		this.buffer.writeBoolean(status);
		this.buffer.writeBytes(playerColor.asByteBuf(4));
	}
	
	private HandshakePacket() {
		super(OPCODE);
		this.packetType = PacketType.OUTBOUND;
	}
	
	private HandshakePacket(String virtualHost) {
		super(OPCODE);
		this.packetType = PacketType.OUTBOUND;
		this.buffer.writeByte(virtualHost.getBytes().length);
		this.buffer.writeBytes(virtualHost.getBytes());
	}
	
	public static HandshakePacket newInboundPacket(boolean status, Vector playerColor) {
		return new HandshakePacket(status, playerColor);
	}
	
	public static HandshakePacket newOutboundPacket() {
		return new HandshakePacket();
	}
	
	public static HandshakePacket newOutboundPacket(String virtualHost) {
		return new HandshakePacket(virtualHost);
	}
}
