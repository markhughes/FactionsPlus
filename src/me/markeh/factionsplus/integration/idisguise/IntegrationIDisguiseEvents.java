package me.markeh.factionsplus.integration.idisguise;

import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.factionsmanager.FactionsManager;
import me.markeh.factionsframework.objs.Rel;
import me.markeh.factionsplus.FactionsPlus;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.integration.IntegrationEvents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import de.robingrether.idisguise.api.DisguiseAPI;

public class IntegrationIDisguiseEvents extends IntegrationEvents implements Listener  {

	private DisguiseAPI api = null;

	@Override
	public void enable() {
		api = FactionsPlus.get().getServer().getServicesManager().getRegistration(DisguiseAPI.class).getProvider();
		
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
			api.undisguiseToAll(event.getPlayer());
			return;
		}
		
		if (factionAt.getMembers().contains(event.getPlayer()) && ! Config.get().disguise_allowInTerritory) {
			api.undisguiseToAll(event.getPlayer());
			return;
		}
		
		Rel rel = factionAt.getRealtionshipTo(event.getPlayer());
		
		if (rel == Rel.ENEMY && ! Config.get().disguise_allowInEnemy) {
			api.undisguiseToAll(event.getPlayer());
			return;
		}
	}
}
