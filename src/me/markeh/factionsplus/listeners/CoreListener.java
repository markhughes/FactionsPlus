package me.markeh.factionsplus.listeners;

import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.factionsmanager.FactionsManager;
import me.markeh.factionsframework.objs.FPlayer;
import me.markeh.factionsplus.FactionsPlus;
import me.markeh.factionsplus.conf.Config;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CoreListener implements Listener {

	private static CoreListener instance = null;
	public static CoreListener get() {
		if (instance == null) instance = new CoreListener();
		return instance;
	}
	
	protected CoreListener() { }
	
	// Check for commands, and if an enemy is in a radius we block it 
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		// Ops can bypass 
		if (event.getPlayer().isOp()) return;
		
		// Permission can bypass
		if (event.getPlayer().hasPermission("factionsplus.commandblockbypass")) return;
		
		// Ensure it's not disabled 
		if (Config.get().commandBlockInRadiusOf <= 0) return;
		
		// Grab fplayer 
		FPlayer fplayer = FPlayer.get(event.getPlayer());
		
		// Grab faction at location
		Faction faction = FactionsManager.get().fetch().getFactionAt(fplayer.getPlayer().getLocation());
		
		// If it's their land, they can run the command 
		if (faction.getID() == fplayer.getFactionID()) return;
		
		// Fetch the command and get the executing command
		String command = event.getMessage();
		if (event.getMessage().startsWith("/")) command = command.substring(1);
		if (event.getMessage().contains(" ")) command = event.getMessage().split(" ")[0];
		command = command.toLowerCase().trim();
		
		// Check if it's in the block list 
		if ( ! Config.get().commandBlockCommands.contains(command)) return;
		
		
		// Grab the radius, and check if any of the players are near this player
		double radiusSquared = Config.get().commandBlockInRadiusOf * Config.get().commandBlockInRadiusOf;
	 
		for (Player rplayer : FactionsPlus.get().getServer().getOnlinePlayers()) {
			if (rplayer.getLocation().distanceSquared(event.getPlayer().getLocation()) <= radiusSquared) {
				FPlayer frplayer = FPlayer.get(rplayer);
				
				// Check if it's an enemy 
				if ( ! frplayer.getFaction().isEnemyOf(fplayer.getFaction())) return;
				
				// Cancel the command, notify the player 
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "There is an enemy too close to use this command!");
				return;
			}
		}
	}


}
