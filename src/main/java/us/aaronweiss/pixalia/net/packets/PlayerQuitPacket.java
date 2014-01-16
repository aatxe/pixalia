package us.aaronweiss.pixalia.net.packets;

import us.aaronweiss.pixalia.tools.Utils;

public class PlayerQuitPacket extends Packet {
	public static final byte OPCODE = 0x07;

	protected PlayerQuitPacket() {
		super(OPCODE);
		this.packetType = PacketType.OUTBOUND;
		trim();
	}

	protected PlayerQuitPacket(String hostname) {
		super(OPCODE);
		this.packetType = PacketType.INBOUND;
		this.buffer.writeByte(hostname.getBytes().length);
		this.buffer.writeBytes(hostname.getBytes());
		trim();
	}

	public static PlayerQuitPacket newOutboundPacket() {
		return new PlayerQuitPacket();
	}

	public static PlayerQuitPacket newInboundPacket(String hostname) {
		return new PlayerQuitPacket(hostname);
	}

	public String hostname() {
		this.ready();
		if (this.packetType.is(PacketType.INBOUND)) {
			return Utils.readString(this.buffer.readByte(), this.buffer);
		}
		return null;
	}
}
