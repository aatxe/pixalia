package us.aaronweiss.pixalia.net.packets;

import us.aaronweiss.pixalia.tools.Vector;

public class MovementPacket extends Packet {
	public static final byte OPCODE = 0x02;

	private MovementPacket(Vector position) {
		super(OPCODE);
		this.packetType = PacketType.OUTBOUND;
		this.buffer.writeBytes(position.asByteBuf(2));
	}
	
	private MovementPacket(String hostname, Vector position) {
		super(OPCODE);
		this.packetType = PacketType.INBOUND;
		this.buffer.writeByte(hostname.getBytes().length);
		this.buffer.writeBytes(hostname.getBytes());
		this.buffer.writeBytes(position.asByteBuf(2));
	}
	
	public static MovementPacket newInboundPacket(String hostname, Vector position) {
		return new MovementPacket(hostname, position);
	}
	
	public static MovementPacket newOutboundPacket(Vector position) {
		return new MovementPacket(position);
	}
}
