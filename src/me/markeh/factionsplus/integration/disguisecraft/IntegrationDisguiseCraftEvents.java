package me.markeh.factionsplus.integration.disguisecraft;

import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.factionsmanager.FactionsManager;
import me.markeh.factionsframework.objs.Rel;
import me.markeh.factionsplus.FactionsPlus;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.integration.IntegrationEvents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import pgDev.bukkit.DisguiseCraft.DisguiseCraft;
import pgDev.bukkit.DisguiseCraft.api.DisguiseCraftAPI;

public class IntegrationDisguiseCraftEvents extends IntegrationEvents implements Listener {
	
	private DisguiseCraftAPI api = null;
	
	@Override
	public void enable() {
		api = DisguiseCraft.getAPI();
		
		FactionsPlus.get().addListener(this);
	}

	@Override
	public void disable() {
		FactionsPlus.get().removeListener(this);
		
		api = null;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if ( ! api.isDisguised(event.getPlayer())) return;
		
		if (event.getPlayer().hasPermission("factionsplus.disguseanywhere")) return;
		
		Faction factionAt = FactionsManager.get().fetch().getFactionAt(event.getTo());
		
		if (factionAt.isWilderness() && ! Config.get().disguise_allowInWilderness) {
			api.undisguisePlayer(event.getPlayer());
			return;
		}
		
		if (factionAt.getMembers().contains(event.getPlayer()) && ! Config.get().disguise_allowInTerritory) {
			api.undisguisePlayer(event.getPlayer());
			return;
		}
		
		Rel rel = factionAt.getRealtionshipTo(event.getPlayer());
		
		if (rel == Rel.ENEMY && ! Config.get().disguise_allowInEnemy) {
			api.undisguisePlayer(event.getPlayer());
			return;
		}
	}
}
