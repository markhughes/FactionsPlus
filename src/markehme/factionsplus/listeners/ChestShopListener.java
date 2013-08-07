package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsPlusPlugin;
import markehme.factionsplus.config.Config;
import markehme.factionsplus.extras.FType;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.Acrobot.ChestShop.Events.PreShopCreationEvent;
import com.Acrobot.ChestShop.Events.PreShopCreationEvent.CreationOutcome;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.FactionsEventChunkChange;
import com.massivecraft.mcore.ps.PS;

public class ChestShopListener implements Listener {

	@EventHandler(ignoreCancelled=true)
	public void onChestShopCreate(PreShopCreationEvent event) {
		
		Faction factionAtSign = BoardColls.get().getFactionAt(PS.valueOf(event.getSign().getLocation()));
		FType factionType = FType.valueOf(factionAtSign);
		
		if(factionType == FType.WILDERNESS && !Config._extras._protection.allowShopsInWilderness._) {
			
			event.setOutcome( CreationOutcome.NO_PERMISSION_FOR_TERRAIN ) ;
			
			return;
		}
		
		if( factionType == FType.FACTION && !Config._extras._protection.allowShopsInTerritory._ ) {
			
			event.setOutcome( CreationOutcome.NO_PERMISSION_FOR_TERRAIN );
			
			return;
		}
		
	}
	
	@EventHandler(priority = EventPriority.MONITOR )
	public void onLandClaim( FactionsEventChunkChange event ) {
		if ( event.isCancelled() ) {
			return;
		} else {
			UPlayer uPlayer = event.getUSender();
			
			try {
				
				World world = event.getChunk().asBukkitWorld();
				
				if ( null == world ) {
					throw new Exception( "World is undenified." );
				}
				
				Chunk chunk = world.getChunkAt( 
						(uPlayer.getPlayer().getLocation().getBlockX()), 
						(uPlayer.getPlayer().getLocation().getBlockZ())
					);

				if ( !world.isChunkLoaded( chunk ) ) {
					world.loadChunk( chunk );
					
					if ( !chunk.isLoaded() ) {
						throw new Exception( "Failed to force load chunk at x: " + chunk.getX() + ", z:" + chunk.getZ() );
					}
				}
				
				int numberOfRemovedProtections = 0;
				
				// parse each block(in the chunk) and if it's a sign 
				for ( int x = 0; x < 16; x++ ) {
					for ( int z = 0; z < 16; z++ ) {
						for ( int y = 0; y < 256; y++ ) {
							
							Block block = chunk.getBlock( x, y, z );
							Material type = block.getType();
							
							// is it a sign?
							if( type == Material.SIGN ) {
								
								// get a connected chest
								Chest connectedChest = com.Acrobot.ChestShop.Utils.uBlock.findConnectedChest( block );
								
								// is it actually connected ? 
								if( connectedChest != null ) {
									
									if( ( FType.valueOf( BoardColls.get().getFactionAt(PS.valueOf(block.getLocation()))) == FType.WILDERNESS ) && !Config._extras._protection.allowShopsInWilderness._
											&& !com.Acrobot.ChestShop.Signs.ChestShopSign.isAdminShop((org.bukkit.block.Sign) block)) {
										
										// Break it! 
										
										block.breakNaturally();
										
									}
									
									if( ( FType.valueOf( BoardColls.get().getFactionAt(PS.valueOf(block.getLocation()))) == FType.FACTION ) && !Config._extras._protection.allowShopsInTerritory._
											&& !com.Acrobot.ChestShop.Signs.ChestShopSign.isAdminShop((org.bukkit.block.Sign) block)) {
										
										// Break it! 
										
										block.breakNaturally();
										
									}
									
									
									
								}
								
							}

						}
					}
				}

				if ( numberOfRemovedProtections > 0 ) {
					uPlayer.sendMessage( ChatColor.GOLD + "Automatically removed " + numberOfRemovedProtections
						+ " ChestShops protections in the claimed chunk." );
				}
			} catch ( Exception cause ) {
				event.setCancelled( true ); 
				FactionsPlusPlugin.severe(cause, "Internal error clearing Lockette locks on land claim, inform admin to check console." );
				uPlayer.msg("Internal error clearing Lockette locks on land claim, inform admin to check console.");
			}
		}
	}

}
