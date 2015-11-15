package me.markeh.factionsplus.integration.showcasestandalone;

import me.markeh.factionsframework.events.LandChangeEvent;
import me.markeh.factionsframework.events.LandChangeEvent.ChangeType;
import me.markeh.factionsframework.objs.FPlayer;
import me.markeh.factionsframework.objs.Loc;
import me.markeh.factionsplus.FactionsPlus;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.integration.IntegrationEvents;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.kellerkindt.scs.ShowCaseStandalone;
import com.kellerkindt.scs.events.ShowCaseCreateEvent;
import com.kellerkindt.scs.events.ShowCaseDeleteEvent;
import com.kellerkindt.scs.events.ShowCasePlayerBuyEvent;
import com.kellerkindt.scs.shops.Shop;

public class IntegrationShowCaseStandaloneEvents extends IntegrationEvents implements Listener  {

	private ShowCaseStandalone scs = null;
	
	@SuppressWarnings("deprecation")
	@Override
	public void enable() {
		FactionsPlus.get().addListener(this);
		
		this.scs = ShowCaseStandalone.get();
	}

	@Override
	public void disable() {
		FactionsPlus.get().removeListener(this);
		
		this.scs = null;
	}
	
	
	@EventHandler
	public void onLandClaim(LandChangeEvent event) {
		if (event.getChangeType() != ChangeType.Claim) return;
		
		Chunk chunk = event.getChunk();
		
		int removed = 0;
		
		FPlayer fplayer = FPlayer.get(event.getPlayer());
		
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 256; y++) {
					Block block = chunk.getBlock(x, y, z);
					
					if ( ! scs.getShopHandler().isShopBlock(block)) continue;
					
					Shop s = scs.getShopHandler().getShop(block);
					
					if (s == null) continue;
						
					FPlayer fowner = FPlayer.get(Bukkit.getPlayer(s.getOwner()));
					
					if (fplayer.getFaction() == fowner.getFaction()) continue;
					
					new ShowCaseDeleteEvent(Bukkit.getPlayer(s.getOwner()), s);
					
					removed++;
				}
			}
		}

		if (removed > 0) fplayer.msg("<gold><?> protections have been removed", String.valueOf(removed));
		
	}
	
	@EventHandler
	public void onSCSCreate(ShowCaseCreateEvent event) {
		Loc loc = Loc.from(event.getShop().getLocation());
		
		if (loc.isWilderness() && ! Config.get().showcase_allowInWilderness) {
			event.setCancelled(true);
			FPlayer.get(event.getPlayer()).msg("<red>You can't create a showcase shop in the wilderness!");
		} else if ( ! Config.get().showcase_allowInTerritory) {
			event.setCancelled(true);
			FPlayer.get(event.getPlayer()).msg("<red>You can't create a showcase shop in territories!");
		}
	}
	
	@EventHandler
	public void onSCSUse(ShowCasePlayerBuyEvent event) {
		Loc loc = Loc.from(event.getShop().getLocation());
		FPlayer fplayer = FPlayer.get(event.getPlayer());
		
		if (loc.getFactionHere().isEnemyOf(fplayer.getFaction()) && ! Config.get().showcase_canUseEnemy) {
			event.setCancelled(true);
			fplayer.msg("<red>You can't use the showcase shop here because you're an enemy of <gold><?><red>!", loc.getFactionHere().getName());
		}
	}
	
}
