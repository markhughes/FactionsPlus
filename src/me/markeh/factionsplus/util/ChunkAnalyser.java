package me.markeh.factionsplus.util;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class ChunkAnalyser extends BukkitRunnable {

	private Chunk chunk;
	
	public ChunkAnalyser(Chunk chunk) {
		this.chunk = chunk;
	}
	
	@Override
	public final void run() {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 256; y++) {
					Block block = chunk.getBlock(x, y, z);
					this.callback(block);
				}
			}
		}		
	}
	
	public abstract void callback(Block block);
	
}
