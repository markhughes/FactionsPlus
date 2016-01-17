package me.markeh.factionsplus.wildernesschunks;

import java.util.Date;
import java.util.UUID;

import me.markeh.factionsplus.conf.Config;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class OutdatedChunkCheck extends BukkitRunnable {
	
	@Override
	public void run() {
		for (UUID world : WildernessChunks.get().getChunkLog().keySet()) {
			for (Integer x : WildernessChunks.get().getChunkLog().get(world).keySet()) {
				for (Integer z : WildernessChunks.get().getChunkLog().get(world).get(x).keySet()) {
					Long timestamp = WildernessChunks.get().getChunkLog().get(world).get(x).get(z);
					
					if (new Date().getTime()- timestamp > Config.get().wildernessregenTimer) {
						WildernessChunks.get().callRegen(Bukkit.getWorld(world).getChunkAt(x, z));
					}
				}
			}
		}
	}
}
