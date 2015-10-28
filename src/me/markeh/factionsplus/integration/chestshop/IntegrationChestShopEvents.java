package me.markeh.factionsplus.integration.chestshop;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.Acrobot.ChestShop.Events.PreTransactionEvent;
import com.Acrobot.ChestShop.Events.PreTransactionEvent.TransactionOutcome;
import com.Acrobot.ChestShop.Events.Protection.BuildPermissionEvent;

import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.factionsmanager.FactionsManager;
import me.markeh.factionsframework.objs.FPlayer;
import me.markeh.factionsframework.objs.Rel;
import me.markeh.factionsplus.FactionsPlus;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.integration.IntegrationEvents;

public class IntegrationChestShopEvents extends IntegrationEvents implements Listener {

	@Override
	public void enable() {
		FactionsPlus.get().addListener(this);
	}

	@Override
	public void disable() {
		FactionsPlus.get().removeListener(this);

	}
	
	@EventHandler
	public void onBeforeShopCreationEvent(BuildPermissionEvent event) {
		if (event.getPlayer() == null) return;
		
		// Get FPlayer
		FPlayer fplayer = FPlayer.get(event.getPlayer());
		
		// Get the faction at the sign
		Faction factionAt = FactionsManager.get().fetch().getFactionAt(event.getSign().getBlock().getLocation());
		
		if ( ! Config.get().chestShop_allowInTerritory && factionAt.getID() == fplayer.getFactionID()) {
			event.disallow();
			fplayer.msg("<red>You can't build shops in your territory!");
			return;
		}
		
		if ( ! Config.get().chestShop_allowInWilderness && factionAt.isWilderness()) {
			event.disallow();
			fplayer.msg("<red>You can't build shops in the wilderness!");
			return;
		}
	}
	
	public void onShopUse(PreTransactionEvent event) {
		if (event.getClient() == null) return;
		
		// Get FPlayer
		FPlayer fplayer = FPlayer.get(event.getClient());
		
		// Get faction here
		Faction factionAt = FactionsManager.get().fetch().getFactionAt(event.getSign().getLocation());
		
		// If they can't use enemy shops and they're an enemy then restrict it 
		if ( ! Config.get().chestShop_canUseEnemy && factionAt.getRealtionshipTo(fplayer) == Rel.ENEMY) {
			event.setCancelled(TransactionOutcome.SHOP_IS_RESTRICTED);
			return;
		}
	}
	
	

}
