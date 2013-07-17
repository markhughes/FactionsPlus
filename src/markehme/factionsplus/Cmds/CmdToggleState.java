package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;

import org.bukkit.ChatColor;

import com.massivecraft.factions.FFlag;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;

public class CmdToggleState extends FPCommand {
	
	public CmdToggleState() {
		super();
		
		this.aliases.add("toggle");
		this.aliases.add("togglestate");
		
		this.optionalArgs.put("faction", "yours");
		this.errorOnToManyArgs = false;
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		
		this.setHelp("changes the Faction between peaceful and normal");
		this.setDesc("changes the Faction between peaceful and normal");
		
	}
	
	// /f togglestate <faction>
	
	
	@Override
	public void performfp() {
		
		String factionToggling = this.arg(0);
		Faction factiont;
		
		boolean authallow=sender.isOp();
		factiont = usender.getFaction();//if this is reached, faction will exist, cause fme is member of it senderMustBeMember = true;

		if ( (factionToggling != null) && (!factionToggling.equals( factiont.getName())) ) {
			if(!FactionsPlus.permission.has(sender, "factionsplus.togglestate.others")) {
				sender.sendMessage(ChatColor.RED + "No permission to toggle peaceful for other factions!");
				return;
			} else {//here if either has that perm or is Op
				authallow|=sender.isOp();
			}
			
			factiont = Faction.get( factionToggling );	
			
			if (null == factiont) {
				msg(ChatColor.RED + "The faction `"+factionToggling+"` doesn't exist!");
				
				return;
				
			} else {
				if ( ( ! sender.isOp()) && ( ! Utilities.isNormalFaction( factiont ) ) ) {
					msg( ChatColor.RED +"You may not change the state of WarZone/SafeZone/Wilderness" );
					
					return;
					
				}
			}
		}
		
		authallow|=
			( ( Config._peaceful.membersCanToggleState._ )
				|| ( ( Config._peaceful.leadersCanToggleState._ ) && ( Utilities.isLeader( usender ) ) ) 
				|| ( ( Config._peaceful.officersCanToggleState._ ) && ( Utilities.isOfficer( usender ) ) ) );
		
		if ( !authallow ) {
			msg( ChatColor.RED + "Sorry, you do not have the allowed ranking(in your faction) to do that!" );
			return;
		}

		if( ! Utilities.isPeaceful( factiont ) ) {
			if ( (!Config._economy.isHooked())
					|| (Utilities.doFinanceCrap(Config._economy.costToToggleUpPeaceful._, "adding peaceful flag to " + factiont.getName(), usender)) ) {
				
				Utilities.setPeaceful(factiont);
				
				msg("You have toggled the faction `"+factiont.getName()+"` to Peaceful!");
			}
		} else {
			if ( (!Config._economy.isHooked()) 
					|| ( Utilities.doFinanceCrap(Config._economy.costToToggleDownPeaceful._, "removed peaceful flag from " + factiont.getName(), usender ) ) ) {
			
				factiont.setFlag(FFlag.PEACEFUL, false);
				
				msg("You have removed peaceful status from faction `"+factiont.getName()+"` !");
			}
		}
		
	} 
}