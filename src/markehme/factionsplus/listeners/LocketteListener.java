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

import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.FactionsEventChunkChange;

public class LocketteListener implements Listener  {
	
	@EventHandler(priority = EventPriority.MONITOR )
	public void onLandClaim( FactionsEventChunkChange event ) {
		if ( event.isCancelled() ) {
			return;
		} else {
			UPlayer uPlayer = event.getUSender();
			
			try {
				if (!LocketteBase.isLockettePluginPresent()) { 
					return;
				}
				
				int removedProtections = LocketteFunctions.removeLocketteLocks( event.getChunk(), uPlayer );
				if ( removedProtections > 0 ) {
					uPlayer.sendMessage( ChatColor.GOLD + "Automatically removed " + removedProtections
						+ " Lockette protections in the claimed chunk." );
				}
			} catch ( Exception cause ) {
				event.setCancelled( true ); 
				FactionsPlusPlugin.severe(cause, "Internal error clearing Lockette locks on land claim, inform admin to check console." );
				uPlayer.msg("Internal error clearing Lockette locks on land claim, inform admin to check console.");
			}
		}
	}
}
