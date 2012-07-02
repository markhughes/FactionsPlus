package markehme.factionsplus.Cmds;

import markehme.factionsplus.*;
import markehme.factionsplus.FactionsBridge.*;
import markehme.factionsplus.config.*;
import markehme.factionsplus.extras.*;

import org.bukkit.ChatColor;
import org.bukkit.entity.*;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
//import com.massivecraft.factions.struct;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;

public class CmdToggleState extends FCommand {
	
	public CmdToggleState() {
		super();
		
		this.aliases.add("toggle");
		this.aliases.add("togglestate");
		
		//this.requiredArgs.add("state");
		this.optionalArgs.put("faction", "faction");
		
		this.permission = "factionsplus.togglestate.use";//Permission.HELP.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = true;
		
		this.setHelpShort("changes the Faction between peaceful and normal");
		
	}
	
	// /f togglestate <faction>
	
	
	@Override
	public void perform() {
		
		String factionToggling = this.argAsString(0);
		Faction factiont;
		boolean authallow=false;
		//nvm seems fixed in Essentials-2.9.2 : groupmanager bug, once you set the  "factionsplus.togglestate.others" permission for Default group, reload then
		//you remove and reload, it's still seen as set/active
		
//		System.out.println(""+Utilities.hasPermissionOrIsOp( (Player)sender, new org.bukkit.permissions.Permission("factionsplus.togglestate.others"))
//			+" vs "+FactionsPlus.permission.has(sender,"factionsplus.togglestate.others"));
		if(!FactionsPlus.permission.has(sender, "factionsplus.togglestate.use")) {
			sender.sendMessage(ChatColor.RED + "No permission!");
			return;
		}else {//here if either has that perm or is Op
			authallow|=sender.isOp();
		}

		factiont = fme.getFaction();//if this is reached, faction will exist, cause fme is member of it senderMustBeMember = true;

		if ( (factionToggling != null) && (!factionToggling.equals( factiont.getTag())) ) {
			if(!FactionsPlus.permission.has(sender, "factionsplus.togglestate.others")) {
				sender.sendMessage(ChatColor.RED + "No permission!");
				return;
			}else {//here if either has that perm or is Op
				authallow|=sender.isOp();
			}
			//kinda done: investigate what's the desired behaviour here, seems odd...
			factiont = Factions.i.getByTag( factionToggling);//done: this will NPE later if inexistent faction			
			if (null == factiont) {
				sender.sendMessage(ChatColor.RED + "The faction `"+factionToggling+"` doesn't exist!");
				return;
			}else {
				if ( (!sender.isOp()) && (factiont.isSafeZone() || Utilities.isWarZone( factiont )) ) {
					sender.sendMessage( ChatColor.RED +"You may not change the state of WarZone and SafeZone !" );
					return;
				}
			}
		}
		
		authallow|=
			( ( Config._peaceful.membersCanToggleState._ )
				|| ( ( Config._peaceful.leadersCanToggleState._ ) && ( Utilities.isLeader( fme ) ) ) 
				|| ( ( Config._peaceful.officersCanToggleState._ ) && ( Utilities.isOfficer( fme ) ) ) );
		
		if ( !authallow ) {
			sender.sendMessage( ChatColor.RED + "Sorry, you do not have the allowed ranking(in your faction) to do that!" );
			return;
		}

		
		
		//done(presumably /f peaceful uses a specific permission node which is unset by default):
		//using "/f peaceful factiontag" from Factions will bypass the payment employed for "/f togglestate" here
		//see if we're considering peaceful and togglestate the same thing, or they use different permissions ?
		//ie. maybe only admins can use peaceful but any others can use togglestate (if different permissions are in effect)
		
		
		if(!factiont.isPeaceful()) {//done: is economy enabled ?!
			//if faction wasn't already peaceful, then we set it
			if ( (!Config._economy.enabled._)
					|| (payForCommand(Config._economy.costToToggleUpPeaceful._, "to set faction to peaceful", 
						"for setting faction `"+factiont.getTag()+"` to peaceful")) ) {
				
				Bridge.factions.setFlag( factiont, FactionsAny.FFlag.PEACEFUL,  Boolean.TRUE );
				
				sender.sendMessage("You have toggled the faction `"+factiont.getTag()+"` to Peaceful!");
			}
		} else {
			//faction was peaceful, we now remove this flag
			if ( (!Config._economy.enabled._) 
					|| (payForCommand(Config._economy.costToToggleDownPeaceful._, "to remove the peaceful status", 
						"for removing the peaceful flag from faction `"+factiont.getTag()+"`")) ) {
				Bridge.factions.setFlag( factiont, FactionsAny.FFlag.PEACEFUL,  Boolean.FALSE );
				sender.sendMessage("You have removed peaceful status from faction `"+factiont.getTag()+"` !");
			}
		}
		
	}//perform
}