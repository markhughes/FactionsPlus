package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.MCore.FPUConf;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.FactionsEventChunkChange;

public class DenyClaimListener implements Listener {
	
	@EventHandler(priority=EventPriority.NORMAL)
	public void onFactionsClaim(FactionsEventChunkChange event) {
		if(!FPUConf.get(event.getUSender().getUniverse()).enabled) return;

		Faction faction 	= event.getNewFaction();
		String fid 			= faction.getId();
		
		UPlayer fplayer 	= event.getUSender();
		
		for (Player player: FactionsPlus.instance.getServer().getOnlinePlayers()) {
			UPlayer cur = UPlayer.get(player);
			if (!cur.getFactionId().equals(fid)) {
				Faction faction2 = cur.getFaction();
				
				Rel rel = faction.getRelationTo(faction2);
				
				// Ensure they're in the same chunk!
				if(fplayer.getPlayer().getLocation().getChunk() != player.getLocation().getChunk()) {
					continue;
				}
				
				
				if(FPUConf.get(event.getUSender().getUniverse()).denyClaimsWhenNearby.containsKey(faction.getRelationTo(faction2))) {
					if(FPUConf.get(event.getUSender().getUniverse()).denyClaimsWhenNearby.get(faction.getRelationTo(faction2))) {
						event.setCancelled( true );
						cur.sendMessage( "Someone ("+rel+") tried to claim this chunk but was denied." );
						fplayer.sendMessage( ChatColor.RED+"You may not claim this chunk while "+rel.toString()+" is within it." );
						return;
					}
				}

			}
		}
	}
	
}
