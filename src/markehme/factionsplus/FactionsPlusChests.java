package markehme.factionsplus;

import java.util.ArrayList;

import org.bukkit.ChatColor;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;

import markehme.factionsplus.extras.FactionChest;
import markehme.factionsplus.references.FP;

public class FactionsPlusChests {
	
	public static ArrayList<FactionChest> loadedChests = new ArrayList<FactionChest>();
	
	public void setup() {
		FP.info( "Setting up Faction Chests" );
		

	}
	
	public void getChest(Faction faction, UPlayer sender, String chestName) {
				
		if( faction.getId() != sender.getFactionId())  {
			if( ! FP.permission.has(sender.getPlayer(), "factionsplus.accessotherfactionschests") && !sender.isUsingAdminMode() ) {
				sender.msg( ChatColor.RED + "You do not have permission to access other Faction chests!" );
				return;
			}
		} 
		
		
	}
}
