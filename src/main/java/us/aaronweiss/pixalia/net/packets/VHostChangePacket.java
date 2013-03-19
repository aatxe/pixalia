package us.aaronweiss.pixalia.net.packets;

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
}
