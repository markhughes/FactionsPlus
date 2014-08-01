package markehme.factionsplus.Cmds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import markehme.factionsplus.MCore.LConf;

import com.massivecraft.factions.FFlag;
import com.massivecraft.factions.Perm;
import com.massivecraft.factions.PlayerRoleComparator;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.arg.ARFaction;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UConf;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.EventFactionsChunkChangeType;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.money.Money;
import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;

/**
 * This class is a clone of Factions f/faction command.
 * 
 * Current update status:
 * https://github.com/MassiveCraft/Factions/blob/56768aca55e07e8a9c056a8c76eb4c52f71cff90/src/com/massivecraft/factions/cmd/CmdFactionsCreate.java
 * 
 * Differences: using older mcore references 
 */
public class CmdFactionsFaction extends FPCommand {

	public CmdFactionsFaction() {
		// Aliases
		
		// START: FactionsPlus edits 
		this.aliases.add("f");
		this.aliases.add("faction");
		// END: FactionsPlus edits 
		
		this.fpidentifier = "faction_override";
		
		// Args
		this.addOptionalArg("faction", "you");

		// Requirements
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasPerm.get(Perm.FACTION.node));

	}
	
	@Override
	public void performfp() {
		// Args
		Faction faction = this.arg(0, ARFaction.get(usenderFaction), usenderFaction);
		if (faction == null) return;
		
		// Data precalculation 
		UConf uconf = UConf.get(faction);
		//boolean none = faction.isNone();
		boolean normal = faction.isNormal();
		
		// INFO: Title
		msg(Txt.titleize(Txt.upperCaseFirst(faction.getUniverse()) + " Faction " + faction.getName(usender)));
		
		// INFO: Description
		if(!fpuconf.factionCommandHideDescriptionInfo.contains((faction.getId())) && !fpuconf.factionCommandHideDescriptionInfo.contains(("all"))) {
			msg("<a>Description: <i>%s", faction.getDescription());	
		}
		
		if (normal)
		{
			// INFO: Age
			long ageMillis = faction.getCreatedAtMillis() - System.currentTimeMillis();
			LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(ageMillis, TimeUnit.getAllButMillis()), 3);
			String ageString = TimeDiffUtil.formatedVerboose(ageUnitcounts, "<i>");
			
			if(!fpuconf.factionCommandHideAgeInfo.contains((faction.getId())) && !fpuconf.factionCommandHideAgeInfo.contains(("all"))) {
				msg("<a>Age: <i>%s", ageString);
			}
			
			// INFO: Open
			if(!fpuconf.factionCommandHideOpenInfo.contains((faction.getId())) && !fpuconf.factionCommandHideOpenInfo.contains(("all"))) {
				msg("<a>Open: <i>"+(faction.isOpen() ? "<lime>Yes<i>, anyone can join" : "<rose>No<i>, only invited people can join"));
			}
			
			// INFO: Power
			double powerBoost = faction.getPowerBoost();
			String boost = (powerBoost == 0.0) ? "" : (powerBoost > 0.0 ? " (bonus: " : " (penalty: ") + powerBoost + ")";
			if(!fpuconf.factionCommandHidePowerInfo.contains((faction.getId())) && !fpuconf.factionCommandHidePowerInfo.contains(("all"))) {
				msg("<a>Land / Power / Maxpower: <i> %d/%d/%d %s", faction.getLandCount(), faction.getPowerRounded(), faction.getPowerMaxRounded(), boost);
			}
			
			// show the land value
			if (Econ.isEnabled(faction))
			{
				long landCount = faction.getLandCount();
				
				for (EventFactionsChunkChangeType type : EventFactionsChunkChangeType.values())
				{
					Double money = uconf.econChunkCost.get(type);
					if (money == null) continue;
					if (money == 0D) continue;
					money *= landCount;
					
					String word = null;
					if (money > 0)
					{
						word = "cost";
					}
					else
					{
						word = "reward";
						money *= -1;
					}
					if(!fpuconf.factionCommandHideLandInfo.contains((faction.getId())) && !fpuconf.factionCommandHideLandInfo.contains(("all"))) {
						msg("<a>Total land %s %s: <i>%s", type.toString().toLowerCase(), word, Money.format(money));
					}
				}
				
				// Show bank contents
				if (UConf.get(faction).bankEnabled)
				{
					if(!fpuconf.factionCommandHideBankInfo.contains((faction.getId())) && !fpuconf.factionCommandHideBankInfo.contains(("all"))) {
						msg("<a>Bank contains: <i>"+Money.format(Money.get(faction)));
					}
				}
			}
			
			// Display important flags
			// TODO: Find the non default flags, and display them instead.
			if (faction.getFlag(FFlag.PERMANENT))
			{
				if(!fpuconf.factionCommandHidePermanentInfo.contains((faction.getId())) && !fpuconf.factionCommandHidePermanentInfo.contains(("all"))) {
					msg("<a>This faction is permanent - remaining even with no followers.");
				}
			}
			
			if (faction.getFlag(FFlag.PEACEFUL))
			{
				if(!fpuconf.factionCommandHidePeacefulInfo.contains((faction.getId())) && !fpuconf.factionCommandHidePeacefulInfo.contains(("all"))) {
					msg("<a>This faction is peaceful - in truce with everyone.");
				}
			}
		}
		
		String sepparator = Txt.parse("<i>")+", ";
		
		// List the relations to other factions
		Map<Rel, List<String>> relationNames = faction.getFactionNamesPerRelation(usender, true);
		
		if(!fpuconf.factionCommandHideIsTruceWithInfo.contains((faction.getId())) && !fpuconf.factionCommandHideIsTruceWithInfo.contains(("all"))) {
			if (faction.getFlag(FFlag.PEACEFUL))
			{
				sendMessage(Txt.parse("<a>In Truce with:<i> *everyone*"));
			}
			else
			{
				sendMessage(Txt.parse("<a>In Truce with: ") + Txt.implode(relationNames.get(Rel.TRUCE), sepparator));
			}
		}
		
		if(!fpuconf.factionCommandHideIsAlyWithInfo.contains((faction.getId())) && !fpuconf.factionCommandHideIsTruceWithInfo.contains(("all"))) {
			sendMessage(Txt.parse("<a>Allies: ") + Txt.implode(relationNames.get(Rel.ALLY), sepparator));
		}
		
		if(!fpuconf.factionCommandHideIsEnemyWithInfo.contains((faction.getId())) && !fpuconf.factionCommandHideIsTruceWithInfo.contains(("all"))) {
			sendMessage(Txt.parse("<a>Enemies: ") + Txt.implode(relationNames.get(Rel.ENEMY), sepparator));
		}
		
		// List the followers...
		List<String> followerNamesOnline = new ArrayList<String>();
		List<String> followerNamesOffline = new ArrayList<String>();
		
		List<UPlayer> followers = faction.getUPlayers();
		Collections.sort(followers, PlayerRoleComparator.get());
		


		// START: FactionsPlus edits 
		for (UPlayer follower : followers)
		{
			String uPower = String.valueOf(Math.ceil(follower.getPower())).replace(".0", "");
			
			// Create the follower name template 
			String followerTemplate = Txt.parse(LConf.get().factionCommandPlayerName).replace("{name}", follower.getNameAndTitle(usender)).replace("{power}", uPower);
			
			if (follower.isOnline() && Mixin.canSee(sender, follower.getId()))
			{
				followerNamesOnline.add(followerTemplate);
			}
			else if (normal)
			{
				// For the non-faction we skip the offline members since they are far to many (infinate almost)
				followerNamesOffline.add(followerTemplate);
			}
		}
		// END: FactionsPlus edits 
		if(!fpuconf.factionCommandHideFollowersOnlineInfo.contains((faction.getId())) && !fpuconf.factionCommandHideFollowersOnlineInfo.contains(("all"))) {
			sendMessage(Txt.parse("<a>Followers online (%s): ", followerNamesOnline.size()) + Txt.implode(followerNamesOnline, sepparator));
		}
		if (normal)
		{
			if(!fpuconf.factionCommandHideFollowersOfflineInfo.contains((faction.getId())) && !fpuconf.factionCommandHideFollowersOfflineInfo.contains(("all"))) {
				sendMessage(Txt.parse("<a>Followers offline (%s): ", followerNamesOffline.size()) + Txt.implode(followerNamesOffline, sepparator));
			}
		}
	}
}
