package markehme.factionsplus.listeners;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.event.LandClaimEvent;

import markehme.factionsplus.*;
import markehme.factionsplus.extras.LWCFunctions;



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
