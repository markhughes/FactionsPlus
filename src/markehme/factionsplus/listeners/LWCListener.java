package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusPlugin;
import markehme.factionsplus.config.Config;
import markehme.factionsplus.extras.LWCBase;
import markehme.factionsplus.extras.LWCFunctions;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.griefcraft.scripting.event.LWCProtectionRegisterEvent;
import com.massivecraft.factions.FFlag;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.FactionsEventChunkChange;



public class LWCListener implements Listener {
	
	private final static String	msg	= "internal error clearing LWC locks on land claim, inform admin to check console.";
	
	
	@EventHandler(priority = EventPriority.MONITOR )
	public void onLandClaim( FactionsEventChunkChange event ) {
		if ( event.isCancelled() ) {
			return;
		} else {
			UPlayer fPlayer = event.getUSender();
			try {
				if (!LWCBase.isLWCPluginPresent()) {//ie. run this on server: plugman unload lwc
					//... but if you then also run /f reloadfp  then this listener will be unloaded
					fPlayer.sendMessage( ChatColor.RED+"LWC plugin is not active." );
					return;
				}
				int removedProtections = LWCFunctions.clearLocks( event.getChunk().getLocation(), fPlayer );
				if ( removedProtections > 0 ) {
					fPlayer.sendMessage( ChatColor.GOLD + "Automatically removed " + removedProtections
						+ " LWC protections in the claimed chunk." );
				}
			} catch ( Exception cause ) {
				event.setCancelled( true );// disallow claim
				FactionsPlusPlugin.severe(cause, msg );
				fPlayer.sendMessage( "[FactionsPlus] " + msg );
			}
		}
	}
	
	@EventHandler()
	public void onCreateProtection(LWCProtectionRegisterEvent event) {
		
		if( Config._extras._protection.onlyPeacefulCreateLWCProtections._ ) {
			UPlayer uPlayer = UPlayer.get( event.getPlayer() );
			
			if( ! uPlayer.getFaction().getFlag( FFlag.PEACEFUL ) ) {
				
				event.setCancelled( true );
				
			}
			
		}
		
	}
	
}
