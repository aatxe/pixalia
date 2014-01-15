package us.aaronweiss.pixalia.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.aaronweiss.pixalia.core.Game;
import us.aaronweiss.pixalia.net.packets.HandshakePacket;
import us.aaronweiss.pixalia.net.packets.MessagePacket;
import us.aaronweiss.pixalia.net.packets.MovementPacket;
import us.aaronweiss.pixalia.net.packets.Packet;
import us.aaronweiss.pixalia.net.packets.VHostChangePacket;
import us.aaronweiss.pixalia.net.packets.VHostChangeRequestPacket;
import us.aaronweiss.pixalia.tools.Utils;
import us.aaronweiss.pixalia.tools.Vector;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(PacketDecoder.class);

	@Override
	public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
		byte opcode = in.readByte();
		Packet event = null;
		logger.info("Packet recieved. (Opcode: " + opcode + ")");
		switch (opcode) {
		case HandshakePacket.OPCODE:
			event = HandshakePacket.newInboundPacket(in.readBoolean(), Vector.fromByteBuf(4, in));
			break;
		case MovementPacket.OPCODE:
			event = MovementPacket.newInboundPacket(Utils.readString(in.readByte(), in), Vector.fromByteBuf(2, in));
			break;
		case MessagePacket.OPCODE:
			event = MessagePacket.newInboundPacket(Utils.readString(in.readByte(), in), Utils.readString(in.readInt(), in));
			break;
		case VHostChangeRequestPacket.OPCODE:
			event = VHostChangeRequestPacket.newInboundPacket(in.readBoolean());
			break;
		case VHostChangePacket.OPCODE:
			event = VHostChangePacket.newInboundPacket(Utils.readString(in.readByte(), in), Utils.readString(in.readByte(), in));
			break;
		default:
			logger.error("Unexpected packet recieved. (Opcode: " + opcode + ")");
		}
		if (event != null)
			out.add(event);
	}
}
