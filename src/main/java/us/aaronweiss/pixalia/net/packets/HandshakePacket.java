package us.aaronweiss.pixalia.net.packets;

import us.aaronweiss.pixalia.tools.Utils;
import us.aaronweiss.pixalia.tools.Vector;

public class HandshakePacket extends Packet {
	public static final byte OPCODE = 0x01;
	private final boolean hasVhost;
	
	private HandshakePacket(boolean status, Vector playerColor) {
		super(OPCODE);
		this.packetType = PacketType.INBOUND;
		this.buffer.writeBoolean(status);
		this.buffer.writeBytes(playerColor.asByteBuf(4));
		this.hasVhost = false;
		trim();
	}
	
	private HandshakePacket(String username) {
		super(OPCODE);
		this.packetType = PacketType.OUTBOUND;
		this.buffer.writeByte(username.getBytes().length);
		this.buffer.writeBytes(username.getBytes());
		this.hasVhost = false;
		trim();
	}
	
	private HandshakePacket(String username, String virtualHost) {
		super(OPCODE);
		this.packetType = PacketType.OUTBOUND;
		this.buffer.writeByte(username.getBytes().length);
		this.buffer.writeBytes(username.getBytes());
		this.buffer.writeByte(virtualHost.getBytes().length);
		this.buffer.writeBytes(virtualHost.getBytes());
		this.hasVhost = true;
		trim();
	}
	
	public boolean status() {
		this.ready();
		if (this.packetType.is(PacketType.INBOUND)) {
			return this.buffer.readBoolean();
		}
		return false;
	}
	
	public Vector color() {
		this.ready();
		if (this.packetType.is(PacketType.INBOUND)) {
			this.buffer.readBoolean();
			return Vector.fromByteBuf(4, this.buffer);
		}
		return null;
	}
	
	public String virtualHost() {
		this.ready();
		if (this.packetType.is(PacketType.OUTBOUND) && this.hasVhost) {
			return Utils.readString(this.buffer.readInt(), this.buffer);
		}
		return null;
	}
	
	public boolean hasVirtualHost() {
		return this.hasVhost;
	}
	
	public static HandshakePacket newInboundPacket(boolean status, Vector playerColor) {
		return new HandshakePacket(status, playerColor);
	}
	
	public static HandshakePacket newOutboundPacket(String username) {
		return new HandshakePacket(username);
	}
	
	public static HandshakePacket newOutboundPacket(String username, String virtualHost) {
		return new HandshakePacket(username, virtualHost);
	}
}
