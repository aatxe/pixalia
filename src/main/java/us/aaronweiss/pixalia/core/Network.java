package us.aaronweiss.pixalia.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Network {
    private static final Logger logger = LoggerFactory.getLogger(Network.class);
    
	public Network(Game game) {
		// TODO: register listeners
		Game.getEventBus().register(null);
		logger.info("Registered listeners with event bus.");
	}
}
