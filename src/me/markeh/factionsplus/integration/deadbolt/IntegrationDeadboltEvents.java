package me.markeh.factionsplus.integration.deadbolt;

import me.markeh.factionsframework.objs.FPlayer;
import me.markeh.factionsplus.FactionsPlus;
import me.markeh.factionsplus.integration.IntegrationEvents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.daemitus.deadbolt.Deadbolt;
import com.daemitus.deadbolt.Deadbolted;

public class IntegrationDeadboltEvents extends IntegrationEvents implements Listener  {

	@Override
	public void enable() {
		FactionsPlus.get().addListener(this);
	}

	@Override
	public void disable() {
		FactionsPlus.get().removeListener(this);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event){
		FPlayer fplayer = FPlayer.get(event.getPlayer());
		
		Deadbolted db = Deadbolt.get(event.getBlock());
		
		if (db.isProtected() && ! db.isOwner(event.getPlayer()) && ! db.isUser(event.getPlayer()) && fplayer.isLeader()) {
			fplayer.msg("<green>This block is owned by <gold>" + db.getOwner() + "<green> but has been broken as you're the leader.");
			event.setCancelled(true);
			event.getBlock().breakNaturally();
		}
	}}
