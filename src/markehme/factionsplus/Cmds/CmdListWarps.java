package markehme.factionsplus.Cmds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.Cmds.req.ReqAtLeastOneWarp;
import markehme.factionsplus.config.Config;
import markehme.factionsplus.references.FPP;

import org.bukkit.ChatColor;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;

public class CmdListWarps extends FPCommand  {
	public CmdListWarps() {
		this.aliases.add( "listwarps" );
		
		this.optionalArgs.put( "faction", "string" );
		this.errorOnToManyArgs = true;
		
		this.addRequirements( ReqFactionsEnabled.get() );
		this.addRequirements( ReqIsPlayer.get() );
		this.addRequirements( ReqHasFaction.get() );
		
		this.setHelp( "list warps in a Faction" );
		this.setDesc( "list warps in a Faction" );
	}
	
	@Override
	public void performfp() {
		Faction currentFaction = usender.getFaction();
		
		if( this.arg(0) != null ) {
			if( ! FactionsPlus.permission.has( Utilities.getOnlinePlayerExact( usender ), "factionsplus.listwarps" ) ) {
				msg( "No permission!" );
				
				return;
			}
			
			currentFaction = Faction.get( arg( 0 )  );
		}
		
		if( Utilities.isWilderness( currentFaction ) ) {
			msg( "This Faction does not exist." );
			
			return;
		}
		
		File currentWarpFile = new File( Config.folderWarps, currentFaction.getId() );
	    
		if( !currentWarpFile.exists() ) {
			msg( ChatColor.RED + "Your Faction has no warps!" );
			
			return;
		}
		
	    FileInputStream fis = null;
	    
	    try {
	    	fis = new FileInputStream( new File( Config.folderWarps, currentFaction.getId() ) );
	    	int b = fis.read();
	    	
	    	if ( b == -1 ) {
	    		msg( ChatColor.RED + "Your faction has no warps!" );
	    		return;
	    	}
	    } catch ( Exception e ) {
	    	msg( "Internal error (LW:01)" );
	    	
	    	return;
	    } finally {
	    	if ( null != fis ) {
	    		try {
					fis.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
	    	}
	    }
	    
	    Scanner scanner = null;
	    FileReader fr = null;
	    
	    try {
	    	fr = new FileReader(currentWarpFile);
	    	scanner = new Scanner(fr);
	        
	    	String buffer = ChatColor.RED + "Your Factions warps: " + ChatColor.WHITE;
	        
	        boolean warps = false;
	        
	        while( scanner.hasNextLine() ) {
	        	String item = scanner.nextLine();
	        	
	        	if( ! item.trim().isEmpty() ) {
	        		String[] items = item.split( ":" );
	        		
	        		if ( items.length > 0 ) {
	        			if ( buffer.length() + items[0].length() + 2 >= 256 ) {
	        				
	        				msg( buffer );
	        				
	        				buffer = items[0] + ", ";
	        				
	        			} else {
	        				
	        				buffer = buffer + items[0] + ", ";
	        				
	        				warps = true;
	        			}
	        		}
	        	}
	        	
	        }
	        
	        if( warps ){
	        	buffer = buffer.substring( 0, buffer.length() - 2 );
	        	buffer += ". ";
	        }
	        
	        msg(buffer);
	        
	    } catch ( Exception e ) {
	    	FPP.info( "Cannot create file " + currentWarpFile.getName() + " - " + e.getMessage() );
	    	
	        msg( ChatColor.RED + "An internal error occured (LW:02)" );
	        
	        e.printStackTrace();
	    } finally {
	    	
	    	if ( null != scanner ) {
	    		scanner.close();
	    	}
	    	
	    	if ( null != fr ) {
	    		try {
					fr.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
	    	}
	    }
	}
}
