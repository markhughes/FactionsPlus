package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.extras.LWCFunctions;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;

public class CmdClearLocks extends FPCommand {
	public CmdClearLocks() {
		this.aliases.add("clearlocks");
		
		this.errorOnToManyArgs = true;
				
		this.addRequirements( ReqFactionsEnabled.get());
		this.addRequirements( ReqIsPlayer.get());
		
		this.setHelp("clears LWC and Lockette locks not owned by a Faction member in your land");
		this.setDesc("clears LWC and Lockette locks not owned by a Faction member in your land");
		
	}
	
	@Override
	public void performfp() {
		if(!FactionsPlus.permission.has(sender, "factionsplus.clearlwclocks")) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
			return;
		}
		
		Location loc	= Utilities.getOnlinePlayerExact( usender ).getLocation();
		Player name		= Utilities.getOnlinePlayerExact( usender );
		
		int lwc_clearedlocks = LWCFunctions.clearLocksCommand( name, loc );
		
		int clearedlocks = 0;
		
		if( lwc_clearedlocks < 0 ) {
			
			lwc_clearedlocks = 0;
			
		}
		
		clearedlocks+= lwc_clearedlocks;
		
		if( clearedlocks == 0 ) {
			name.sendMessage( ChatColor.GOLD + "No unlockable protections were found in this chunk" );
			return;
		}
		
		name.sendMessage( ChatColor.GOLD + "Successfully removed " + clearedlocks + " protections from this chunk" );
		
	}

}
