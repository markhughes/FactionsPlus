package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsPlusPlugin;
import markehme.factionsplus.extras.LWCBase;
import markehme.factionsplus.extras.LWCFunctions;
import markehme.factionsplus.extras.LocketteBase;
import markehme.factionsplus.extras.LocketteFunctions;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.event.LandClaimEvent;

public class LocketteListener implements Listener  {
	
	@EventHandler(priority = EventPriority.MONITOR )
	public void onLandClaim( LandClaimEvent event ) {
		if ( event.isCancelled() ) {
			return;
		} else {
			FPlayer fPlayer = event.getFPlayer();
			
			try {
				if (!LocketteBase.isLockettePluginPresent()) { 
					return;
				}
				
				int removedProtections = LocketteFunctions.removeLocketteLocks( event.getLocation(), fPlayer );
				if ( removedProtections > 0 ) {
					fPlayer.sendMessage( ChatColor.GOLD + "Automatically removed " + removedProtections
						+ " Lockette protections in the claimed chunk." );
				}
			} catch ( Exception cause ) {
				event.setCancelled( true ); 
				FactionsPlusPlugin.severe(cause, "Internal error clearing Lockette locks on land claim, inform admin to check console." );
				fPlayer.msg("Internal error clearing Lockette locks on land claim, inform admin to check console.");
			}
		}
	}
}
