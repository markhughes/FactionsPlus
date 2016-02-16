package me.markeh.factionsplus.wildernesschunks;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import me.markeh.factionsplus.FactionsPlus;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.wildernesschunks.integrations.HawkEyeRegegenerator;
import me.markeh.factionsplus.wildernesschunks.integrations.PrismRegenerator;
import me.markeh.factionsplus.wildernesschunks.integrations.Regenerator;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.scheduler.BukkitTask;

public class WildernessChunks {
	
	// --------
	// Singleton
	// --------
	
	private static WildernessChunks instance = null;
	public static WildernessChunks get() {
		return instance;
	}
	
	// --------
	// Fields
	// --------
		
	private HashMap<UUID, HashMap<Integer, HashMap<Integer, Long>>> chunkLog = new HashMap<UUID, HashMap<Integer, HashMap<Integer, Long>>>();
	private BukkitTask task = null;
	
	// --------
	// Methods
	// --------
		
	public void logChunk(Chunk chunk) {
		int x = chunk.getX();
		int z = chunk.getZ();
		
		if ( ! this.chunkLog.containsKey(chunk.getWorld().getUID())) this.chunkLog.put(chunk.getWorld().getUID(), new HashMap<Integer, HashMap<Integer, Long>>());
		
		if ( ! this.chunkLog.get(chunk.getWorld().getUID()).containsKey(x)) this.chunkLog.get(chunk.getWorld().getUID()).put(x,new HashMap<Integer, Long>());
		
		this.chunkLog.get(chunk.getWorld().getUID()).get(x).put(z, new Date().getTime());
	}
	
	public void startCheck() {
		this.loadLogCache();
		
		FactionsPlus.get().addListener(WildernessChunksEvents.get());
		
		this.task = new OutdatedChunkCheck().runTaskTimerAsynchronously(FactionsPlus.get(), 10L, 20L);
	}
	
	public void stopCheck() {
		FactionsPlus.get().removeListener(WildernessChunksEvents.get());
		
		if (this.task != null) this.task.cancel();
		
		this.saveLogCache();
	}
	
	public HashMap<UUID, HashMap<Integer, HashMap<Integer, Long>>> getChunkLog() {
		return this.chunkLog;
	}

	public void callRegen(Chunk chunkAt) {
		if (Config.get().wildernessregenUseGriefManagementPlugin && this.griefManagementPluginAvailable() != null) {
			
		} else {
			chunkAt.getWorld().regenerateChunk(chunkAt.getX(), chunkAt.getZ());
		}
	}
	
	private Regenerator regeneratorInstance = null;
	
	public Regenerator griefManagementPluginAvailable() {
		if (regeneratorInstance == null) {
			if (Bukkit.getPluginManager().isPluginEnabled("HawkEye")) regeneratorInstance = new HawkEyeRegegenerator();
			if (Bukkit.getPluginManager().isPluginEnabled("Prism")) regeneratorInstance = new PrismRegenerator();
		}
		
		return regeneratorInstance;
	}
	
	private void loadLogCache() {
		
	}
	
	private void saveLogCache() {
		
	}
}
