package markehme.factionsplus.Cmds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Scanner;

import markehme.factionsplus.FactionsPlus;

import org.bukkit.ChatColor;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;

public class CmdListWarps extends FCommand  {
	public CmdListWarps() {
		this.aliases.add("listwarps");

		//this.requiredArgs.add("name");
		this.optionalArgs.put("faction", "string");

		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = true;
		
		this.setHelpShort("list warps in a Faction");
		
	}
	
	public void perform() {
		Faction currentFaction = myFaction;
		
		if(this.argAsString(0) != null) {
			if(!FactionsPlus.permission.has(fme.getPlayer(), "factionsplus.listwarps")) {
				fme.msg("No permission!");
				return;
			}
			
			currentFaction = Factions.i.get(argAsString(0));
		}
		
		if(currentFaction.isNone()) {
			fme.msg("This Faction does not exist.");
			return;
			
		}
		File currentWarpFile = new File(FactionsPlus.folderWarps, currentFaction.getId());
		
	    if (!currentWarpFile.exists()) {
	    	sender.sendMessage(ChatColor.RED + "Your faction has no warps!");
	        return;
	    }
	    
	    try {
	    	FileInputStream fis = new FileInputStream(new File(FactionsPlus.folderWarps, currentFaction.getId()));
	    	int b = fis.read();
	    	
	    	if (b == -1) {
	    		sender.sendMessage(ChatColor.RED + "Your faction has no warps!");
	    		return;
	    	}
	    } catch (Exception e) {
	    	fme.msg("Internal error (-90571)");
	    	return;
	    }
	    
	    try {
	    	Scanner scanner = new Scanner(new FileReader(currentWarpFile));
	        String buffer = ChatColor.RED + "Your Factions warps: " + ChatColor.WHITE;
	        boolean warps = false;
	        while (scanner.hasNextLine()) {
	        	String item = scanner.nextLine();
	        	if(item.trim() != "") {
	        		String[] items = item.split(":");
	        		if (items.length > 0) {
	        			if (buffer.length() + items[0].length() + 2 >= 256) {
	        				sender.sendMessage(buffer);
	        				buffer = items[0] + ", ";
	        			} else {
	        				buffer = buffer + items[0] + ", ";
	        				warps = true;
	        			}
	        		}
	        	}
	        	
	        }
	        if(warps){
	        	buffer = buffer.substring(0, buffer.length() - 2);
	        	buffer += ". ";
	        }
	        sender.sendMessage(buffer);
	        scanner.close();
	    } catch (Exception e) {
	    	FactionsPlus.info("Cannot create file " + currentWarpFile.getName() + " - " + e.getMessage());
	    	
	        sender.sendMessage(ChatColor.RED + "An internetal error occured (03)");
	        
	        e.printStackTrace();
	    }

	}

}
