package me.markeh.factionsplus.integration.chestshop;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.Acrobot.Breeze.Utils.BlockUtil;
import com.Acrobot.ChestShop.Signs.ChestShopSign;
import com.Acrobot.ChestShop.Events.PreTransactionEvent;
import com.Acrobot.ChestShop.Events.PreTransactionEvent.TransactionOutcome;
import com.Acrobot.ChestShop.Events.Protection.BuildPermissionEvent;

import me.markeh.factionsframework.events.LandChangeEvent;
import me.markeh.factionsframework.events.LandChangeEvent.ChangeType;
import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.factionsmanager.FactionsManager;
import me.markeh.factionsframework.objs.FPlayer;
import me.markeh.factionsframework.objs.Rel;
import me.markeh.factionsplus.FactionsPlus;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.integration.IntegrationEvents;
import me.markeh.factionsplus.util.ChunkAnalyser;

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
	
	@EventHandler
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
	
	@EventHandler
	public void onLandUnClaim(LandChangeEvent event) {
		if (event.getChangeType() != ChangeType.Unclaim) return;
		
		new ChunkAnalyser(event.getChunk()) {
			@Override
			public final void callback(Block block) {
				if (block.getType() != Material.SIGN) return;
				
				Sign sign = (Sign) block.getState();
				Block shopBlock = BlockUtil.getAttachedBlock(sign);
				
				if ( ! ChestShopSign.isShopChest(shopBlock)) return;
								
				if ( ! Config.get().chestShop_allowInWilderness) {
					block.breakNaturally();
				}
			}
		}.runTaskAsynchronously(FactionsPlus.get());
	}
	
	@EventHandler
	public void onLandClaim(LandChangeEvent event) {
		if (event.getChangeType() != ChangeType.Claim) return;
		
		new ChunkAnalyser(event.getChunk()) {
			@Override
			public final void callback(Block block) {
				if (block.getType() != Material.SIGN) return;
				
				Sign sign = (Sign) block.getState();
				Block shopBlock = BlockUtil.getAttachedBlock(sign);
				
				if ( ! ChestShopSign.isShopChest(shopBlock)) return;
				
				Player owner = Bukkit.getPlayer(sign.getLine(ChestShopSign.NAME_LINE));
				
				if (owner == null || FPlayer.get(owner).getFaction() != event.getNewFaction()) {
					block.breakNaturally();
					return;
				}
				
				if ( ! Config.get().chestShop_allowInTerritory) {
					block.breakNaturally();
				}
			}
		}.runTaskAsynchronously(FactionsPlus.get());
	}

}
