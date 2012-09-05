package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsBridge.Bridge;
import markehme.factionsplus.FactionsBridge.FactionsAny;
import markehme.factionsplus.FactionsBridge.FactionsAny.Relation;
import markehme.factionsplus.config.Config;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.P;
import com.massivecraft.factions.event.LandClaimEvent;


public class DenyClaimListener implements Listener {
	
	@EventHandler(priority=EventPriority.NORMAL)
	public void onFClaim(LandClaimEvent event) {
		if (event.isCancelled()) {
			return;
		}
		Faction faction = event.getFaction();
		String fid = event.getFactionId();
		FPlayer fplayer = event.getFPlayer();
		for (Player player: P.p.getServer().getOnlinePlayers())
		{
			FPlayer cur = FPlayers.i.get(player);
			if (!cur.getFactionId().equals( fid )) //IgnoreCase not needed since IDs are numbers w/ signs
			{//skip all players from same faction as claimer
				Faction faction2 = cur.getFaction();
				Relation rel = Bridge.factions.getRelationBetween( faction, faction2 );
				assert null != rel;
				if (
						(Config._extras._protection._pvp.denyClaimWhenEnemyNearBy._ && FactionsAny.Relation.ENEMY.equals(rel))
						||(Config._extras._protection._pvp.denyClaimWhenAllyNearBy._ && FactionsAny.Relation.ALLY.equals(rel))
						||(Config._extras._protection._pvp.denyClaimWhenNeutralNearBy._ && (FactionsAny.Relation.NEUTRAL.equals(rel) ||
								FactionsAny.Relation.TRUCE.equals(rel)))
								)
				{
					event.setCancelled( true );
					cur.sendMessage( "Someone"/*+ChatColor.YELLOW+fplayer*/+"("+rel+") tried to claim this chunk but was denied." );
					fplayer.sendMessage( ChatColor.RED+"You may not claim this chunk while "+rel+" is within it." );
					return;
				}
			}
		}
	}
	
}
