package markehme.factionsplus;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class FactionsPlusTemplates {
	public static String Go(String templateOption, String args[]) {
		String workingstring = "Invalid Template File for " + templateOption;
		
		if(templateOption == "announcement_message") {
			workingstring = FactionsPlus.templates.getString("announcement_message");

		}
		
		if(templateOption == "warp_created") {
			workingstring = FactionsPlus.templates.getString("warp_created");
		}
		
		if(templateOption == "notify_warp_created") {
			workingstring = FactionsPlus.templates.getString("notify_warp_created");
		}

		
		if(templateOption == "jailed_message") {
			workingstring = FactionsPlus.templates.getString("jailed_message");
		}
		workingstring = colorFormat(workingstring);
		
		
		if(args.length == 2) {
			workingstring = workingstring.replace("$1", args[1]);
			return(workingstring);
		}
		
		if(args.length == 3) {
			workingstring = workingstring.replace("!1", args[1]);
			workingstring = workingstring.replace("!2", args[2]);
			return(workingstring);
		}
		
		if(args.length == 4) {
			workingstring = workingstring.replaceAll("!1", args[1]);
			workingstring = workingstring.replaceAll("!2", args[2]);
			workingstring = workingstring.replaceAll("!3", args[3]);

			return(workingstring);
		}
		
		if(args.length == 5) {
			workingstring = workingstring.replace("!1", args[1]);
			workingstring = workingstring.replace("!2", args[2]);
			workingstring = workingstring.replace("!3", args[3]);
			workingstring = workingstring.replace("!4", args[4]);

			return(workingstring);		
		}

		return workingstring;

		
		
	}
	
	public static String colorFormat(String a) {
		a = a.replaceAll("<green>", ChatColor.GREEN + "");
		a = a.replaceAll("<red>", ChatColor.RED + "");
		a = a.replaceAll("<white>", ChatColor.WHITE + "");
		a = a.replaceAll("<purple>", ChatColor.DARK_PURPLE + "");
		a = a.replaceAll("<aqua>", ChatColor.AQUA + "");
		a = a.replaceAll("<black>", ChatColor.BLACK + "");
		a = a.replaceAll("<blue>", ChatColor.BLUE + "");
		a = a.replaceAll("<yellow>", ChatColor.YELLOW + "");
		a = a.replaceAll("<gray>", ChatColor.GRAY + "");
		a = a.replaceAll("<grey>", ChatColor.GRAY + "");
		
		return a;
		
	}
	
	@SuppressWarnings("boxing")
	public static void createTemplatesFile() {
		try { 
			if(FactionsPlus.templatesFile.exists()) {
				FactionsPlus.templatesFile.delete();
			}
			
			FactionsPlus.templatesFile.createNewFile();
			
			FactionsPlus.templates = YamlConfiguration.loadConfiguration(FactionsPlus.templatesFile);
			
			// For announcements 
			FactionsPlus.templates.set("announcement_message", "<red>!1 <white>announced: !2");
			
			// For warps 
			FactionsPlus.templates.set("warp_created", "<green>Warp <white>!1 <green>set for your Faction!");
			FactionsPlus.templates.set("notify_warp_created", "!1 created a warp in your faction called !2");
			
			// For jail
			FactionsPlus.templates.set("jailed_message", "<red>You have been Jailed! If you are unhappy with this faction, you can leave the Faction.");
			
			// Default value don't change
			FactionsPlus.templates.set("doNotChangeMe", 3);
			
			FactionsPlus.templates.save(FactionsPlus.templatesFile);
			
		} catch (Exception e) {
	    	e.printStackTrace();
	    	FactionsPlus.info("ERROR: Couldn't create templates file.");
	    	return; 
	    }
	}
}
