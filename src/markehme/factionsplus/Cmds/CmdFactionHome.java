package markehme.factionsplus.Cmds;

import markehme.factionsplus.*;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.*;

import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;

public class CmdFactionHome extends FCommand {
	Factions factions;
	FPlayers fplayers;
	Faction faction;
	
	public CmdFactionHome() {
		this.aliases.add("factionhome");
		
		this.requiredArgs.add("tag");
		
		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = false;
		
		this.setHelpShort("teleport to another Factions home");
	}
	
	@Override
	public void perform() {
		String factionName = this.argAsString(0).toString();
		Faction currentF = Factions.i.getByTag(factionName);
		
		Player player = Utilities.getOnlinePlayerExact(fme);
		if(FactionsPlus.permission.has(player, "factionsplus.otherfactionshome")) {
			if(currentF == null) {
				player.sendMessage("Faction was not found!");
			} else {
				if(currentF.hasHome()) {
					Location FactionHome = currentF.getHome();
					player.teleport(FactionHome);
					player.sendMessage("You have been teleported to the Faction home of " + ChatColor.RED + factionName);
				} else {
					player.sendMessage("That faction doesn't have a home!");
				}
			}
		}
	}
}
