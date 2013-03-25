package us.aaronweiss.pixalia.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToByteEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.aaronweiss.pixalia.tools.Utils;

public class BufferLengthEncoder extends ByteToByteEncoder {
	private static final Logger logger = LoggerFactory.getLogger(BufferLengthEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
		logger.info(Utils.toHexString(in.array()));
		out = out.writeByte(in.readableBytes()).writeBytes(in);
	}
}
