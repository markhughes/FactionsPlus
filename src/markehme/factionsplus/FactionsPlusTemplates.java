package markehme.factionsplus;

import markehme.factionsplus.config.Config;

import org.bukkit.ChatColor;

public class FactionsPlusTemplates {
	public static String Go(String templateOption, String args[]) {
		String workingstring = "Invalid Template File for " + templateOption;
		
		if(templateOption == "announcement_message") {
			workingstring = Config._templates.announcement_message._;
		}
		
		if(templateOption == "warp_created") {
			workingstring = Config._templates.warp_created._;
		}
		
		if(templateOption == "notify_warp_created") {
			workingstring = Config._templates.notify_warp_created._;
		}
		
		if(templateOption == "jailed_message") {
			workingstring = Config._templates.jailed_message._;
		}
		
		if(templateOption == "warp_non_existant") {
			workingstring = Config._templates.warp_non_existant._;
		}
		
		if(templateOption == "warped_to") {
			workingstring = Config._templates.warped_to._;
		}
		
		if(templateOption == "warped_removed") {
			workingstring = Config._templates.warped_removed._;
		}
		
		if(templateOption == "warp_incorrect_password") {
			workingstring = Config._templates.warp_incorrect_password._;
		}
		
		if(templateOption == "create_warp_denied_badrank") {
			workingstring = Config._templates.create_warp_denied_badrank._;
		}
		
		if(templateOption == "create_warp_denied_badterritory") {
			workingstring = Config._templates.create_warp_denied_badterritory._;
		}
		
		if(templateOption == "warps_reached_max") {
			workingstring = Config._templates.warps_reached_max._;
		}
		
		if(templateOption == "warps_already_exists") {
			workingstring = Config._templates.warps_already_exists._;
		}
		
		if(templateOption == "faction_need") {
			workingstring = Config._templates.faction_need._;
		}
		
		
		workingstring = colorFormat(workingstring);
		
		if(args != null) {			
			if(args.length == 1) {
				workingstring = workingstring.replace("!1", args[0]);
	
				return(workingstring);
			}
			
			if(args.length == 2) {
				workingstring = workingstring.replace("!1", args[0]);
				workingstring = workingstring.replace("!2", args[1]);
				return(workingstring);
			}
			
			if(args.length == 3) {
				workingstring = workingstring.replaceAll("!1", args[0]);
				workingstring = workingstring.replaceAll("!2", args[1]);
				workingstring = workingstring.replaceAll("!3", args[2]);
	
				return(workingstring);
			}
			
			if(args.length == 4) {
				workingstring = workingstring.replace("!1", args[0]);
				workingstring = workingstring.replace("!2", args[1]);
				workingstring = workingstring.replace("!3", args[2]);
				workingstring = workingstring.replace("!4", args[4]);
	
				return(workingstring);		
			}
			
			if(args.length == 5) {
				workingstring = workingstring.replace("!1", args[0]);
				workingstring = workingstring.replace("!2", args[1]);
				workingstring = workingstring.replace("!3", args[2]);
				workingstring = workingstring.replace("!4", args[4]);
				workingstring = workingstring.replace("!5", args[5]);
				
				return(workingstring);		
			}
			

		}

		return workingstring;
		
	}
	
	public static String colorFormat(String a) {
		String b = a.replaceAll("<green>", ChatColor.GREEN + "");
		b = b.replaceAll("<red>", ChatColor.RED + "");
		b = b.replaceAll("<white>", ChatColor.WHITE + "");
		b = b.replaceAll("<purple>", ChatColor.DARK_PURPLE + "");
		b = b.replaceAll("<aqua>", ChatColor.AQUA + "");
		b = b.replaceAll("<black>", ChatColor.BLACK + "");
		b = b.replaceAll("<blue>", ChatColor.BLUE + "");
		b = b.replaceAll("<yellow>", ChatColor.YELLOW + "");
		b = b.replaceAll("<gray>", ChatColor.GRAY + "");
		b = b.replaceAll("<grey>", ChatColor.GRAY + "");
		
		return b;
		
	}
}
