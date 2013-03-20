package us.aaronweiss.pixalia.net.packets;

import us.aaronweiss.pixalia.tools.Utils;

public class MessagePacket extends Packet {
	public static final byte OPCODE = 0x03;
	
	private MessagePacket(String hostname, String message) {
		super(OPCODE);
		this.packetType = PacketType.INBOUND;
		this.buffer.writeByte(hostname.getBytes().length);
		this.buffer.writeBytes(hostname.getBytes());
		this.buffer.writeByte(message.getBytes().length);
		this.buffer.writeBytes(message.getBytes());
	}
	
	private MessagePacket(String message) {
		super(OPCODE);
		this.packetType = PacketType.OUTBOUND;
		this.buffer.writeByte(message.getBytes().length);
		this.buffer.writeBytes(message.getBytes());
	}
	
	public String hostname() {
		if (this.packetType.is(PacketType.INBOUND)) {
			this.ready();
			return Utils.readString(this.buffer.readInt(), this.buffer);
		}
		return null;
	}
	
	public String message() {
		this.ready();
		if (this.packetType.is(PacketType.INBOUND)) {
			this.buffer.readBytes(this.buffer.readInt());
		}
		return Utils.readString(this.buffer.readInt(), this.buffer);
	}
	
	public static MessagePacket newInboundPacket(String hostname, String message) {
		return new MessagePacket(hostname, message);
	}
	
	public static MessagePacket newOutboundPacket(String message) {
		return new MessagePacket(message);
	}
}
