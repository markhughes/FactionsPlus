package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusPlugin;
import markehme.factionsplus.extras.LWCBase;
import markehme.factionsplus.extras.LWCFunctions;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.event.LandClaimEvent;



public class LWCListener implements Listener {
	
	private final static String	msg	= "internal error clearing LWC locks on land claim, inform admin to check console.";
	
	
	@EventHandler(
			priority = EventPriority.MONITOR )
	public void onLandClaim( LandClaimEvent event ) {
		if ( event.isCancelled() ) {
			return;
		} else {
			FPlayer fPlayer = event.getFPlayer();
			try {
				if (!LWCBase.isLWCPluginPresent()) {//ie. run this on server: plugman unload lwc
					fPlayer.sendMessage( ChatColor.RED+"LWC plugin is not active." );
					return;
				}
				int removedProtections = LWCFunctions.clearLocks( event.getLocation(), fPlayer );
				if ( removedProtections > 0 ) {
					fPlayer.sendMessage( ChatColor.GOLD + "Automatically removed " + removedProtections
						+ " LWC protections in the claimed chunk." );
				}
			} catch ( Exception cause ) {
				event.setCancelled( true );// disallow claim
				FactionsPlusPlugin.severe(cause, msg );
				fPlayer.sendMessage( FactionsPlus.FP_TAG_IN_LOGS + msg );
			}
		}
	}
	
}
