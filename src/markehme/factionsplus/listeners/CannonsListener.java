package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusPlugin;
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
	
	public static boolean isCannonsIntegrated = false;
	public static CannonsListener cannonslistener;
	
	/**
	 * Depending on the Cannons configuration, we allow
	 * servers to control how cannons are used. 
	 * @param e
	 */
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
	
	/**
	 * Determine if we should enable integration, and if so
	 * commence the integration 
	 * @param instance
	 */
	public static final void enableOrDisable(FactionsPlus instance) {
 		PluginManager pm = Bukkit.getServer().getPluginManager();
			
 		// Check if plugin is enabled, and check if the plugin is integrated
		if(pm.isPluginEnabled("Cannons") && !isCannonsIntegrated) {
			cannonslistener = new CannonsListener();
			
			pm.registerEvents(cannonslistener, instance);
			
			// Try again
			if(cannonslistener == null) {
				cannonslistener = new CannonsListener();
				pm.registerEvents(cannonslistener, instance);
			}
			
			FactionsPlusPlugin.debug("Hooked into plugin: Cannons");
		}	
	}
}