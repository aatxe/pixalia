package us.aaronweiss.pixalia.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.aaronweiss.pixalia.net.listeners.HandshakeListener;
import us.aaronweiss.pixalia.net.listeners.MessageListener;
import us.aaronweiss.pixalia.net.listeners.MovementListener;
import us.aaronweiss.pixalia.net.packets.Packet;
import us.aaronweiss.pixalia.tools.Constants;

public class Network {
	private static final Logger logger = LoggerFactory.getLogger(Network.class);

	public Network(Game game) {
		// TODO: register moar listeners
		Game.getEventBus().register(new HandshakeListener(game)); // 0x01
		Game.getEventBus().register(new MovementListener(game)); // 0x02
		Game.getEventBus().register(new MessageListener(game)); // 0x03
		logger.info("Registered listeners with event bus.");
	}
	
	public void write(Packet packet) {
		if (Constants.OFFLINE_MODE)
			return;
	}
}
