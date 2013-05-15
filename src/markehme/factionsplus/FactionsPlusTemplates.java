package markehme.factionsplus;

import markehme.factionsplus.config.Config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class FactionsPlusTemplates {
	public static String Go(String templateOption, String args[]) {
		String workingstring = "Invalid Template File for " + templateOption;
		
		if(templateOption == "announcement_message") {
			workingstring = Config.templates.getString("announcement_message");

		}
		
		if(templateOption == "warp_created") {
			workingstring = Config.templates.getString("warp_created");
		}
		
		if(templateOption == "notify_warp_created") {
			workingstring = Config.templates.getString("notify_warp_created");
		}

		
		if(templateOption == "jailed_message") {
			workingstring = Config.templates.getString("jailed_message");
		}
		workingstring = colorFormat(workingstring);
		
		
		if(args.length == 2) {
			workingstring = workingstring.replace("$1", args[1]);
			workingstring = workingstring.replace("!1", args[1]);

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
	
	@SuppressWarnings("boxing")
	public static void createTemplatesFile() {
		try { 
			if(Config.templatesFile.exists()) {
				Config.templatesFile.delete();
			}
			
			Config.templatesFile.createNewFile();
			
			Config.templates = YamlConfiguration.loadConfiguration(Config.templatesFile);
			
			// For announcements 
			Config.templates.set("announcement_message", "<red>!1 <white>announced: !2");
			
			// For warps 
			Config.templates.set("warp_created", "<green>Warp <white>!1 <green>set for your Faction!");
			Config.templates.set("notify_warp_created", "!1 created a warp in your faction called !2");
			
			// For jail
			Config.templates.set("jailed_message", "<red>You have been Jailed! If you are unhappy with this faction, you can leave the Faction.");
			
			// Default value don't change
			Config.templates.set("doNotChangeMe", 3);
			
			Config.templates.save(Config.templatesFile);
			
		} catch (Exception e) {
	    	e.printStackTrace();
	    	FactionsPlusPlugin.info("ERROR: Couldn't create templates file.");
	    	return; 
	    }
	}
}
