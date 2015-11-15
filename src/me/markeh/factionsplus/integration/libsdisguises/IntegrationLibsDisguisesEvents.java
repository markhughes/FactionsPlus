package me.markeh.factionsplus.integration.libsdisguises;

import me.libraryaddict.disguise.DisguiseAPI;
import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.factionsmanager.FactionsManager;
import me.markeh.factionsframework.objs.Rel;
import me.markeh.factionsplus.FactionsPlus;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.integration.IntegrationEvents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class IntegrationLibsDisguisesEvents extends IntegrationEvents implements Listener  {

	@Override
	public void enable() {
		FactionsPlus.get().addListener(this);
	}

	@Override
	public void disable() {
		FactionsPlus.get().removeListener(this);
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if ( ! DisguiseAPI.isDisguised(event.getPlayer())) return;
		if (event.getPlayer().hasPermission("factionsplus.disguseanywhere")) return;
		
		Faction factionAt = FactionsManager.get().fetch().getFactionAt(event.getTo());
		
		if (factionAt.isWilderness() && ! Config.get().disguise_allowInWilderness) {
			DisguiseAPI.undisguiseToAll(event.getPlayer());
			return;
		}
		
		if (factionAt.getMembers().contains(event.getPlayer()) && ! Config.get().disguise_allowInTerritory) {
			DisguiseAPI.undisguiseToAll(event.getPlayer());
			return;
		}
		
		Rel rel = factionAt.getRealtionshipTo(event.getPlayer());
		
		if (rel == Rel.ENEMY && ! Config.get().disguise_allowInEnemy) {
			DisguiseAPI.undisguiseToAll(event.getPlayer());
			return;
		}
	}
}
