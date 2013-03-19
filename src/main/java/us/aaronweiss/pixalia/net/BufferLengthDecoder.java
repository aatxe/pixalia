package us.aaronweiss.pixalia.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class BufferLengthDecoder extends ByteToMessageDecoder {
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		if (in.readableBytes() < 3)
			return null;
		int length = in.readInt();
		if (in.readableBytes() < length)
			return null;
		return in.readBytes(length);
	}
}
