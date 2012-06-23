package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;

import org.bukkit.ChatColor;

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
		
		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = true;
		
		this.setHelpShort("changes the Faction between peaceful");
		
	}
	
	// /f togglestate <faction>
	
	
	public void perform() {
		
		String factionToggling = this.argAsString(0);
		Faction factiont;
		
		if(!FactionsPlus.permission.has(sender, "factionsplus.togglestate.use")) {
			sender.sendMessage(ChatColor.RED + "No permission!");
			return;
		}
		
		if (factionToggling != null) {
			if(!FactionsPlus.permission.has(sender, "factionsplus.togglestate.others")) {
				sender.sendMessage(ChatColor.RED + "No permission!");
				return;
			}
			
			factiont = Factions.i.get(factionToggling);			
			
			Boolean authallow = false;
			
			if(FactionsPlus.config.getBoolean("leadersCanToggleState")) {
				if(fme.getRole().toString().contains("admin") || fme.getRole().toString().contains("LEADER")) { // 1.6.x
					authallow = true;
				}
			}
			
			if(FactionsPlus.config.getBoolean("officersCanToggleState")) {
				if(fme.getRole().toString().contains("mod") || fme.getRole().toString().contains("OFFICER")) {
					authallow = true;
				}
			}

			if(FactionsPlus.config.getBoolean("membersCanToggleState")) {
				authallow = true;
			}
			
			if(!authallow) {
				sender.sendMessage(ChatColor.RED + "Sorry, your ranking is not high enough to do that!");
				return;
			}
			
		} else {
			factiont = fme.getFaction();
		}
		
		if(factiont.isPeaceful()) {
			if(payForCommand(FactionsPlus.config.getInt("economy_costToToggleUpPeaceful"), "to set faction to peaceful", "for setting faction to peaceful")) {
				
				
				if(FactionsPlus.FactionsVersion.startsWith("1.7")) {
					//factiont.setFlag(com.massivecraft.factions.struct.FFlag.PEACEFUL, true);
				} else {
					factiont.setPeaceful(true);
				}
				
				sender.sendMessage("You have toggle the Faction to Peaceful!");
			}
		} else {
			if(payForCommand(FactionsPlus.config.getInt("economy_costToToggleDownPeaceful"), "to remove the peaceful status", "for setting faction to unpeaceful")) {
				if(FactionsPlus.FactionsVersion.startsWith("1.7")) {
					//factiont.setFlag(com.massivecraft.factions.struct.FFlag.PEACEFUL, false);
				} else {
					factiont.setPeaceful(false);
				}
				sender.sendMessage("You have removed peaceful status from the Faction!");
			}
			
		}
		
	}
}