package us.aaronweiss.pixalia.net.packets;

import us.aaronweiss.pixalia.tools.Utils;

public class VHostChangePacket extends Packet {
	public static final byte OPCODE = 0x05;
	
	private VHostChangePacket(String hostname, String newHostname) {
		super(OPCODE);
		this.packetType = PacketType.INBOUND;
		this.buffer.writeByte(hostname.getBytes().length);
		this.buffer.writeBytes(hostname.getBytes());
		this.buffer.writeByte(newHostname.getBytes().length);
		this.buffer.writeBytes(newHostname.getBytes());
	}
	
	public static VHostChangePacket newInboundPacket(String hostname, String newHostname) {
		return new VHostChangePacket(hostname, newHostname);
	}
	
	public String hostname() {
		if (this.packetType.is(PacketType.INBOUND)) {
			this.ready();
			return Utils.readString(this.buffer.readInt(), this.buffer);
		}
		return null;
	}
	
	public String newHostname() {
		this.ready();
		if (this.packetType.is(PacketType.INBOUND)) {
			Utils.readString(this.buffer.readInt(), this.buffer);
			return Utils.readString(this.buffer.readInt(), this.buffer);
		}
		return null;
	}
}
