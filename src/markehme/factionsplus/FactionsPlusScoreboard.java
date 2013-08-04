package markehme.factionsplus;

import java.util.ArrayList;
import java.util.Collections;

import markehme.factionsplus.config.Config;
import markehme.factionsplus.extras.FType;
import markehme.factionsplus.references.FP;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.FactionColls;


public class FactionsPlusScoreboard {
	
	public static String objective_name = "FP_Top_Factions";
	public static ArrayList<String> sFactions;
	public static Scoreboard scoreBoard = null;
	
	public static void setup() {
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask( FP.instance, new Runnable() {
			
			@Override
			public void run() {
				
				// Ensure there are players online to send a scoreboard to 
				if( ! ( Bukkit.getServer().getOnlinePlayers().length > 0) ) {
					return;
				}
				
				ScoreboardManager manager	= Bukkit.getScoreboardManager();
				scoreBoard		= manager.getMainScoreboard(); // don't make a new one or we'll overwrite them all!
				
				if( Config._extras._scoreboards.showScoreboardOfFactions._ ) {
					
					Objective objective = null;
					
					// This works with all score board plugin's. 
					
					// Check if `objective_name` exists in the objectives. 
					
					if( scoreBoard.getObjective( objective_name ) == null) {
						
						objective			= scoreBoard.registerNewObjective( objective_name, "dummy" );
						
						objective.setDisplayName( ChatColor.WHITE + "" + ChatColor.BOLD + "Top Factions" );
						objective.setDisplaySlot( DisplaySlot.SIDEBAR );
						
					} else {
						
						objective			= scoreBoard.getObjective( objective_name );
						
					}				
					
					sFactions = new ArrayList<String>(); // clear it, and start again. 
					
					int i = 0;
					
					String some_number;
					
					for( FactionColl currentFactionColl : FactionColls.get().getColls() ) {
						
					    for( Faction sFaction : currentFactionColl.getAll() ) {
					    	
					    	// We want normal factions
					    	if( FType.valueOf( sFaction ) == FType.FACTION ) {
					    		some_number = Math.floor( sFaction.getLandCount() + sFaction.getPowerBoost() + sFaction.getPowerMaxRounded() ) + "";
					    		if(some_number.length() == 1 ) { // changes power looking like "9" to "000009"
					    			some_number = "00000"+some_number;
					    			
					    		} else if(some_number.length() == 2 ) { // changes power looking like "15" to "000015"
					    			
					    			some_number = "0000"+some_number;
					    			
					    		} else if(some_number.length() == 3 ) { // changes power looking like "200" to "000200"
					    			
					    			some_number = "000"+some_number;
					    			
					    		} else if(some_number.length() == 4 ) { // changes power looking like "5555" to "005555"
					    			some_number = "00"+some_number;
					    			
					    		} else if(some_number.length() == 5 ) { // changes power looking like "99999" to "099999"
					    			
					    			some_number = "0"+some_number;
					    			
					    		}
					    		
					    		sFactions.add( some_number + "mooISplitStringsLuls123" + sFaction.getName() );
					    		
						    	i++;
						    	
					    	}
					    	
					    }
					    
					}
					
					// More than one Faction, so sort it. 
					if( i > 1  ) {
						
						Collections.sort( sFactions, Collections.reverseOrder() );
						
					}
					
					int LimitCount = 0;
					
					String[] workingString = null;
					String faction_name;
					int faction_power;
					
					int scoreboard_charater_limit = 16;
					
					
					for ( String cFaction : sFactions ) {
						workingString = cFaction.split( "mooISplitStringsLuls123" );
						
						faction_name = workingString[1].substring( 0, Math.min( workingString[1].length(), scoreboard_charater_limit ) );
						
						try {
							
							faction_power = Integer.parseInt( workingString[0].replace( ".0", "" ) );
							
						} catch( Exception e ) {
							
							faction_power = 0;
							
							FP.severe(e,  "Could not convert " + workingString[0] + " to int (faction power was assumed 0) - error should not have occured" );
							
						}
						
						switch( LimitCount ) {
						case 0:
							Score faction1 	= objective.getScore( Bukkit.getOfflinePlayer( faction_name ) );
							faction1.setScore( faction_power );
							break;
							
						case 1:
							Score faction2 	= objective.getScore( Bukkit.getOfflinePlayer( faction_name ) );
							faction2.setScore( faction_power );
							break;
							
						case 2:
							Score faction3 	= objective.getScore( Bukkit.getOfflinePlayer( faction_name ) );
							faction3.setScore( faction_power );
							break;
							
						case 3:
							Score faction4 	= objective.getScore( Bukkit.getOfflinePlayer( faction_name ) );
							faction4.setScore( faction_power );
							break;
							
						case 4:
							Score faction5 	= objective.getScore( Bukkit.getOfflinePlayer( faction_name ) );
							faction5.setScore( faction_power );
							break;
							
						}
						
						LimitCount++;
						
						// Has to be a better way to do this. 
						if( LimitCount > 4 ) {
							
							break;
							
						}
					}
				
				} else {

					
					try {
						
						scoreBoard.getObjective( objective_name ).unregister();
						
					} catch( Exception e ) {
						
						// Not sure if it'll throw an Exception on a non-existant scoreboard
						// so put it in a try catch 
						
					}
					
				}
				

				for ( Player p : Bukkit.getOnlinePlayers() ) {
										
					if( !( FP.permission.has( p, "factionsplus.hidesb." + p.getWorld().getName() ) && ! FP.permission.has( p, "factionsplus.hidesb" ) ) 
							|| FP.permission.has( p, "factionsplus.forcesb") ) {
						
						if( Config._extras._scoreboards.showScoreboardOfMap._ ) {
							
							// 16 character limit.. maybe map should not be too detailed
							// e.g. not have any names of the Factions. No colours.. 
							
							// this map is 16x16
							/*
							String mapLine_1	= "----------------";
							String mapLine_2	= "-------\\-------";
							String mapLine_3	= "-------\\-------";
							String mapLine_4	= "-------\\-------";
							String mapLine_5	= "-------.//------";
							String mapLine_6	= "----------------";
							String mapLine_7	= "----------------";
							String mapLine_8	= "----------------";
							String mapLine_9	= "----------------";
							String mapLine_10	= "----------------";
							String mapLine_11	= "----------------";
							String mapLine_12	= "----------------";
							String mapLine_13	= "----------------";
							String mapLine_14	= "----------------";
							String mapLine_15	= "----------------";
							String mapLine_16	= "----------------";
							*/
						}
								
						p.setScoreboard( scoreBoard );
						
					} else {
						
						try {
							
							p.getScoreboard().getObjective( objective_name ).unregister();
							
						} catch( Exception e ) {
							
							// Not sure if it'll throw an Exception on a non-existant scoreboard
							// so put it in a try catch 
							
						}
					}
				}
			}
			
	    }, 0L, Config._extras._scoreboards.secondsBetweenScoreboardUpdates._*20L );

	}
	
}
