package markehme.factionsplus.Cmds;

import markehme.factionsplus.Utilities;
import markehme.factionsplus.Cmds.req.ReqBansEnabled;
import markehme.factionsplus.MCore.FactionData;
import markehme.factionsplus.MCore.FactionDataColls;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.util.FPPerm;

import org.bukkit.entity.Player;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.util.Txt;

public class CmdBan extends FPCommand {
	public CmdBan() {
		
		this.aliases.add("ban");
		
		this.fpidentifier = "ban";
		
		this.requiredArgs.add("player");
		this.errorOnToManyArgs = true;
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
		this.addRequirements(ReqBansEnabled.get());
		
		this.addRequirements(ReqHasPerm.get(FPPerm.BAN.node));
		
		this.setHelp(LConf.get().cmdDescBan);
		this.setDesc(LConf.get().cmdDescBan);
		
	}

	@Override
	public void performfp() {
		
		if(!FPUConf.get(usender.getUniverse()).whoCanBan.get(usender.getRole())) {
			msg(Txt.parse(LConf.get().banNotHighEnoughRanking));
			return;
		}
		
		Player banningPlayer = Utilities.getPlayer(this.arg(0));
		
		if(banningPlayer == null) {
			msg(Txt.parse("<red>This player hasn't been on the server before and you therefore can't ban them."));
			return;
		}
		
		UPlayer ubPlayer = UPlayer.get(banningPlayer);
		
		if(ubPlayer != null) {
			if(ubPlayer.isUsingAdminMode()) {
				msg(Txt.parse(LConf.get().banCannotBan));
				return;
			}

		}
		
		if(ubPlayer.getPlayer().isOp()) {
			msg(Txt.parse(LConf.get().banCannotBan));
			return;
		}
		
		// Disallow banning admins or ops
		if((ubPlayer.isUsingAdminMode() || ubPlayer.getPlayer().isOp())) {
			msg(Txt.parse(LConf.get().banCannotBan));
			return;
		}
		
		FactionData fData = FactionDataColls.get().getForUniverse(this.universe).get(usenderFaction.getId());
		
		// Are they already banned?
		if(fData.bannedPlayerIDs.containsKey(banningPlayer.getUniqueId().toString())) {
			msg(Txt.parse(LConf.get().banPlayerAlreadyBanned));
			return;
		}
		
		// Check they're not a leader or in admin mode
		if(ubPlayer.getRole().equals(Rel.LEADER) || ubPlayer.isUsingAdminMode()) {
			msg(Txt.parse(LConf.get().banCantBanLeader));
			return;
		}
		
		// Ensure we're following Faction rules of power 
		if(!com.massivecraft.factions.entity.UConf.get(this.universe).canLeaveWithNegativePower) {
			if(ubPlayer.getPower() < 0) {
				msg(Txt.parse(LConf.get().banCantBanTooLowPower));
				return;
			}
		}
		
		// Store the player data
		fData.bannedPlayerIDs.put(banningPlayer.getUniqueId().toString(), banningPlayer.getUniqueId().toString());
		
		// Run the kick event
		EventFactionsMembershipChange event = new EventFactionsMembershipChange(sender, ubPlayer, FactionColls.get().get(ubPlayer).getNone(), MembershipChangeReason.KICK);
		event.run();
		
		if (event.isCancelled()) return;
			
		// Uninvite the player from the Faction if they're still invited 
		if(usenderFaction.isInvited(ubPlayer)) {
			usenderFaction.setInvited(ubPlayer, false);
		}
		
		// Notify the Faction
		usenderFaction.msg(LConf.get().banNotifyAll, usender.getName(), ubPlayer.getName());
		
		ubPlayer.describeTo(usenderFaction, true);
		
		// Log faction kicks if required
		if (com.massivecraft.factions.entity.MConf.get().logFactionKick) {
			Factions.get().log( ( senderIsConsole ? "The console" : usender.getName() ) + " banned " + ubPlayer.getName()
				+ " from the faction: " + usenderFaction.getName() );
		}
		
		// Clear their Faction data
		if(ubPlayer.getFaction().getId() == usenderFaction.getId()) {
			ubPlayer.resetFactionData();
		}

	}
}
