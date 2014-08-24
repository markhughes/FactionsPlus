package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.extras.FType;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;

import at.pavlov.cannons.event.CannonUseEvent;

public class CannonsListener implements Listener {
	
	/**
	 * Boolean to define if it is hooked in
	 */
	public static boolean isHooked = false;
	
	/**
	 * Store our listener 
	 */
	public static CannonsListener listener;
	
	/**
	 * Plugin Name
	 */
	public static String pluginName = "Cannons";
	
	/**
	 * Determine if a plugin is enabled, and if so - setup our listeners 
	 * @param instance
	 */
	public static final void enableOrDisable(FactionsPlus instance) {
 		PluginManager pm = Bukkit.getServer().getPluginManager();
			
 		// Check if plugin is enabled, and check if the plugin is integrated
		if(pm.isPluginEnabled(pluginName) && !isHooked) {
			listener = new CannonsListener();
			
			pm.registerEvents(listener, instance);
			
			// Try again
			if(listener == null) {
				listener = new CannonsListener();
				pm.registerEvents(listener, instance);
			}
			
			FactionsPlus.debug("Hooked into plugin: "+pluginName);
		}
	}
	
	/********/
	
	@EventHandler
	public void cannonUseEvent(CannonUseEvent e) {
		UPlayer uPlayer = UPlayer.get(e.getPlayer());
		
		if(uPlayer == null) return;
		
		if(!FPUConf.get(uPlayer.getUniverse()).enabled) return;

		if(FPUConf.get(uPlayer.getUniverse()).cannons.get("useCannonsInEnemy")) {
			if(uPlayer.isInEnemyTerritory()) {
				uPlayer.msg(Txt.parse(LConf.get().cannonsCantUseInEnemy));
				e.setCancelled(true);
				return;
			}
		}
		
		if(FPUConf.get(uPlayer.getUniverse()).cannons.get("useCannonsInOwn")) {
			if(uPlayer.isInOwnTerritory() ) {
				uPlayer.msg(Txt.parse(LConf.get().cannonsCantUseInOwn));
				e.setCancelled(true);
				return;
			}
		}
		
		if(FPUConf.get(uPlayer.getUniverse()).cannons.get("useCannonsInWilderness")) {
			if(FType.valueOf(BoardColls.get().getFactionAt(PS.valueOf(uPlayer.getPlayer().getLocation()))) == FType.WILDERNESS) {
				uPlayer.msg(Txt.parse(LConf.get().cannonsCantUseInWilderness));
				e.setCancelled( true );
				return;
			}
		}
	}
}