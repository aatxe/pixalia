package us.aaronweiss.pixalia.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundMessageHandlerAdapter;

public class BufferLengthEncoder extends ChannelOutboundMessageHandlerAdapter<ByteBuf> {
	@Override
	protected void flush(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		ctx.nextOutboundByteBuffer().writeByte(msg.readableBytes()).writeBytes(msg);
	}
}
