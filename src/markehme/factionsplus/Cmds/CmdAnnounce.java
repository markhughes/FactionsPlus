package markehme.factionsplus.Cmds;



import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusTemplates;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;
import markehme.factionsplus.util.TextUtil;

import org.bukkit.ChatColor;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.UConf;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;

public class CmdAnnounce extends FPCommand {
	public CmdAnnounce() {
		this.aliases.add("announce");
		
		this.requiredArgs.add("message");
		this.errorOnToManyArgs = false;
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		
		this.setHelp("sends an announcment to your Faction");
		this.setDesc("sends an announcment to your Faction");
	}
	
	@Override
	public void performfp() {
		if(!FactionsPlus.permission.has(sender, "factionsplus.announce")) {
			sender.sendMessage(ChatColor.RED + "No permission!");
			return;
		}
		
		String message 				= TextUtil.implode(args, " ").replaceAll("(&([a-f0-9]))", "& $2");
		
		Faction currentFaction 		= usender.getFaction();


		if(!Config._announce.leadersCanAnnounce._ && Utilities.isLeader(usender) ) {
			if( !Config._announce.officersCanAnnounce._ && Utilities.isOfficer(usender) ) {
				sender.sendMessage(ChatColor.RED + "Sorry, your ranking is not high enough to do that!");
				return;
			}
		} 
		
		if(Config._economy.costToAnnounce._ > 0.0d) {
			// TODO: move to pay for command thingy 
			if ( !Utilities.doFinanceCrap( Config._economy.costToAnnounce._, "an announcement", usender ) ) {
				return;
			}
		}
		
		String[] argsa = { sender.getName(), message };
			
		String formatedAnnouncement = FactionsPlusTemplates.Go("announcement_message", argsa);
		
		currentFaction.msg(formatedAnnouncement);
		
		DataOutputStream announceWrite = null;
		
		try {
			
			File fAF = new File(Config.folderAnnouncements, usender.getFactionId());
			if(!fAF.exists()) {
				fAF.createNewFile();
			}
			formatedAnnouncement = "Previously, " + formatedAnnouncement;
			announceWrite = new DataOutputStream(new FileOutputStream(fAF, false));
			announceWrite.write(formatedAnnouncement.getBytes());
			
		} catch (Exception e) {
			
			e.printStackTrace();
		
			sender.sendMessage( "[FactionsPlus] Failed to set announcement (Internal error -21)" );
			
			return;
			
		} finally {
			if (null != announceWrite) {
				try {
					announceWrite.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
		}


	}

}
