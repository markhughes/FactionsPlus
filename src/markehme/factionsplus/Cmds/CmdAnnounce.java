package markehme.factionsplus.Cmds;



import java.io.*;

import markehme.factionsplus.*;
import markehme.factionsplus.config.*;

import org.bukkit.ChatColor;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TextUtil;

public class CmdAnnounce extends FCommand {
	public CmdAnnounce() {
		this.aliases.add("announce");
		this.errorOnToManyArgs = false;
		
		this.requiredArgs.add("message");
		
		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = false;
		
		this.setHelpShort("sends an announcment to your Faction");
	}
	
	@Override
	public void perform() {
		String message = TextUtil.implode(args, " ").replaceAll("(&([a-f0-9]))", "& $2");
		
		if(!FactionsPlus.permission.has(sender, "factionsplus.announce")) {
			sender.sendMessage(ChatColor.RED + "No permission!");
			return;
		}
		
		FPlayer fplayer = FPlayers.i.get(sender.getName());
		Faction currentFaction = fplayer.getFaction();

		boolean authallow = false;

		if(Config._announce.leadersCanAnnounce._ && Utilities.isLeader(fplayer)) {
			authallow = true;
		} else if(Config._announce.officersCanAnnounce._ && Utilities.isOfficer(fplayer)) {
			authallow = true;
		}

		if(!authallow) {
			sender.sendMessage(ChatColor.RED + "Sorry, your ranking is not high enough to do that!");
			return;
		}
		
		if(Config._economy.costToAnnounce._ > 0.0d) {
			// TODO: move to pay for command thingy 
			if (!doFinanceCrap(Config._economy.costToAnnounce._, "to make an announcement", "for making an announcement", fplayer)) {
				return;
			}
		}
		
		String[] argsa = new String[3];
		argsa[1] = sender.getName();
		argsa[2] = message;
		
		String formatedMessage = FactionsPlusTemplates.Go("announcement_message", argsa);
		
		
		for (FPlayer fplayerlisting : currentFaction.getFPlayersWhereOnline(true)){
			fplayerlisting.msg(formatedMessage);
		}
		
		DataOutputStream announceWrite=null;
		try {
			File fAF = new File(Config.folderAnnouncements, fplayer.getFactionId());
			if(!fAF.exists()) {
				fAF.createNewFile();
			}
			formatedMessage = "Previously, " + formatedMessage;
			announceWrite = new DataOutputStream(new FileOutputStream(fAF, false));
			announceWrite.write(formatedMessage.getBytes());
			announceWrite.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		
			sender.sendMessage("Failed to set announcement (Internal error -21)");
			return;
		}finally{
			if (null != announceWrite) {
				try {
					announceWrite.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
		}


	}
	
	public static boolean doFinanceCrap(double cost, String toDoThis, String forDoingThis, FPlayer player) {
		if ( !Config._economy.enabled._ || ! Econ.shouldBeUsed() || player.getPlayer() == null || cost == 0.0) return true;
		
		if(Conf.bankEnabled && Conf.bankFactionPaysCosts && player.hasFaction())
			return Econ.modifyMoney(player.getFaction(), -cost, toDoThis, forDoingThis);
		else
			return Econ.modifyMoney(player, -cost, toDoThis, forDoingThis);
	}
}
