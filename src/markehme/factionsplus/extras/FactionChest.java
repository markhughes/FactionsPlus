package markehme.factionsplus.extras;

import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.Faction;

public class FactionChest {
	String factionID;
	Faction faction;
	
	public void load() {
		faction = Faction.get( factionID );
	}
	
	public void showChestToPlayer(Player player, Faction faction) {
		// TODO: shows a chest to a player, for the specified Faction
	}
}
