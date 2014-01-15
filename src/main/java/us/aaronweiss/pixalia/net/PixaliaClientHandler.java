package us.aaronweiss.pixalia.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.aaronweiss.pixalia.net.listeners.PacketHandler;
import us.aaronweiss.pixalia.net.packets.Packet;
import us.aaronweiss.pixalia.tools.Constants;
import us.aaronweiss.pixalia.tools.Utils;

public class PixaliaClientHandler extends SimpleChannelInboundHandler<Packet> {
	private static final Logger logger = LoggerFactory.getLogger(PixaliaClientHandler.class.getName());
	private PacketHandler[] handlers;

	public PixaliaClientHandler() {
		handlers = new PacketHandler[Constants.HANDLER_COUNT];
	}

	public void register(byte opcode, PacketHandler handler) {
		handlers[opcode] = handler;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) throws Exception {
		logger.info("Message received: " + Utils.toHexString(packet.array()));
		handlers[packet.opcode()].process(packet);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.warn("Unexpected exception caught from downstream.", cause);
		ctx.close();
	}
}
