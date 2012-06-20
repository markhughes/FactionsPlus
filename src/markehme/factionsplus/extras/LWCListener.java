package markehme.factionsplus.extras;

import org.bukkit.Location;
import org.bukkit.event.Listener;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.event.LandClaimEvent;

public class LWCListener implements Listener {
	public void onLandClaim(LandClaimEvent event) {
		if(event.isCancelled()==true) {
			return;
		}
		
		FPlayer fPlayer = event.getFPlayer();
		Location location = fPlayer.getPlayer().getLocation();
		
		LWCFunctions.clearLocks(location, fPlayer);
	}
}
