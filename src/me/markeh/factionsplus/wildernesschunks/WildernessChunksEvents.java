package me.markeh.factionsplus.wildernesschunks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class WildernessChunksEvents implements Listener {

	private static WildernessChunksEvents instance = null;
	public static WildernessChunksEvents get() {
		if (instance == null) instance = new WildernessChunksEvents();
		
		return instance;
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		WildernessChunks.get().logChunk(event.getBlock().getChunk());
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		WildernessChunks.get().logChunk(event.getBlock().getChunk());
	}
	
}
