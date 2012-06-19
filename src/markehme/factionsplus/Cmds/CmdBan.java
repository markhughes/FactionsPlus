package markehme.factionsplus.Cmds;

import java.io.File;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;

public class CmdBan extends FCommand {
	public CmdBan() {
		this.aliases.add("ban");
		
		this.requiredArgs.add("player");
		
		//this.optionalArgs.put("on/off", "flip");
		
		this.errorOnToManyArgs = false;
		
		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = true;
		
		this.setHelpShort("kicks a player out of your Faction, and stops them from re-joining");
	}
	
	public void perform() {
		String banningThisPlayer = this.argAsString(0);
		Faction pFaction = fme.getFaction();
		
		if(!FactionsPlus.permission.has(sender, "factionsplus.ban")) {
			sender.sendMessage(ChatColor.RED + "No permission!");
			return;
		}
		
		Boolean authallow = false;
		
		if(FactionsPlus.config.getBoolean("leadersCanFactionBan")) {
			if(Utilities.isLeader(fme)) {
				authallow = true;
			}
		}
		
		if(FactionsPlus.config.getBoolean("officersCanFactionBan")) {
			if(Utilities.isOfficer(fme)) {
				authallow = true;
			}
		}
		
		if(!authallow) {
			fme.msg(ChatColor.RED + "Sorry, your ranking is not high enough to do that!");
			return;
		}
		
		Player playerBanThisPlayer = Bukkit.getServer().getPlayer(banningThisPlayer);
		
		FPlayer fPlayerBanThisPlayer = FPlayers.i.get(banningThisPlayer);
		
		if(Utilities.isLeader(fPlayerBanThisPlayer)) {
			me.sendMessage("You can't ban the leader of the Faction!");
			return;
		}
		
		if(fPlayerBanThisPlayer.getFactionId() != fme.getFactionId()) {
			fme.msg("You can only ban players in your Faction.");
			return;
		}
		
		fPlayerBanThisPlayer.leave(true);
		
		playerBanThisPlayer.sendMessage("You have been banned from this Faction!");
		
		File banFile = new File("plugins" + File.separator + "FactionsPlus" + File.separator + "fbans" + File.separator + pFaction.getId() + "." + banningThisPlayer.toLowerCase());
		
		if(banFile.exists()) {
			me.sendMessage("This user is already banned from the Faction!");
			return;
		} else {
			try {
				banFile.createNewFile();
				me.sendMessage(banningThisPlayer + " has been banned from the Faction!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
