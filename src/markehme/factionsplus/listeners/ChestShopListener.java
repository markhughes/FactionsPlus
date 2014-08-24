package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.extras.FType;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import com.Acrobot.ChestShop.Events.PreShopCreationEvent;
import com.Acrobot.ChestShop.Events.PreShopCreationEvent.CreationOutcome;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.EventFactionsChunkChange;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;

public class ChestShopListener implements Listener {
	
	
	/**
	 * Boolean to define if it is hooked in
	 */
	public static boolean isHooked = false;
	
	/**
	 * Store our listener 
	 */
	public static ChestShopListener listener;
	
	/**
	 * Plugin Name
	 */
	public static String pluginName = "ChestShop";
	
	/**
	 * Determine if a plugin is enabled, and if so - setup our listeners 
	 * @param instance
	 */
	public static final void enableOrDisable(FactionsPlus instance) {
 		PluginManager pm = Bukkit.getServer().getPluginManager();
			
 		// Check if plugin is enabled, and check if the plugin is integrated
		if(pm.isPluginEnabled(pluginName) && !isHooked) {
			listener = new ChestShopListener();
			
			pm.registerEvents(listener, instance);
			
			// Try again
			if(listener == null) {
				listener = new ChestShopListener();
				pm.registerEvents(listener, instance);
			}
			
			FactionsPlus.debug("Hooked into plugin: "+pluginName);
		}
	}
	
	/********/
	
	/**
	 * Detect if there is a ChestShop allowed in the area they're creating the
	 * shop in. 
	 * @param event
	 */
	@EventHandler
	public void onChestShopCreate(PreShopCreationEvent event) {
		if(!FPUConf.get(UPlayer.get(event.getPlayer()).getUniverse()).enabled) return;
		
		FType factionType	= FType.valueOf(BoardColls.get().getFactionAt(PS.valueOf(event.getSign().getLocation())));
		String universe		= UPlayer.get(event.getPlayer()).getUniverse();
		
		if(factionType == FType.WILDERNESS && !FPUConf.get(universe).allowShopsInWilderness) {
			event.setOutcome(CreationOutcome.NO_PERMISSION_FOR_TERRAIN);
			return;
		}
		
		if(factionType == FType.FACTION && !FPUConf.get(universe).allowShopsInTerritory) {
			event.setOutcome(CreationOutcome.NO_PERMISSION_FOR_TERRAIN);
			return;
		}
	}
	
	/**
	 * Detect if there is a ChestShop created by someone outside of the Faction on land
	 * claim. 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onLandClaim(EventFactionsChunkChange event) {
		if(event.isCancelled()) {
			return;
		} else {
			
			UPlayer uPlayer = event.getUSender();
			
			try {
				World world = event.getChunk().asBukkitWorld();
				
				// Ensure the world is loaded
				if(world == null) {
					throw new Exception("World is null");
				}
				
				Chunk chunk = world.getChunkAt( 
						(uPlayer.getPlayer().getLocation().getBlockX()), 
						(uPlayer.getPlayer().getLocation().getBlockZ())
					);
				
				// Ensure the chunk is loaded
				if(!world.isChunkLoaded(chunk)) {
					world.loadChunk(chunk);
					
					if (!chunk.isLoaded()) {
						throw new Exception("Failed to force load chunk at x: " + chunk.getX() + ", z: " + chunk.getZ());
					}
				}
				
				int numberOfRemovedProtections = 0;
				
				// TODO: move into alternative thread? 
				
				// Check all the blocks in the chunk for a sign
				for(int x = 0; x < 16; x++) {
					for(int z = 0; z < 16; z++) {
						for(int y = 0; y < 256; y++) {
							Block block = chunk.getBlock(x, y, z);
							Material type = block.getType();
							
							// We're looking for signs 
							if(type == Material.SIGN) {
								
								// Grab the chest
								Chest connectedChest = com.Acrobot.ChestShop.Utils.uBlock.findConnectedChest(block);
								
								// Ensure it is actually there
								if(connectedChest != null) {	
									
									org.bukkit.block.Sign theSign = (org.bukkit.block.Sign) block;
									
									// If they're a member of their Faction, don't do anything. 
									if(UPlayer.get(theSign.getLine(0).trim()).getFactionId() == uPlayer.getFactionId()) {
										return;
									}
									
									if((FType.valueOf(BoardColls.get().getFactionAt(PS.valueOf(block.getLocation()))) == FType.WILDERNESS) && !FPUConf.get(uPlayer.getUniverse()).allowShopsInWilderness
											&& !com.Acrobot.ChestShop.Signs.ChestShopSign.isAdminShop(theSign)) {
										
										// Break it
										block.breakNaturally();
									}
									
									if((FType.valueOf(BoardColls.get().getFactionAt(PS.valueOf(block.getLocation()))) == FType.FACTION) && !FPUConf.get(uPlayer.getUniverse()).allowShopsInTerritory
											&& !com.Acrobot.ChestShop.Signs.ChestShopSign.isAdminShop((org.bukkit.block.Sign) block)) {
										
										// Break it
										block.breakNaturally();
									}
								}
							}
						}
					}
				}
				
				// If there are protections that have been removed, notify the player 
				if(numberOfRemovedProtections > 0) {
					uPlayer.msg(Txt.parse(LConf.get().chestShopAutoRemoveNotice, numberOfRemovedProtections));
				}
				
			} catch (Exception cause) {
				event.setCancelled(true); 
				FactionsPlus.severe(cause, "Could not remove ChestShop shops on land claim (internal error)");
				uPlayer.msg(Txt.parse(LConf.get().chestShopErrorRemoving));
			}
		}
	}
}
