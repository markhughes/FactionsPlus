package markehme.factionsplus.Cmds;

import java.io.File;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;
import markehme.factionsplus.config.sections.Section_Banning;

import org.bukkit.ChatColor;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;


public class CmdUnban extends FPCommand {
	public CmdUnban() {
		this.aliases.add("unban");
		
		this.requiredArgs.add("player");
		this.errorOnToManyArgs = false;
		
		this.addRequirements( ReqFactionsEnabled.get() );
		this.addRequirements( ReqIsPlayer.get() );
		this.addRequirements( ReqHasFaction.get() );
		
		this.setHelp( "unbans a player allowing them to re-join the faction" );
		this.setDesc( "unbans a player allowing them to re-join the faction" );
	}
	
	@Override
	public void performfp(){
		if ((Config._banning.furtherRestrictBanUnBanToThoseThatHavePermission._)
				&&(!FactionsPlus.permission.has(sender, Section_Banning.banUnBanPermissionNodeName))) {
			msg(ChatColor.RED + "You don't have the required permission node!");
			return;
		}
		
		String unbanningThisPlayer = this.arg(0);
		
		boolean authallow = false;
		
		if(Config._banning.leadersCanFactionBanAndUnban._ && Utilities.isLeader(usender)){
			authallow = true;
		} else if(Config._banning.officersCanFactionBanAndUnban._ && Utilities.isOfficer(usender)){
			authallow = true;
		}
		
		if(!authallow){
			msg(ChatColor.RED + "Sorry, your ranking is not high enough to do that!");
			return;
		}
		
		
		File banFile = new File(Config.folderFBans, usender.getFactionId() + "." + unbanningThisPlayer.toLowerCase());
		
		if(banFile.exists()) {
			boolean deleted = banFile.delete();
			msg(unbanningThisPlayer + " has "+(deleted?"":"not")+" been unbanned from the Faction!");
		} else {
			msg("This user isn't banned from the Faction!");
		}
		
		
	}
}
