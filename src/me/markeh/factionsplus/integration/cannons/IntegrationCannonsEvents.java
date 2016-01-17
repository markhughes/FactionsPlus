package me.markeh.factionsplus.integration.cannons;

import java.util.HashSet;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.pavlov.cannons.API.CannonsAPI;
import at.pavlov.cannons.Enum.BreakCause;
import at.pavlov.cannons.Enum.MessageEnum;
import at.pavlov.cannons.cannon.Cannon;
import at.pavlov.cannons.event.CannonBeforeCreateEvent;
import at.pavlov.cannons.event.CannonUseEvent;

import me.markeh.factionsframework.events.LandChangeEvent;
import me.markeh.factionsframework.events.LandChangeEvent.ChangeType;
import me.markeh.factionsframework.factionsmanager.FactionsManager;
import me.markeh.factionsframework.objs.FPlayer;
import me.markeh.factionsframework.objs.Loc;

import me.markeh.factionsplus.FactionsPlus;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.integration.IntegrationEvents;
import me.markeh.factionsplus.util.ChunkAnalyser;

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
		
		Loc location = Loc.from(event.getCannon().getLocation());
		
		FPlayer fplayer = FPlayer.get(event.getPlayer());
		
		if (fplayer.getPlayer().hasPermission("factionsplus.cannonsanywhere")) return;
		
		if (location.isWilderness() && ! Config.get().cannons_allowInWilderness) {
			fplayer.msg("<red>You can't create cannons in the wilderness!");
			event.getCannon().destroyCannon(true, false, BreakCause.PlayerBreak);
			event.setMessage(MessageEnum.CannonDestroyed);
			event.setCancelled(true);
		} else if ( ! Config.get().cannons_allowInTerritories) {
			fplayer.msg("<red>You can't create cannons in territories!");
			event.getCannon().destroyCannon(true, false, BreakCause.PlayerBreak);
			event.setMessage(MessageEnum.CannonDestroyed);
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onCannonUse(CannonUseEvent event) {
		if( ! FactionsManager.get().fetch().isFactionsEnabled(event.getCannon().getLocation().getWorld())) return;
		
		Loc location = Loc.from(event.getCannon().getLocation());
		
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
	
	@EventHandler
	public void onLandClaim(LandChangeEvent event) {
		if (event.getChangeType() != ChangeType.Claim) return;
		
		new ChunkAnalyser(event.getChunk()) {
			@Override
			public final void callback(Block block) {
				HashSet<Cannon> cannons = CannonsAPI.getCannonsInSphere(block.getLocation(), 2.0);
				
				for (Cannon cannon : cannons) {
					if (FPlayer.get(cannon.getOwner()).getFaction() == event.getNewFaction()) continue;
					
					cannon.destroyCannon(true, false, BreakCause.PlayerBreak);
				}
			}
		}.runTaskAsynchronously(FactionsPlus.get());
	}
}
