package markehme.factionsplus.extras;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.event.LandClaimEvent;

import markehme.factionsplus.extras.LWCFunctions;

public class LWCListener implements Listener {
	@EventHandler(priority=EventPriority.MONITOR)
	public void onLandClaim(LandClaimEvent event) {
		if(event.isCancelled()==true) {
			return;
		}
		else{
		FPlayer fPlayer = event.getFPlayer();
		Location location = fPlayer.getPlayer().getLocation();
		LWCFunctions.clearLocks(location, fPlayer);
		}
	}
}
