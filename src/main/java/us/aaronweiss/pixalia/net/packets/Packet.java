package us.aaronweiss.pixalia.net.packets;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public abstract class Packet {
	protected byte opcode;
	protected ByteBuf buffer;
	protected PacketType packetType;
	
	protected Packet(byte opcode) {
		this.createBuffer();
		this.buffer.writeByte(opcode);
		this.opcode = opcode;
	}

	protected void trim() {
		this.buffer.capacity(this.buffer.writerIndex());
	}

	private void createBuffer() {
		this.buffer = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);
	}
	
	protected void ready() {
		this.buffer.resetReaderIndex();
		this.buffer.readByte();
	}

	public byte opcode() {
		return this.opcode;
	}

	public byte[] array() {
		return this.buffer.array();
	}
	
	public ByteBuf buf() {
		return this.buffer;
	}
	
	public PacketType type() {
		return this.packetType;
	}
	
	public static enum PacketType {
		INBOUND(1),
		OUTBOUND(2);
		private final int value;
		
		private PacketType(int value) {
			this.value = value;
		}
		
		public boolean is(PacketType pt) {
			return this.value == pt.value;
		}
	}
}
