package markehme.factionsplus.Cmds;

import java.io.*;
import java.util.*;

import markehme.factionsplus.*;
import markehme.factionsplus.config.*;

import org.bukkit.*;

import com.massivecraft.factions.*;
import com.massivecraft.factions.cmd.*;
import com.massivecraft.factions.struct.*;

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
	
	@Override
	public void perform() {
		Faction currentFaction = myFaction;
		
		if(this.argAsString(0) != null) {
			if(!FactionsPlus.permission.has(Utilities.getOnlinePlayerExact(fme), "factionsplus.listwarps")) {
				fme.msg("No permission!");
				return;
			}
			
			currentFaction = Factions.i.get(argAsString(0));
		}
		
		if(Utilities.isWilderness(currentFaction)) {
			fme.msg("This Faction does not exist.");
			return;
			
		}
		File currentWarpFile = new File(Config.folderWarps, currentFaction.getId());
		
	    if (!currentWarpFile.exists()) {
	    	sender.sendMessage(ChatColor.RED + "Your faction has no warps!");
	        return;
	    }
	    
	    FileInputStream fis=null;
	    try {
	    	fis = new FileInputStream(new File(Config.folderWarps, currentFaction.getId()));
	    	int b = fis.read();
	    	
	    	if (b == -1) {
	    		sender.sendMessage(ChatColor.RED + "Your faction has no warps!");
	    		return;
	    	}
	    } catch (Exception e) {
	    	fme.msg("Internal error (-90571)");
	    	return;
	    }finally{
	    	if (null != fis) {
	    		try {
					fis.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
	    	}
	    }
	    
	    Scanner scanner=null;
	    FileReader fr = null;
	    try {
	    	fr=new FileReader(currentWarpFile);
	    	scanner = new Scanner(fr);
	        String buffer = ChatColor.RED + "Your Factions warps: " + ChatColor.WHITE;
	        boolean warps = false;
	        while (scanner.hasNextLine()) {
	        	String item = scanner.nextLine();
	        	if(!item.trim().isEmpty()) {
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
//	        scanner.close();
	    } catch (Exception e) {
	    	FactionsPlusPlugin.info("Cannot create file " + currentWarpFile.getName() + " - " + e.getMessage());
	    	
	        sender.sendMessage(ChatColor.RED + "An internal error occured (03)");
	        
	        e.printStackTrace();
	    }finally{
	    	if (null != scanner) {
	    		scanner.close();//technically this would also close fr, but I can't really suppress warnings on the entire method
	    	}
	    	if (null != fr) {
	    		try {
					fr.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
	    	}
	    }

	}

}
