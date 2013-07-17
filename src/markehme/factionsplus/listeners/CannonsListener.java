package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusPlugin;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.ps.PS;

import at.pavlov.cannons.event.CannonUseEvent;

public class CannonsListener implements Listener {
	
	public static boolean isCannonsIntegrated = false;
	public static CannonsListener cannonslistener;
	
	@EventHandler
	public void cannonUseEvent(CannonUseEvent e) {
		UPlayer uPlayer = UPlayer.get( e.getPlayer() );
		
		if( uPlayer == null ) {
			return; // no UPlayer object to work with 
		}
		
		if( ! Config._extras._Cannons.allowCannonUseInEnemyTerritory._ ) {
			if( uPlayer.isInEnemyTerritory() ) {
				uPlayer.msg( ChatColor.RED + "You can not use Cannons in enemy territory." );
				e.setCancelled( true );
				return;
			}
		}
		
		if( ! Config._extras._Cannons.allowCannonUseInOwnTerritory._ ) {
			if( uPlayer.isInOwnTerritory() ) {
				uPlayer.msg( ChatColor.RED + "You can not use Cannons in your territory." );
				e.setCancelled( true );
				return;
			}
		}
		
		if( ! Config._extras._Cannons.allowCannonUseInWilderness._ ) {
			
			if( Utilities.isWilderness( BoardColls.get().getFactionAt( PS.valueOf( uPlayer.getPlayer().getLocation() ) ) ) )  {
				uPlayer.msg( ChatColor.RED + "You can not use Cannons in the wilderness." );
				e.setCancelled( true );
				return;
			}
		}
	}
	
	public static final void enableOrDisable(FactionsPlus instance){
 		PluginManager pm = Bukkit.getServer().getPluginManager();
			
		boolean isCannonsplugin = pm.isPluginEnabled("Cannons");
		
		if ( isCannonsplugin && !isCannonsIntegrated ) {
			assert ( null == cannonslistener );
			
			cannonslistener = new CannonsListener();
			pm.registerEvents( cannonslistener, instance );
			
			if (null == cannonslistener) {
				cannonslistener = new CannonsListener();
				Bukkit.getServer().getPluginManager().registerEvents(cannonslistener, instance);
			}
			
			FactionsPlusPlugin.info( "Hooked into Cannons." );
		}	
	}

}