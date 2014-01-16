package us.aaronweiss.pixalia.net.packets;


import us.aaronweiss.pixalia.tools.Utils;
import us.aaronweiss.pixalia.tools.Vector;

public class PlayerJoinPacket extends Packet {
	public static final byte OPCODE = 0x06;

	private PlayerJoinPacket(String hostname, Vector playerColor) {
		super(OPCODE);
		this.packetType = PacketType.INBOUND;
		this.buffer.writeByte(hostname.getBytes().length);
		this.buffer.writeBytes(hostname.getBytes());
		this.buffer.writeBytes(playerColor.asByteBuf(4));
		trim();
	}

	private PlayerJoinPacket(String hostname, Vector playerColor, Vector position) {
		super(OPCODE);
		this.packetType = PacketType.INBOUND;
		this.buffer.writeByte(hostname.getBytes().length);
		this.buffer.writeBytes(hostname.getBytes());
		this.buffer.writeBytes(playerColor.asByteBuf(4));
		this.buffer.writeBytes(position.asByteBuf(2));
		trim();
	}

	public static PlayerJoinPacket newInboundPacket(String hostname, Vector playerColor) {
		return new PlayerJoinPacket(hostname, playerColor);
	}

	public static PlayerJoinPacket newInboundPacket(String hostname, Vector playerColor, Vector position) {
		return new PlayerJoinPacket(hostname, playerColor, position);
	}

	public String hostname() {
		if (this.packetType.is(PacketType.INBOUND)) {
			this.ready();
			return Utils.readString(this.buffer.readByte(), this.buffer);
		}
		return null;
	}

	public Vector playerColor() {
		this.ready();
		if (this.packetType.is(PacketType.INBOUND)) {
			Utils.readString(this.buffer.readByte(), this.buffer);
			return Vector.fromByteBuf(4, this.buffer);
		}
		return null;
	}

	public Vector position() {
		this.ready();
		if (this.packetType.is(PacketType.INBOUND)) {
			Utils.readString(this.buffer.readByte(), this.buffer);
			Vector.fromByteBuf(4, this.buffer);
			if (this.buffer.readableBytes() > 0)
				return Vector.fromByteBuf(2, this.buffer);
		}
		return null;
	}
}
