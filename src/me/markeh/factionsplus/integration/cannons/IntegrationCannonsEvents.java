package me.markeh.factionsplus.integration.cannons;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.pavlov.cannons.event.CannonBeforeCreateEvent;
import at.pavlov.cannons.event.CannonUseEvent;
import me.markeh.factionsframework.factionsmanager.FactionsManager;
import me.markeh.factionsframework.objs.FPlayer;
import me.markeh.factionsframework.objs.Loc;
import me.markeh.factionsplus.FactionsPlus;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.integration.IntegrationEvents;

public class IntegrationCannonsEvents extends IntegrationEvents implements Listener {

	@Override
	public void enable() {
		FactionsPlus.get().addListener(this);
	}

	@Override
	public void disable() {
		FactionsPlus.get().removeListener(this);
	}
	
	@EventHandler
	public void onCannonCreate(CannonBeforeCreateEvent event) {
		if( ! FactionsManager.get().fetch().isFactionsEnabled(event.getCannon().getLocation().getWorld())) return;
		
		Loc location = new Loc(event.getCannon().getLocation());
		
		FPlayer fplayer = FPlayer.get(event.getPlayer());
		
		if (fplayer.getPlayer().hasPermission("factionsplus.cannonsanywhere")) return;
		
		if (location.isWilderness() && ! Config.get().cannons_allowInWilderness) {
			fplayer.msg("<red>You can't create cannons in the wilderness!");
			event.setCancelled(true);
		} else if ( ! Config.get().cannons_allowInTerritories) {
			fplayer.msg("<red>You can't create cannons in territories!");
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onCannonUse(CannonUseEvent event) {
		if( ! FactionsManager.get().fetch().isFactionsEnabled(event.getCannon().getLocation().getWorld())) return;
		
		Loc location = new Loc(event.getCannon().getLocation());
		
		FPlayer fplayer = FPlayer.get(event.getPlayer());
		
		if (fplayer.getPlayer().hasPermission("factionsplus.cannonsanywhere")) return;
		
		if (location.isWilderness() && ! Config.get().cannons_allowInWilderness) {
			fplayer.msg("<red>You can't use cannons in the wilderness!");
			event.setCancelled(true);
		} else if ( ! Config.get().cannons_allowInTerritories) {
			fplayer.msg("<red>You can't use cannons in territories!");
			event.setCancelled(true);
		}
	}
}
