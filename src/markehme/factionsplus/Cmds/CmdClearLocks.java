package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.FactionsBridge.Bridge;
import markehme.factionsplus.extras.LWCFunctions;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;

public class CmdClearLocks extends FCommand {
	public CmdClearLocks() {
		this.aliases.add("clearlocks");
		this.errorOnToManyArgs = true;
				
		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = false;
//		senderMustBeAdmin = true;
		Bridge.factions.setSenderMustBeFactionAdmin(this, true);
		
		this.setHelpShort("Clears all LWC protections not owned by a faction member of the user's faction");
	}
	
	@Override
	public void perform() {
		if(!FactionsPlus.permission.has(sender, "factionsplus.clearlwclocks")) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
			return;
		}
		
		Location loc = Utilities.getOnlinePlayerExact(fme).getLocation();
		Player name = Utilities.getOnlinePlayerExact(fme);
		int clearedlocks = LWCFunctions.clearLocksCommand(name, loc);
		if( clearedlocks < 0 ) {
			return;
		}
		if( clearedlocks == 0 ) {
			name.sendMessage(ChatColor.GOLD + "No unlockable protections were found in this chunk");
			return;
		}
		name.sendMessage(ChatColor.GOLD + "Successfully removed " + clearedlocks + " protections from this chunk");

		
	}

}
