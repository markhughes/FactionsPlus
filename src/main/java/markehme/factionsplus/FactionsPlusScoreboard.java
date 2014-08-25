package markehme.factionsplus;

import java.util.ArrayList;
import java.util.Collections;

import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.MCore.MConf;
import markehme.factionsplus.extras.FType;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.util.Txt;


public class FactionsPlusScoreboard {
	
	public static String objective_name = "FactionsPlus_TF";
	public static Scoreboard scoreBoard = null;
	
	private static String strSplit = "[FP Str Splitter]";
	
	public static void setup() {
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(FactionsPlus.instance, new Runnable() {
			
			@Override
			public void run() {
				
				// Ensure there are players online to send a scoreboard to 
				if( ! ( Bukkit.getServer().getOnlinePlayers().size() > 0) ) {
					return;
				}
				
				ScoreboardManager manager	= Bukkit.getScoreboardManager();
				scoreBoard		= manager.getMainScoreboard(); // don't make a new one or we'll overwrite them all!
				
					
					Objective objective = null;
					
					// This works with all score board plugin's. 
					
					// Check if `objective_name` exists in the objectives. 
					
					if(scoreBoard.getObjective(objective_name) == null) {
						objective = scoreBoard.registerNewObjective(objective_name, "dummy");
					} else {
						objective = scoreBoard.getObjective(objective_name);
					}
					
					
					objective.setDisplayName(Txt.parse(LConf.get().scoreboardTitle));
					objective.setDisplaySlot( DisplaySlot.SIDEBAR );
										
					ArrayList<String> sFactions = new ArrayList<String>(); 
					sFactions.clear();
					
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
								
								sFactions.add( some_number + strSplit + sFaction.getName() );
								
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
						workingString = cFaction.split(strSplit);
						
						faction_name = workingString[1].substring( 0, Math.min( workingString[1].length(), scoreboard_charater_limit ) );
						
						try {
							
							faction_power = Integer.parseInt( workingString[0].replace( ".0", "" ) );
							
						} catch( Exception e ) {
							
							faction_power = 0;
							
							FactionsPlus.severe(e, "Could not convert " + workingString[0] + " to int (faction power was assumed 0) - error should not have occured" );
							
						}
						
						switch( LimitCount ) {
						case 0:
							Score faction1 	= objective.getScore(faction_name);
							faction1.setScore( faction_power );
							break;
							
						case 1:
							Score faction2 	= objective.getScore(faction_name);
							faction2.setScore( faction_power );
							break;
							
						case 2:
							Score faction3 	= objective.getScore(faction_name);
							faction3.setScore( faction_power );
							break;
							
						case 3:
							Score faction4 	= objective.getScore(faction_name);
							faction4.setScore( faction_power );
							break;
							
						case 4:
							Score faction5 	= objective.getScore(faction_name);
							faction5.setScore( faction_power );
							break;
							
						}
						
						LimitCount++;
						
						// Has to be a better way to do this. 
						if( LimitCount > 4 ) {
							
							break;
							
						}
					}
				
					
				

				for ( Player p : Bukkit.getOnlinePlayers() ) {
										
					if( !( FactionsPlus.permission.has( p, "factionsplus.hidesb." + p.getWorld().getName() ) && ! FactionsPlus.permission.has( p, "factionsplus.hidesb" ) ) 
							|| FactionsPlus.permission.has( p, "factionsplus.forcesb") ) {
						
						
						// Yes - this is proper per-universe support for scoreboards. 
						if(FPUConf.get(UPlayer.get(p).getUniverse()).scoreboardTopFactions) {
							p.setScoreboard(scoreBoard);
						}
						
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
			
		}, 0L, MConf.get().scoreboardUpdate*20L );

	}
	
}
