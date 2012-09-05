package markehme.factionsplus.Cmds;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusPlugin;
import markehme.factionsplus.config.Config;

import org.bukkit.ChatColor;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Permission;

public class CmdRemoveWarp extends FPCommand {
	public CmdRemoveWarp() {
		/*
		 * This is a dummy function, it'll do exactly the same as /f deletewarp [warp]
		 */
		this.aliases.add("removewarp");
		this.aliases.add("deletewarp");
		this.aliases.add("unsetwarp");

		this.requiredArgs.add("name");
		//this.optionalArgs.put("on/off", "flip");

		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = false;
		
		this.setHelpShort("removes a warp");
	}
	
	@SuppressWarnings( "static-access" )
	@Override
	public void performfp() {
		String warpname = this.argAsString(0);
		
		if(!FactionsPlus.permission.has(sender, "factionsplus.deletewarp")) {
			sender.sendMessage(ChatColor.RED + "No permission!");
			return;
		}
		
		FPlayer fplayer = FPlayers.i.get(sender.getName());
		
		
		if(!Config._warps.canSetOrRemoveWarps(fplayer)) {
			sender.sendMessage(ChatColor.RED + "Sorry, your ranking is not high enough to remove warps!");
			return;
		}
		
		Faction currentFaction = fplayer.getFaction();
		
		try {
			boolean found = false;
			
			// Get out working files
			File currentWarpFile = new File(Config.folderWarps, currentFaction.getId());
			File currentWarpFileTMP = new File(Config.folderWarps,  currentFaction.getId() + ".tmp");
			
			// Scan through the warp file for the correct
			
			FileReader fr=null;
			Scanner scanner=null;
			try {
				fr = new FileReader( currentWarpFile );
				scanner = new Scanner( fr );
				while ( scanner.hasNextLine() ) {
					String[] warp = scanner.nextLine().split( ":" );
					if ( ( warp.length < 1 ) || ( !warp[0].equalsIgnoreCase( warpname ) ) ) {
						continue;
					}
					
					found = true;
					
					break;
				}
			}finally{
				if (null != scanner) {
					scanner.close();
				}
				if (null != fr) {
					try {
						fr.close();
					} catch ( IOException e ) {
						e.printStackTrace();
					}
				}
			}

//		    scanner.close();

		    if (!found) {
		    	return;
		    }
		    
		    if(Config._economy.costToDeleteWarp._ > 0.0d && !Config._economy.isHooked()) {
				if (!payForCommand(Config._economy.costToDeleteWarp._, "to remove this warp", "for removing the warp")) {
					return;
				}
			}


			PrintWriter wrt=null;
			BufferedReader rdr=null;
			try {
				wrt = new PrintWriter( new FileWriter( currentWarpFileTMP ) );
				rdr = new BufferedReader( new FileReader( currentWarpFile ) );
				
				String line;
				
				while ( ( line = rdr.readLine() ) != null ) {
					String[] warp = line.split( ":" );
					if ( ( warp.length >= 1 ) && ( warp[0].equalsIgnoreCase( warpname ) ) ) {
						continue;
					}
					
					wrt.println( line );
				}
				
				// wrt.close();
				// rdr.close();
			} finally {
				if ( null != rdr ) {
					try {
						rdr.close();
					} catch ( IOException e ) {
						e.printStackTrace();
					}
				}
				if ( null != wrt ) {
					wrt.close();
				}
			}
		
		    if (!currentWarpFile.delete()) {
		    	System.out.println("[FactionsPlus] Cannot delete " + currentWarpFile.getName());
		        return;
		    }
		    
		    if (!currentWarpFileTMP.renameTo(currentWarpFile)) {
		    	System.out.println("[FactionsPlus] Cannot rename " + currentWarpFileTMP.getName() + " to " + currentWarpFile.getName());
		        return;
		    }
		    
		} catch (Exception e) {
			FactionsPlusPlugin.info("Unexpected error " + e.getMessage());
			e.printStackTrace();
		    return;
		}
				
		sender.sendMessage(ChatColor.GREEN + "Poof!");
		

	}
}
