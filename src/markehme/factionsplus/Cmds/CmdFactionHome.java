package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.arg.ARFaction;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.ps.PS;

public class CmdFactionHome extends FPCommand {
	Factions factions;
	
	public CmdFactionHome() {
		this.aliases.add("factionhome");
		
		this.fpidentifier = "factionhome";
		
		this.requiredArgs.add("faction");
		this.errorOnToManyArgs = false;
		
		this.addRequirements( ReqFactionsEnabled.get() );
		this.addRequirements( ReqIsPlayer.get() );
		this.addRequirements( ReqHasFaction.get() );
		
		this.setHelp("teleport to another Factions home");
		this.setDesc("teleport to another Factions home");
	}
	
	@Override
	public void performfp() {		
		if( FactionsPlus.permission.has( sender, "factionsplus.otherfactionshome" ) || sender.isOp()) {
			String factionName = this.arg(0).toString();
			
			Faction currentF = this.arg(0, ARFaction.get(sender));
			
			if(currentF == null) {
				
				msg(ChatColor.RED + "Faction was not found!");
				
			} else {
				
				if( currentF.hasHome() ) {
					
					Location FactionHome = PS.asBukkitLocation( currentF.getHome() );
					me.teleport( FactionHome );
					
					msg( ChatColor.GOLD + "You have been teleported to the Faction home of " + ChatColor.RED + factionName );
					
				} else {
					
					msg( "That Faction doesn't have a home set!" );
					
				}
				
			}
			
		} else {
			
			msg( ChatColor.RED + "No permission to use this command!" );
			
		}
	}
}
