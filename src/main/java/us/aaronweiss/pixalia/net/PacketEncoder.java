package us.aaronweiss.pixalia.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.aaronweiss.pixalia.net.packets.Packet;
import us.aaronweiss.pixalia.tools.Utils;

public class PacketEncoder extends MessageToByteEncoder<Packet> {
	private static final Logger logger = LoggerFactory.getLogger(PacketEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
		ByteBuf buf = msg.buf();
		buf = buf.capacity(buf.readableBytes());
		logger.info(Utils.toHexString(buf.array()));
		out.writeBytes(buf);
	}
}
