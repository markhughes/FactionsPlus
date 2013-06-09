package markehme.factionsplus;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;

public class FactionsPlusChests {
	
	/**
	 * Creates a Faction chest
	 * @param faction
	 * @param creator
	 * @param chestName
	 * @param chestPermission
	 */
	public static void createChest(Faction faction, FPlayer creator, String chestName, String chestPermission) {
		if(Utilities.isLeader(creator) || Utilities.isOfficer(creator) ) {
			// TODO: Create a Faction chest
			return;
		} else {
			creator.msg( ChatColor.RED + "Your ranking is not high enough to create a Faction chest." );
			return;
		}
		
	}
	
	/**
	 * Removes a Faction chest
	 * @param faction
	 * @param destroyer
	 * @param chestName
	 */
	public static void removeChest(Faction faction, FPlayer destroyer, String chestName) {
		if(Utilities.isLeader(destroyer) || Utilities.isOfficer(destroyer) ) {
			// TODO: Remove a Faction chest
			return;
		} else {
			destroyer.msg( ChatColor.RED + "Your ranking is not high enoguh to remove a Faction chest." );
			return;
		}
		
	}

}
