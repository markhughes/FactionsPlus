package markehme.factionsplus.Cmds;

import java.io.File;

import markehme.factionsplus.*;
import markehme.factionsplus.config.*;

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

	@Override
	public void perform() {
		String banningThisPlayer = this.argAsString(0);
		Faction pFaction = fme.getFaction();

		if(!FactionsPlus.permission.has(sender, "factionsplus.ban")) {
			sender.sendMessage(ChatColor.RED + "No permission!");
			return;
		}

		boolean authallow = false;

		if(Config._banning.leadersCanFactionBan._ && Utilities.isLeader(fme)) {
			authallow = true;
		} else if(Config._banning.officersCanFactionBan._ && Utilities.isOfficer(fme)) {
			authallow = true;
		}

		if(!authallow) {
			fme.msg(ChatColor.RED + "Sorry, your ranking is not high enough to do that!");
			return;
		}

		Player playerBanThisPlayer = Utilities.getOnlinePlayerExact(banningThisPlayer);

		FPlayer fPlayerBanThisPlayer = FPlayers.i.get(banningThisPlayer);

		if(Utilities.isLeader(fPlayerBanThisPlayer)) {
			me.sendMessage("You can't ban the leader of the Faction!");
			return;
		}

		if(!fPlayerBanThisPlayer.getFactionId().equalsIgnoreCase(fme.getFactionId())) {
			fme.msg("You can only ban players in your Faction.");
			return;
		}

		fPlayerBanThisPlayer.leave(true);

		if(playerBanThisPlayer != null){
			playerBanThisPlayer.sendMessage("You have been banned from this Faction!");
		}

		File banFile = new File(Config.folderFBans, pFaction.getId() + "." + banningThisPlayer.toLowerCase());

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
