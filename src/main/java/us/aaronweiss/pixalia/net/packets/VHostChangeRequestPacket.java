package us.aaronweiss.pixalia.net.packets;

import us.aaronweiss.pixalia.tools.Utils;

public class VHostChangeRequestPacket extends Packet {
	public static final byte OPCODE = 0x04;
	
	private VHostChangeRequestPacket(boolean status) {
		super(OPCODE);
		this.packetType = PacketType.INBOUND;
		this.buffer.writeBoolean(status);
		trim();
	}
	
	private VHostChangeRequestPacket(String desiredVHost) {
		super(OPCODE);
		this.packetType = PacketType.OUTBOUND;
		this.buffer.writeByte(desiredVHost.getBytes().length);
		this.buffer.writeBytes(desiredVHost.getBytes());
		trim();
	}
	
	public boolean status() {
		this.ready();
		if (this.packetType.is(PacketType.INBOUND)) {
			return this.buffer.readBoolean();
		}
		return false;
	}
	
	public String virtualHost() {
		this.ready();
		if (this.packetType.is(PacketType.OUTBOUND)) {
			return Utils.readString(this.buffer.readInt(), this.buffer);
		}
		return null;
	}
	
	public static VHostChangeRequestPacket newInboundPacket(boolean status) {
		return new VHostChangeRequestPacket(status);
	}
	
	public static VHostChangeRequestPacket newOutboundPacket(String desiredVHost) {
		return new VHostChangeRequestPacket(desiredVHost);
	}
}
