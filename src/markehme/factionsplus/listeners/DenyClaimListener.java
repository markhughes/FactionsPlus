package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.config.Config;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.FactionsEventChunkChange;

public class DenyClaimListener implements Listener {
	
	@EventHandler(priority=EventPriority.NORMAL)
	public void onFClaim(FactionsEventChunkChange event) {
		if (event.isCancelled()) {
			return;
		}
		
		Faction faction 	= event.getNewFaction();
		String fid 			= faction.getId();
		
		UPlayer fplayer 	= event.getUSender();
		
		for (Player player: FactionsPlus.instance.getServer().getOnlinePlayers()) {
			UPlayer cur = UPlayer.get(player);
			if (!cur.getFactionId().equals( fid )) {
				Faction faction2 = cur.getFaction();
				
				Rel rel = faction.getRelationTo(faction2);
				assert null != rel;
				if ( (Config._extras._protection._pvp.denyClaimWhenEnemyNearBy._ && faction.getRelationTo(faction2) == Rel.ENEMY) ||
					 (Config._extras._protection._pvp.denyClaimWhenAllyNearBy._ && faction.getRelationTo(faction2) == Rel.ALLY) ||
					 (Config._extras._protection._pvp.denyClaimWhenNeutralNearBy._ && (faction.getRelationTo(faction2) == Rel.NEUTRAL ||
					 faction.getRelationTo(faction2) == Rel.TRUCE)) ) 
				{
					event.setCancelled( true );
					cur.sendMessage( "Someone ("+rel+") tried to claim this chunk but was denied." );
					fplayer.sendMessage( ChatColor.RED+"You may not claim this chunk while "+rel+" is within it." );
					return;
				}
			}
		}
	}
	
}
