package markehme.factionsplus;

import java.util.*;

import markehme.factionsplus.extras.*;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.permissions.*;
import org.bukkit.plugin.*;

import com.massivecraft.factions.*;




public class TeleportsListener implements Listener {
	
	private static final Permission	permissionForHomeToEnemy	= new Permission( "factionsplus.allowTeleportingToEnemyLandViaHomeCommand" );
	private static final String configForDisallowHomeToEnemy    = "disallowTeleportingToEnemyLandViaHomeCommand";
	private static final String	configFor_reportSuccessfulByCommandTeleportsIntoEnemyLand	= "reportSuccessfulByCommandTeleportsIntoEnemyLand";
	private static final ChatColor constOneColor = ChatColor.DARK_RED;
	private static Listener	preventTeleports	= new TeleportsListener();
	
	private static boolean	reportSuccessfulByCommandTeleportsIntoEnemyLand;
	private static boolean disallowTeleportingToEnemyLandViaHomeCommand;
	//XXX: this might bite if we add a /f reload config option, it won't be updated unless we call .init() [as it is now]
	
	
	public static void init( Plugin plugin ) {
		if ( !plugin.isEnabled() ) {
			return;
		}
		if (null == FactionsPlus.config) {
			throw FactionsPlus.bailOut( "bad call order while java coding, call this after config is loaded" );
		}
		
		reportSuccessfulByCommandTeleportsIntoEnemyLand=FactionsPlus.config.getBoolean( configFor_reportSuccessfulByCommandTeleportsIntoEnemyLand );
		disallowTeleportingToEnemyLandViaHomeCommand=FactionsPlus.config.getBoolean( configForDisallowHomeToEnemy );
		
		if ( ( !disallowTeleportingToEnemyLandViaHomeCommand ) && ( !reportSuccessfulByCommandTeleportsIntoEnemyLand ) ) {
			//don't hook if neither of the two are set
			return;
		}
		
		Bukkit.getServer().getPluginManager().registerEvents( preventTeleports, plugin );
	}
	//done: investigate what happens on reload(from console) when the hooks here were loaded and now the flag says don't load them
	//are the hooks still on ? since there's no deRegisterEvents... ? - ok, looks like they are gone on reload; 
	//I guess then only disablePlugin keeps them on still.
	
	// keeps track of the last executed command for each of the online players
	private Map<Player, String>	mapLastExecutedCommand	= new HashMap<Player, String>();
	//TODO: unsure here if it should be ConcurrentHashMap instead, i assume though they are not parallelizing events
	
	
	@EventHandler( priority = EventPriority.MONITOR )//MONITOR means it will be called last, after ie. HIGHEST
	public void onCommand( PlayerCommandPreprocessEvent event ) {
		// this hook will trigger on any command ie. only those chat messages preceeded by "/"
		Player sender = event.getPlayer();
		mapLastExecutedCommand.put( sender, event.getMessage() );
	}
	
	
	@EventHandler( priority = EventPriority.MONITOR )
	public void onPlayerLogout( PlayerQuitEvent event ) {
		// this hook will trigger whenever a player quits/disconnects
		// this will prevent the map from getting too big by no longer keeping track of dc-ed players' last executed cmd
		mapLastExecutedCommand.remove( event.getPlayer() );
	}
	
	//TODO: detect if essentials warmup>0 and suggest, on console, using boosCooldown for warmup to avoid bypassing this prevention of /home into enemy land
	
	/**
	 * this will prevent teleports by "/home" command (only) if they land into enemy territory,
	 * unless you have a specific permission node<br />
	 * warning: if something happens between the command event and the actual teleport event, so that you can actually
	 * send another command inbetween those, then this prevention won't work; ie. essentials warmup can allow many other commands
	 * to be executed after doing /home   until the warmup timer reaches 0 and the teleport event happens;
	 * use boosCooldown plugin for warmups for commands like /home /tp etc. instead of essentials' warmup, if you care about this 
	 * prevention working 
	 * 
	 * @param event
	 */
	// LOWEST=executed prior to others with like NORMAL or HIGHEST priorities
	// however if two plugins use the same, one of them will be executed first
	// but in this case we don't need a specific priority, tested with HIGHEST and it still worked with setCancelled()
	@EventHandler( priority = EventPriority.MONITOR )//using this to be the last or as last to be called as possible just to allow other plugins to deny
	public void onTeleport( PlayerTeleportEvent event ) {
		Player player = event.getPlayer();
		if ( event.isCancelled() ) {
			// already cancelled, we don't care then, though some tricky plugin could cancel it in LOWEST and reenable it
			// in HIGHEST, thus totally bypassing us here; this could be avoided if we were to use MONITOR prio but not entirely
			// avoided
			return;
		}
		
		// FIXME: problem is if the player can execute another command before the teleport is issued such as if warmup delays
		//	are enabled for teleports, it will completely bypass this, because /home won't be the last seen command
		//	find another way to fix this: maybe deny all teleports(to enemy land) unless the last command is in the 
		//  whitelist of allowed ones  
		
		TeleportCause cause = event.getCause();
		if ( cause == PlayerTeleportEvent.TeleportCause.COMMAND ) {
			// possibly could be the /home command
			// now we check if the last command the player executed was /home
			String lastExecutedCommandByPlayer = mapLastExecutedCommand.get( player );
			//this actually shouldn't be null here if tp cause was COMMAND if it ever is, then we need to investigate
			//( null != lastExecutedCommandByPlayer ) &&  (
			if ((disallowTeleportingToEnemyLandViaHomeCommand)&&(!player.hasPermission( permissionForHomeToEnemy ))) {
				//disallowed and no permission to bypass ? then check
				if ( lastExecutedCommandByPlayer.startsWith( "/home" ) ) {
					//TODO: think about having a list of commands here which when used to teleport into X territory 
					// would be denied; X is configurable too
					
					// it is home, then let us check if his home is in enemy territory
					Location targetLocation = event.getTo();
					if ( isEnemyLandAt( player, targetLocation ) ) {
						player.sendMessage( ChatColor.RED
							+ "You are not allowed to teleport to your /home which is now in enemy territory" );
						event.setCancelled( true );
						// not just cancel it, make sure that the canceling isn't ignored
						// worst case they'll teleport in the same spot where the command was issued from
						Location from = event.getFrom();
						event.setTo( from );
					}
				}
			}
			
			if (( reportSuccessfulByCommandTeleportsIntoEnemyLand )&&(!event.isCancelled())) {
				//yeah report even if player had bypass permission but only if it will be a successful teleport
				Location targetLocation = event.getTo();
				Faction fac = getFactionAt( targetLocation );
				if ( areEnemies( player, fac ) ) {
					FactionsPlus.info( constOneColor + "Player '" + ChatColor.DARK_AQUA+player.getName()
						+ constOneColor+"' teleported into enemy land faction '" + ChatColor.DARK_AQUA+fac.getTag() + constOneColor+"' using command: '"+ChatColor.AQUA
						+ lastExecutedCommandByPlayer+constOneColor+"'." );
				}
			}
		}
	}
	
	private boolean isEnemyLandAt(Player player, Location targetLocation) 
	{
		Faction factionAtTarget = getFactionAt(targetLocation);//Board.getFactionAt( new FLocation( targetLocation ) );
		return areEnemies(player, factionAtTarget);
	}
	
	private boolean areEnemies(Player player, Faction faction) 
	{
		FPlayer fp = FPlayers.i.get( player );//should be able to get offline players too, js
		if ( FactionsAny.Relation.ENEMY == Bridge.factions.getRelationBetween( faction, fp ) ) {
			return true;
		}else{
			return false;
		}
	}
	
	private Faction getFactionAt( Location targetLocation) {
		return Board.getFactionAt( new FLocation( targetLocation ) );
	}
}
