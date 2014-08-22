package markehme.factionsplus.sublisteners;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.MCore.LConf;

import org.bukkit.entity.Player;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.EventFactionsChunkChange;
import com.massivecraft.mcore.util.Txt;


public class DenyClaimSubListener {
	
	public EventFactionsChunkChange eventFactionsChunkChange(EventFactionsChunkChange event) {

		Faction faction 	= event.getNewFaction();
		String fid 			= faction.getId();
		
		UPlayer uPlayer 	= event.getUSender();
		
		for (Player player: FactionsPlus.instance.getServer().getOnlinePlayers()) {
			UPlayer cur = UPlayer.get(player);
			
			if(!cur.getFactionId().equals(fid)) {
				// Ensure they're in the same chunk!
				if(uPlayer.getPlayer().getLocation().getChunk() != player.getLocation().getChunk()) {
					continue;
				}
				
				Faction faction2 = cur.getFaction();
				
				Rel rel = faction.getRelationTo(faction2);
				
				if(FPUConf.get(event.getUSender().getUniverse()).denyClaimsWhenNearby.containsKey(faction.getRelationTo(faction2))) {
					if(FPUConf.get(event.getUSender().getUniverse()).denyClaimsWhenNearby.get(faction.getRelationTo(faction2))) {
						event.setCancelled(true);
						cur.msg(Txt.parse(LConf.get().denyClaimNotice, rel.toString()));
						uPlayer.msg(Txt.parse(LConf.get().denyClaimNotify, rel.toString()));
						return event;
					}
				}

			}
		}
		
		return event;
	}
}
