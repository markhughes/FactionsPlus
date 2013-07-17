package markehme.factionsplus.Cmds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Typeo;
import markehme.factionsplus.config.sections.Section_Jails;
import markehme.factionsplus.references.FP;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitWorker;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;


public class CmdDebug extends FPCommand {
	public CmdDebug() {
		this.aliases.add("debug");
		
		this.optionalArgs.put("configdiff", "");
		this.errorOnToManyArgs = true;
		
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		
		this.setHelp("used to debug FactionsPlus");
		this.setDesc("used to debug FactionsPlus");
		
	}
	@Override
	public void performfp() {
		// TEMP, remove this completely after this inconsistency is fixed; if it's commented out it means it's not fixed
		
		/*
		if ( null != usender ) {
			Player player = Bukkit.getPlayerExact( usender.getId() );
			assert Utilities.getOnlinePlayerExact( usender ).equals(player);
			if ( null != player ) {
				String perm = Section_Jails.permissionNodeNameForCanJailUnjail;//"factionsplus.xyz"
				sender.sendMessage( player.getWorld().toString() );
				sender.sendMessage( "has perm: " + FactionsPlus.permission.playerHas( player, perm ) );
				sender.sendMessage( "has perm: " + FactionsPlus.permission.playerHas( (World)null, player.getName(), perm ) );
				sender.sendMessage( "has perm: "
					+ FactionsPlus.permission.playerHas( player.getWorld(), player.getName(), perm ) );
			}
		}
		*/
		
		if ( (null != usender) && (usender.isOnline()) && (!sender.isOp())) {
				return;
		}
		
		String param = this.arg(0);
		
		if ( ( param != null ) && ( param.trim().equalsIgnoreCase( "configdiff" ) ) ) {
			Typeo.showDiff( sender );
			
			return;
		}
		
		msg( "--- START ---");
		msg( "FactionsPlus Version: " + FP.version);
		msg( "Bukkit Version: " + Bukkit.getBukkitVersion());
		msg( "Bukkit Version: " + Bukkit.getServer().getVersion());
		List<BukkitWorker> workers = Bukkit.getScheduler().getActiveWorkers();
		msg( "Active Workers: "+workers.size());
		
		for ( BukkitWorker bukkitWorker : workers ) {
			msg("  workerOwner: "+bukkitWorker.getOwner()+" taskId="+bukkitWorker.getTaskId()
				+", "+bukkitWorker.getThread().getName());			
		}
		
		msg("Permissions: " + FactionsPlus.permission.getClass().getName());
		
		if (null != usender) {
			Faction f=usender.getFaction();
			if (null != f) {
				msg(Utilities.getCountOfWarps(f) + " warps for faction "+f.getName());
			}
		}
		msg("--- END ---");
		
		if( this.arg(0) != null ) {
			msg( this.arg(0) );
			if( arg(0).trim().equals( "sb" ) ) {
				msg("[TEST-DEBUG] Starting FP Debug Information");
				msg("[TEST-DEBUG] Console will contain important information");
				
				ArrayList<String> sFactions = new ArrayList<String>();
				
				int i = 0;
				
				msg("[TEST-DEBUG] Fetching Factions");
				
				String some_number;
				
				for( FactionColl currentFactionColl : FactionColls.get().getColls() ) {
					
				    for( Faction sFaction : currentFactionColl.getAll() ) {
				    	
				    	// we don't want these Factions at all.
				    	
				    	// Not Wilderness / SafeZone / Warzone
				    	if( Utilities.isNormalFaction( sFaction )) {
				    		some_number = sFaction.getPowerRounded()+"";
				    		
				    		if(some_number.length() == 1 ) { // changes power looking like "9" to "009"
				    			some_number = "00"+some_number;
				    		} else if(some_number.length() == 2 ) { // changes power looking like "15" to "015"
				    			some_number = "0"+some_number;
				    		}
				    		
				    		
				    		sFactions.add( some_number + "mooISplitStringsLuls123" + sFaction.getName() );
				    		msg( "[TEST-DEBUG] Fetched " + sFaction.getId() );
				    		FP.info( "[T-DBG] Faction was fetched into array: "  + sFaction.getId()  );
				    		FP.info( "[T-DBG]  Power Value: "+some_number);
					    	i++;
				    	}
				    }
				}
				
				
				// More than one Faction, so sort it. 
				if( i > 1  ) {
					msg( "[TEST-DEBUG] Detected > 1 so sorting " );
					FP.info( "[T-DBG] Detected > 1 so sorting");
					Collections.sort( sFactions, Collections.reverseOrder() );
				} else {
					msg( "[TEST-DEBUG] Detected 1 __NOT__ Sorting " );
					FP.info( "[T-DBG] Detected 1 __NOT__ Sorting");
				}
				
				int currentCount = 0;
				
				String[] workingString = null;
				String faction_name;
				int faction_power;
				
				int scoreboard_charater_limit = 16;
				
				
				FP.info( "[T-DBG] Array of Factions sorted: " );
				FP.info( "[T-DBG] " );
				FP.info( "[T-DBG] " );
				for ( String cFaction : sFactions ) {
					currentCount++;
					workingString = cFaction.split( "mooISplitStringsLuls123" );
					
					faction_name = workingString[1].substring( 0, Math.min( workingString[1].length(), scoreboard_charater_limit ) );
					faction_power = Integer.parseInt( workingString[0] );
					
					FP.info( "[T-DBG] " + currentCount + " [N]: " +faction_name );
					FP.info( "[T-DBG] " + currentCount + " [P]: " +faction_power );
					FP.info( "[T-DBG] " );
		
				}
			}
		}
	}
}
