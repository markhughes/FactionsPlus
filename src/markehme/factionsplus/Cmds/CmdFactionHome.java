package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Utilities;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;
import com.massivecraft.mcore.ps.PS;

public class CmdFactionHome extends FPCommand {
	Factions factions;
	
	public CmdFactionHome() {
		this.aliases.add("factionhome");
		
		this.requiredArgs.add("tag");
		this.errorOnToManyArgs = false;
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		
		this.setHelp("teleport to another Factions home");
		this.setDesc("From FactionsPlus, allows players with permission `otherfactionshome` to teleport to another Faction home.");
	}
	
	@Override
	public void performfp() {
		String factionName = this.arg(0).toString();
		
		Faction currentF = Faction.get(factionName);
		
		Player player = Utilities.getOnlinePlayerExact(usender);
		
		if( FactionsPlus.permission.has( player, "factionsplus.otherfactionshome" ) ) {
			
			if(currentF == null) {
				
				player.sendMessage("Faction was not found!");
				
			} else {
				
				if( currentF.hasHome() ) {
					
					Location FactionHome = PS.asBukkitLocation( currentF.getHome() );
					player.teleport( FactionHome );
					player.sendMessage( ChatColor.GOLD + "You have been teleported to the Faction home of " + ChatColor.RED + factionName );
					
				} else {
					
					player.sendMessage( "That Faction doesn't have a home set!" );
					
				}
				
			}
		} else {
			
			sendMessage( ChatColor.RED + "No permission to use this command!" );
			
		}
	}
}
