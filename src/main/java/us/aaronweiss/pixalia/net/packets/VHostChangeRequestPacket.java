package us.aaronweiss.pixalia.net.packets;

public class VHostChangeRequestPacket extends Packet {
	public static final byte OPCODE = 0x04;
	
	private VHostChangeRequestPacket(boolean status) {
		super(OPCODE);
		this.packetType = PacketType.INBOUND;
		this.buffer.writeBoolean(status);
	}
	
	private VHostChangeRequestPacket(String desiredVHost) {
		super(OPCODE);
		this.packetType = PacketType.OUTBOUND;
		this.buffer.writeByte(desiredVHost.getBytes().length);
		this.buffer.writeBytes(desiredVHost.getBytes());
	}
	
	public static VHostChangeRequestPacket newInboundPacket(boolean status) {
		return new VHostChangeRequestPacket(status);
	}
	
	public static VHostChangeRequestPacket newOutboundPacket(String desiredVHost) {
		return new VHostChangeRequestPacket(desiredVHost);
	}
}
