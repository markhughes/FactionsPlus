package me.markeh.factionsplus.integration.lockette;

import me.markeh.factionsframework.objs.FPlayer;
import me.markeh.factionsplus.FactionsPlus;
import me.markeh.factionsplus.integration.IntegrationEvents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.yi.acru.bukkit.Lockette.Lockette;

public class IntegrationLocketteEvents extends IntegrationEvents implements Listener  {

	@Override
	public void enable() {
		FactionsPlus.get().addListener(this);
	}

	@Override
	public void disable() {
		FactionsPlus.get().removeListener(this);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event){
		FPlayer fplayer = FPlayer.get(event.getPlayer());
		
		if (Lockette.isProtected(event.getBlock()) && fplayer.isLeader() && ! Lockette.isOwner(event.getBlock(), event.getPlayer())) {
			String owner = Lockette.getProtectedOwner(event.getBlock());
			
			fplayer.msg("<green>This block is owned by <gold>" + owner + "<green> but has been broken as you're the leader.");
			
			event.setCancelled(true);
			event.getBlock().breakNaturally();
		}
	}
	
}
