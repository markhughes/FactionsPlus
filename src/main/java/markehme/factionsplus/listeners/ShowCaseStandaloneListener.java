package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusListener;
import markehme.factionsplus.FactionsPlusPlugin;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.extras.FType;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import com.kellerkindt.scs.ShowCaseStandalone;
import com.kellerkindt.scs.events.ShowCaseCreateEvent;
import com.kellerkindt.scs.exceptions.InsufficientPermissionException;
import com.kellerkindt.scs.shops.Shop;
import com.massivecraft.factions.FFlag;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.EventFactionsChunkChange;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;

public class ShowCaseStandaloneListener implements Listener {
	
	/**
	 * Boolean to define if it is hooked in
	 */
	public static boolean isHooked = false;
	
	/**
	 * Store our listener 
	 */
	public static ShowCaseStandaloneListener listener;
	
	/**
	 * Plugin Name
	 */
	public static String pluginName = "ShowCaseStandalone";
	
	/**
	 * Determine if a plugin is enabled, and if so - setup our listeners 
	 * @param instance
	 */
	public static final void enableOrDisable(FactionsPlus instance) {
 		PluginManager pm = Bukkit.getServer().getPluginManager();
			
 		// Check if plugin is enabled, and check if the plugin is integrated
		if(pm.isPluginEnabled(pluginName) && !isHooked) {
			listener = new ShowCaseStandaloneListener();
			
			pm.registerEvents(listener, instance);
			
			// Try again
			if(listener == null) {
				listener = new ShowCaseStandaloneListener();
				pm.registerEvents(listener, instance);
			}
			
			FactionsPlus.debug("Hooked into plugin: "+pluginName);
			FactionsPlusListener.pluginFeaturesEnabled.add(pluginName);
			
			// additional:
			scs = (ShowCaseStandalone)instance.getServer().getPluginManager().getPlugin("ShowCaseStandalone");
		}
	}
	
	/********/
	
	private static ShowCaseStandalone scs = null;
	
	@EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
	public void createShowCase( ShowCaseCreateEvent event ) {
		
		UPlayer uPlayer = UPlayer.get(event.getPlayer());
		FPUConf fpUConf = FPUConf.get(uPlayer.getUniverse());
		
		if(!fpUConf.enabled) return;
		
		Faction shopAt = BoardColls.get().getFactionAt( PS.valueOf( event.getShop().getLocation() ) );
		FType factionType = FType.valueOf(shopAt);
		
		if(factionType == FType.WILDERNESS && !fpUConf.allowShopsInWilderness) {
			
			event.setCancelled( true );		
			event.setCause( new InsufficientPermissionException( "You cannot create shops in the wilderness." ) );
			
			return;
		}
		
		if(factionType == FType.FACTION && !fpUConf.allowShopsInTerritory && !shopAt.getFlag(FFlag.PERMANENT)) {
			event.setCancelled(true);
			event.setCause(new InsufficientPermissionException("You cannot create shops in Faction territory."));
			
			return;
		}
		
	}
	
	
	@EventHandler(priority = EventPriority.MONITOR )
	public void onLandClaim( EventFactionsChunkChange event ) {
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
							
							if(scs.getShopHandler().isShopBlock(block)) {
								Shop s = scs.getShopHandler().getShop(block);
								if(s == null) continue;
								
								new com.kellerkindt.scs.events.ShowCaseDeleteEvent(Bukkit.getPlayer(s.getOwner()), s);
								
								numberOfRemovedProtections++;
							}
						}
					}
				}

				if ( numberOfRemovedProtections > 0 ) {
					uPlayer.msg(Txt.parse(LConf.get().showcaseRemoved, numberOfRemovedProtections));
				}
			} catch ( Exception cause ) {
				event.setCancelled( true ); 
				FactionsPlusPlugin.severe( cause, "Internal error clearing ShowCaseStandalone shops on land claim, inform admin to check console." );
				uPlayer.msg( "Internal error clearing ShowCaseStandalone shops on land claim, inform admin to check console.." );
			}
		}
	}
}
