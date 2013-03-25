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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.aaronweiss.pixalia.net.BufferLengthDecoder;
import us.aaronweiss.pixalia.net.BufferLengthEncoder;
import us.aaronweiss.pixalia.net.PacketDecoder;
import us.aaronweiss.pixalia.net.PacketEncoder;
import us.aaronweiss.pixalia.net.listeners.HandshakeListener;
import us.aaronweiss.pixalia.net.listeners.MessageListener;
import us.aaronweiss.pixalia.net.listeners.MovementListener;
import us.aaronweiss.pixalia.net.packets.Packet;
import us.aaronweiss.pixalia.tools.Configuration;

public class Network {
	private static final Logger logger = LoggerFactory.getLogger(Network.class);
	private final Bootstrap bootstrap;
	private Channel session;
	
	public Network(Game game) {
		Game.getEventBus().register(new HandshakeListener(game)); // 0x01
		Game.getEventBus().register(new MovementListener(game)); // 0x02
		Game.getEventBus().register(new MessageListener(game)); // 0x03
		// TODO: register moar listeners (virtual host support)
		logger.info("Registered listeners with event bus.");
		bootstrap = new Bootstrap();
		bootstrap.group(new NioEventLoopGroup());
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				// Decoders
				pipeline.addLast("buffer_length_decoder", new BufferLengthDecoder());
				pipeline.addLast("packet_decoder", new PacketDecoder());
				
				// Encoder
				pipeline.addLast("buffer_length_encoder", new BufferLengthEncoder());
				pipeline.addLast("packet_encoder", new PacketEncoder());
			}
		});
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		logger.info("Configured networking pipeline, ready to connect.");
	}
	
	public void connect(InetSocketAddress server) throws ChannelException {
		if (this.session != null) {
			logger.error("Network session already in progress.");
			logger.error("session: " + this.session.toString() + " - " + this.session.remoteAddress().toString());
			throw new ChannelException("Network session already in progress.");
		}
		ChannelFuture cf = this.bootstrap.connect(server);
		cf.syncUninterruptibly();
		this.session = cf.channel();
		logger.info("Connected to " + server.getHostName() + ":" + server.getPort());
	}
	
	public void connect(String server, int port) throws ChannelException {
		this.connect(new InetSocketAddress(server, port));
	}
	
	public void write(Packet packet) {
		if (Configuration.offlineMode())
			return;
		logger.info("Packet written.");
		try {
			this.session.write(packet);
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
