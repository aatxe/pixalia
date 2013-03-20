package us.aaronweiss.pixalia.net.listeners;

import us.aaronweiss.pixalia.core.Game;
import us.aaronweiss.pixalia.core.Pixal;
import us.aaronweiss.pixalia.core.World;
import us.aaronweiss.pixalia.net.packets.MovementPacket;

import com.google.common.eventbus.Subscribe;

public class MovementListener {
	private final World world;
	
	public MovementListener(Game game) {
		this.world = game.getWorld();
	}
	
	@Subscribe
	public void positionUpdate(MovementPacket in) {
		Pixal p = this.world.get(in.hostname());
		p.setPosition(in.position());
		this.world.put(in.hostname(), p);
	}
}
