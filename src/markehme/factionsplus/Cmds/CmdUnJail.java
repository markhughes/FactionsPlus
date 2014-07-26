package markehme.factionsplus.Cmds;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import markehme.factionsplus.EssentialsIntegration;
import markehme.factionsplus.FactionsPlusJail;
import markehme.factionsplus.Cmds.req.ReqJailsEnabled;
import markehme.factionsplus.MCore.FactionData;
import markehme.factionsplus.MCore.FactionDataColls;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.util.FPPerm;


import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.util.Txt;



public class CmdUnJail extends FPCommand {
	
	public CmdUnJail() {
		
		this.aliases.add( "unjail" );
		
		this.fpidentifier = "unjail";
		
		this.requiredArgs.add( "player" );
		this.errorOnToManyArgs = false;
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasFaction.get());
		this.addRequirements(ReqJailsEnabled.get());
		
		this.addRequirements(ReqHasPerm.get(FPPerm.JAIL.node));
		
		this.setHelp(LConf.get().cmdDescUnJail);
		this.setDesc(LConf.get().cmdDescUnJail);
		
	}
	
	
	@Override
	public void performfp() {
		
		OfflinePlayer jp = Bukkit.getPlayer(this.arg(0));
		
		if(jp == null) { 
			jp = Bukkit.getOfflinePlayer(this.arg(0));
		}
		
		if(jp == null) {
			msg(Txt.parse(LConf.get().jailsPlayerNeverOnServer));
			return;
		}
		
		UPlayer ujPlayer = UPlayer.get(jp);
		
		if(sender instanceof Player && !usender.isUsingAdminMode()) {
			if(!FPUConf.get(usender.getUniverse()).whoCanJail.get(usender.getRole())) {
				msg(Txt.parse(LConf.get().jailsNotHighEnoughRanking));
				return;
			}
			
			if(usender.getFactionId() != ujPlayer.getFactionId()) {
				msg(Txt.parse(LConf.get().jailsPlayerNotApartOfFaction, this.arg(0)));
				return;
			}
		}
		
		FactionData jfData = FactionDataColls.get().getForUniverse(usender.getUniverse()).get(usenderFaction.getId());
		
		if(!jfData.isJailed(ujPlayer)) {
			msg(Txt.parse(LConf.get().jailsPlayerNotInJail, this.arg(0)));
			return;
		}
		
		
		msg(Txt.parse(LConf.get().jailsPlayerUnJailed, this.arg(0)));
		
		Location unteleportLocation = jfData.jailedPlayerIDs.get(jp.getUniqueId().toString()).asBukkitLocation();
		jfData.jailedPlayerIDs.remove(jp.getUniqueId().toString());
		
		// If the player is online remove them from the jail
		if(jp.isOnline()) {
			if(!EssentialsIntegration.handleTeleport(jp.getPlayer(), unteleportLocation)) {
				jp.getPlayer().teleport(unteleportLocation);
			}
		}
	}
}
