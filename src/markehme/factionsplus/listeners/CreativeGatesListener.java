package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.extras.FType;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;

import com.massivecraft.creativegates.MainListener;
import com.massivecraft.creativegates.Perm;
import com.massivecraft.creativegates.entity.UConf;
import com.massivecraft.creativegates.entity.UGate;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.FactionsEventChunkChange;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.MUtil;

public class CreativeGatesListener implements Listener {
	
	public static boolean isCreativeGatesIntegrated = false;
	public static CreativeGatesListener creativegateslistener;

	public static final void enableOrDisable(FactionsPlus instance) {
 		PluginManager pm = Bukkit.getServer().getPluginManager();
		
		boolean isMVPplugin = pm.isPluginEnabled("CreativeGates");
		
		if(isMVPplugin && !isCreativeGatesIntegrated ) {			
			creativegateslistener = new CreativeGatesListener();
			pm.registerEvents(creativegateslistener, instance);
			
			if(null == creativegateslistener) {
				creativegateslistener = new CreativeGatesListener();
				Bukkit.getServer().getPluginManager().registerEvents(creativegateslistener, instance);
			}
			
			FactionsPlus.debug("Hooked into CreativeGates.");
		}	
	}

	/*
	 * Ok, so what're we're trying to do here is replicate the event that is done inside
	 * CreativeGates - with a higher priority, but change it so it cancels if required.
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void useGateCheck(PlayerMoveEvent event) {
		if(!FPUConf.get(UPlayer.get(event.getPlayer()).getUniverse()).enabled) return;
		
		// BEGIN: Original code 
		
		// If a player is moving from one block to another ...
		if (MUtil.isSameBlock(event)) return;
		
		// ... and there is a gate in the new block ...
		UGate ugate = UGate.get(event.getTo());
		if (ugate == null) return;

		// ... and if the gate is intact ...
		if (!ugate.isIntact()) {
			// Don't destroy, let their plugin take control 
			return;
		}

		// ... and gates are enabled here ...
		UConf uconf = UConf.get(event.getTo());
		if (!uconf.isEnabled()) return;

		// ... and we have permission to use gates ...
		Player player = event.getPlayer();
		if(!Perm.USE.has(player, true)) return;

		// ... and the gate has enter enabled ...
		if (!ugate.isEnterEnabled()) {
			// Don't send messages, let their plugin handle this
			return;
		}

		// ... and the player is alive ...
		if (player.isDead()) return;
		
		// END: original code
		
		if(!FPUConf.get(UPlayer.get(player)).creativegates.get("useCreativeGatesInEnemy") && BoardColls.get().getFactionAt(PS.valueOf(player.getLocation())).getRelationTo(UPlayer.get(player).getFaction()).equals(Rel.ENEMY)) {
			pushPlayerBack(player); 
			event.setCancelled(true);
		}
		
		if(!FPUConf.get(UPlayer.get(player)).creativegates.get("useCreativeGatesInAlly") && BoardColls.get().getFactionAt(PS.valueOf(player.getLocation())).getRelationTo(UPlayer.get(player).getFaction()).equals(Rel.ALLY)) {
			pushPlayerBack(player);
			event.setCancelled(true);
		}
		
		if(!FPUConf.get(UPlayer.get(player)).creativegates.get("useCreativeGatesInTruce") && BoardColls.get().getFactionAt(PS.valueOf(player.getLocation())).getRelationTo(UPlayer.get(player).getFaction()).equals(Rel.TRUCE)) {
			pushPlayerBack(player);
			event.setCancelled(true);
		}
		
		if(!FPUConf.get(UPlayer.get(player)).creativegates.get("useCreativeGatesInNeutral") && BoardColls.get().getFactionAt(PS.valueOf(player.getLocation())).getRelationTo(UPlayer.get(player).getFaction()).equals(Rel.NEUTRAL)) {
			pushPlayerBack(player);
			event.setCancelled(true);
		}
		
		if(!FPUConf.get(UPlayer.get(player)).creativegates.get("useCreativeGatesInWilderness") && FType.valueOf(BoardColls.get().getFactionAt(PS.valueOf(player.getLocation()))).equals(FType.WILDERNESS)) {
			pushPlayerBack(player);
			event.setCancelled(true);
		}
		
	}
	
	/**
	 * This attempts to remove any CreativeGates in the chunk
	 * @param event
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true )
	public void onLandClaim(FactionsEventChunkChange event) {
		
		// Confirm this is still enabled 
		if(!FPUConf.get(UPlayer.get(event.getSender())).creativegates.get("destroyOnClaimUnclaim")) return;
		
		// Grab the bukkit chunk and the chunk location
		final Chunk chunk = event.getChunk().asBukkitChunk(); 
		final PS chunkLoc = event.getChunk().getLocation(); 
		
		// Here begins the loop (in a thread)
		// TODO: confirm this is ok
		Bukkit.getScheduler().scheduleSyncDelayedTask(FactionsPlus.instance, new Runnable() {
			@Override
			public void run() {
				for(int x = 0; x < 16; x++) {
					for(int z = 0; z < 16; z++) {
						for(int y = 0; y < chunkLoc.asBukkitWorld().getMaxHeight(); y++) {
							
							// Fetch the block
							Block block = chunk.getBlock(x, y, z);
							
							// Ignore air blocks (too many of them)
							if(block.getType() == Material.AIR) continue; 
							
							// Confirm there is a gate nearby 
							if(MainListener.isGateNearby(block)) {
								FactionsPlus.debug("A CreativeGate was found at x: " + x + ", y: " + y + ", z: " + z);
								// Destroy the gate nearby
								MainListener.destroyGate(block);
							}
						}
					}
				}
			}
		});	
	}
	
	/**
	 * Pushes a player back from their current location
	 * @param p
	 */
	private void pushPlayerBack(Player p) {
		// TODO: push player back
	}
}
