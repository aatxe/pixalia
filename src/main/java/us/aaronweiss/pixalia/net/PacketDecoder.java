package us.aaronweiss.pixalia.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundByteHandlerAdapter;

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

public class PacketDecoder extends ChannelInboundByteHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(PacketDecoder.class);
    
	@Override
	protected void inboundBufferUpdated(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		byte opcode = in.readByte();
		Packet event;
		logger.info("Packet recieved. (Opcode: " + opcode + ")");
		switch (opcode) {
		case HandshakePacket.OPCODE:
			event = HandshakePacket.newInboundPacket(in.readBoolean(), Vector.fromByteBuf(4, in));
			break;
		case MovementPacket.OPCODE:
			event = MovementPacket.newInboundPacket(Utils.readString(in.readInt(), in), Vector.fromByteBuf(2, in));
			break;
		case MessagePacket.OPCODE:
			event = MessagePacket.newInboundPacket(Utils.readString(in.readInt(), in), Utils.readString(in.readInt(), in));
			break;
		case VHostChangeRequestPacket.OPCODE:
			event = VHostChangeRequestPacket.newInboundPacket(in.readBoolean());
			break;
		case VHostChangePacket.OPCODE:
			event = VHostChangePacket.newInboundPacket(Utils.readString(in.readInt(), in), Utils.readString(in.readInt(), in));
			break;
		default:
			logger.debug("Unexpected packet recieved. (Opcode: " + opcode + ")");
			event = null;
		}
		if (event != null)
			Game.getEventBus().post(event);
	}
}
