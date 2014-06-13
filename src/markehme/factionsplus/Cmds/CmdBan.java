package markehme.factionsplus.Cmds;

import markehme.factionsplus.Utilities;
import markehme.factionsplus.Cmds.req.ReqBansEnabled;
import markehme.factionsplus.MCore.FactionData;
import markehme.factionsplus.MCore.FactionDataColls;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.util.FPPerm;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.FactionsEventMembershipChange;
import com.massivecraft.factions.event.FactionsEventMembershipChange.MembershipChangeReason;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;
import com.massivecraft.mcore.util.Txt;

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
		
		Player bPlayer = Utilities.getOnlinePlayerExact(this.arg(0));
		
		if(Bukkit.getPlayerExact(bPlayer.getName()) == null) {
			// Player is offline
			// TODO: replace bPlayer with API lookup (in another thread too?) 
			//	bPlayer = Bukkit.getOfflinePlayer(null);
			return;
		}
		
		UPlayer ubPlayer = UPlayer.get(bPlayer);
		
		// Disallow banning admins or ops
		if((ubPlayer.isUsingAdminMode() || ubPlayer.getPlayer().isOp())) {
			msg(Txt.parse(LConf.get().banCannotBan));
			return;
		}
		
		FactionData fData = FactionDataColls.get().getForUniverse(usender.getUniverse()).get(usenderFaction.getId());
		
		// Are they already banned?
		if(fData.bannedPlayerIDs.containsKey(bPlayer.getUniqueId().toString())) {
			msg(Txt.parse(LConf.get().banPlayerAlreadyBanned));
			return;
		}
		
		// Check they're not a leader or in admin mode
		if(ubPlayer.getRole().equals(Rel.LEADER) || ubPlayer.isUsingAdminMode()) {
			msg(Txt.parse(LConf.get().banCantBanLeader));
			return;
		}
		
		// Ensure we're following Faction rules of power 
		if(!com.massivecraft.factions.entity.UConf.get(usender.getUniverse()).canLeaveWithNegativePower) {
			if(ubPlayer.getPower() < 0) {
				msg(Txt.parse(LConf.get().banCantBanTooLowPower));
				return;
			}
		}
		
		// Store the player data
		fData.bannedPlayerIDs.put(bPlayer.getUniqueId().toString(), bPlayer.getUniqueId().toString());
		
		// Run the kick event
		FactionsEventMembershipChange event = new FactionsEventMembershipChange(sender, ubPlayer, FactionColls.get().get(ubPlayer).getNone(), MembershipChangeReason.KICK);
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
