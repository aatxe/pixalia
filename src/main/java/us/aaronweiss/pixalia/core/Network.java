package us.aaronweiss.pixalia.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.aaronweiss.pixalia.net.PacketDecoder;
import us.aaronweiss.pixalia.net.PacketEncoder;
import us.aaronweiss.pixalia.net.PixaliaClientHandler;
import us.aaronweiss.pixalia.net.listeners.*;
import us.aaronweiss.pixalia.net.packets.Packet;
import us.aaronweiss.pixalia.tools.Configuration;
import us.aaronweiss.pixalia.tools.Constants;

public class Network {
	private static final Logger logger = LoggerFactory.getLogger(Network.class);
	private final Bootstrap bootstrap;
	private Channel session;
	private final PixaliaClientHandler handler;

	public Network(Game game) {
		logger.info("Registered listeners with event bus.");
		bootstrap = new Bootstrap();
		handler = new PixaliaClientHandler();
		handler.register(HandshakeHandler.OPCODE, new HandshakeHandler(game));
		handler.register(MovementHandler.OPCODE, new MovementHandler(game));
		handler.register(MessageHandler.OPCODE, new MessageHandler(game));
		handler.register(PlayerJoinHandler.OPCODE, new PlayerJoinHandler(game));
		handler.register(PlayerQuitHandler.OPCODE, new PlayerQuitHandler(game));
		// TODO: register moar handlers with client.
		bootstrap.group(new NioEventLoopGroup())
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast("buffer_length_encoder", new LengthFieldPrepender(2));
						pipeline.addLast("buffer_length_decoder", new LengthFieldBasedFrameDecoder(Constants.MAX_FRAME_LENGTH, 0, 2, 0, 2));

						pipeline.addLast("packet_decoder", new PacketDecoder());
						pipeline.addLast("packet_encoder", new PacketEncoder());

						pipeline.addLast("handler", handler);
					}
				});
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		logger.info("Configured networking pipeline, ready to connect.");
	}

	public void connect(InetSocketAddress server) throws ChannelException, InterruptedException {
		if (this.session != null) {
			logger.error("Network session already in progress.");
			logger.error("session: " + this.session.toString() + " - " + this.session.remoteAddress().toString());
			throw new ChannelException("Network session already in progress.");
		}

		this.session = this.bootstrap.connect(server).sync().channel();
		logger.info("Connected to " + server.getHostName() + ":" + server.getPort());
	}

	public void connect(String server, int port) throws ChannelException, InterruptedException {
		this.connect(new InetSocketAddress(server, port));
	}

	public void write(Packet packet) {
		if (Configuration.offlineMode())
			return;
		logger.info("Packet written.");
		try {
			this.session.writeAndFlush(packet);
		} catch (NullPointerException e) {
			throw new IllegalStateException("Network is not connected.", e);
		}
	}

	public void disconnect() {
		SocketAddress server = this.session.remoteAddress();
		ChannelFuture cf = this.session.disconnect();
		cf.syncUninterruptibly();
		this.session = null;
		logger.info("Disconnected from " + server.toString());
	}
}
