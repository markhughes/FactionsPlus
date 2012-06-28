package markehme.factionsplus.listeners;

import java.util.*;

import markehme.factionsplus.*;
import markehme.factionsplus.FactionsBridge.*;
import markehme.factionsplus.extras.*;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.permissions.*;
import org.bukkit.plugin.*;

//import com.earth2me.essentials.*;
//import com.earth2me.essentials.api.*;
import com.massivecraft.factions.*;




public class TeleportsListener implements Listener {
	
	private static final Permission	permissionForHomeToEnemy	= new Permission( "factionsplus.allowTeleportingToEnemyLandViaHomeCommand", PermissionDefault.FALSE );
	private static final ChatColor constOneColor = ChatColor.DARK_RED;
	private static Listener	preventTeleports	= new TeleportsListener();
	
	private static boolean	reportSuccessfulByCommandTeleportsIntoEnemyLand;
	private static boolean disallowTeleportingToEnemyLandViaHomeCommand;
	private static boolean disallowTeleportingToEnemyLandViaEnderPeals;
	//XXX: this might bite if we add a /f reload config option, it won't be updated unless we call .init() [as it is now]
	
//	private static Essentials ess=null;
//	private static ESS_HAVE haveEssentials=ESS_HAVE.NOT_INITED;//since last Plugin.onEnable()
//	private enum ESS_HAVE {
//		INITED_AND_HAVE,
//		NOT_INITED,
//		INITED_AND_NOT_HAVE
//	}
	
	public static void init( Plugin plugin ) {
		if ( !plugin.isEnabled() ) {
			return;
		}
		if (null == Config.config) {
			throw FactionsPlusPlugin.bailOut( "bad call order while java coding, call this after config is loaded" );
		}
		
		reportSuccessfulByCommandTeleportsIntoEnemyLand=Config.config.getBoolean( Config.str_reportSuccessfulByCommandTeleportsIntoEnemyLand );
		disallowTeleportingToEnemyLandViaHomeCommand=Config.config.getBoolean( Config.str_disallowTeleportingToEnemyLandViaHomeCommand );
		disallowTeleportingToEnemyLandViaEnderPeals=Config.config.getBoolean( Config.str_disallowTeleportingToEnemyLandViaEnderPeals  );
		//TODO: implement this disallowTeleportingToEnemyLandViaEnderPeals  in the next hour
		
		if ( (!isHomeTracking())
		        && (!disallowTeleportingToEnemyLandViaEnderPeals)) {
			//don't hook if neither of the two are set
			return;
		}
		
		//must init these every time on reload
//		ess=null;
//		haveEssentials=ESS_HAVE.NOT_INITED;
		
		Bukkit.getPluginManager().registerEvents( preventTeleports, plugin );
	}
	
	private final static boolean isHomeTracking() {//private/final so it can be inlined by compiler, supposedly
	    return disallowTeleportingToEnemyLandViaHomeCommand || reportSuccessfulByCommandTeleportsIntoEnemyLand;
	}
	
//	/**
//	 * with lazy init
//	 * @return
//	 */
//	public static final Essentials getEssentialsInstance() {
//		if (ESS_HAVE.NOT_INITED == haveEssentials) {
//			//lazyly init or XXX: maybe add depend (not soft) in plugin.yml
//			Plugin essPlugin = Bukkit.getPluginManager().getPlugin("Essentials");
//			if ((null != essPlugin) && (essPlugin.isEnabled()) ) {
//				haveEssentials=ESS_HAVE.INITED_AND_HAVE;
//				ess=(Essentials)essPlugin;
//			}else{
//				haveEssentials=ESS_HAVE.INITED_AND_NOT_HAVE;
//			}
//		}
//		return ess;//can be null
//	}
	
//	private double getTeleportDelay(Player player) {
//		Essentials esi = getEssentialsInstance();
//		if (null != esi) {
//			return esi.getRanks().getTeleportDelay( esi.getUser( player ) );fail no such method 
//		}
//		return -1;
//	}
	
	//done: investigate what happens on reload(from console) when the hooks here were loaded and now the flag says don't load them
	//are the hooks still on ? since there's no deRegisterEvents... ? - ok, looks like they are gone on reload; 
	//I guess then only disablePlugin keeps them on still.
	
	// keeps track of the last executed command for each of the online players
	private Map<Player, String>	mapLastExecutedCommand	= new HashMap<Player, String>();
	//TODO: unsure here if it should be ConcurrentHashMap instead, i assume though they are not parallelizing events
	
	
	@EventHandler( priority = EventPriority.MONITOR )//MONITOR means it will be called last, after ie. HIGHEST
	public void onCommand( PlayerCommandPreprocessEvent event ) {
	    if (!isHomeTracking()) {
	        return;
	    }
		// this hook will trigger on any command ie. only those chat messages preceeded by "/"
		Player sender = event.getPlayer();
		mapLastExecutedCommand.put( sender, event.getMessage() );
	}
	
	
	@EventHandler( priority = EventPriority.MONITOR )
	public void onPlayerLogout( PlayerQuitEvent event ) {
	    if (!isHomeTracking()) {
	        return;
	    }
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
		if ( event.isCancelled() ) {
			// already cancelled, we don't care then, though some tricky plugin could cancel it in LOWEST and reenable it
			// in HIGHEST, thus totally bypassing us here; this could be avoided if we were to use MONITOR prio but not entirely
			// avoided
			return;
		}
        Player player = event.getPlayer();
		
		// FIXME: problem is if the player can execute another command before the teleport is issued such as if warmup delays
		//	are enabled for teleports, it will completely bypass this, because /home won't be the last seen command
		//	find another way to fix this: maybe deny all teleports(to enemy land) unless the last command is in the 
		//  whitelist of allowed ones  
        //this will be fixed soon
		
		TeleportCause cause = event.getCause();
		switch (cause) {
		case COMMAND:
		    if (isHomeTracking()) {
			// possibly could be the /home command
			// now we check if the last command the player executed was /home
			String lastExecutedCommandByPlayer = mapLastExecutedCommand.get( player );
			//this actually shouldn't be null here if tp cause was COMMAND if it ever is, then we need to investigate
			//( null != lastExecutedCommandByPlayer ) &&  (
			if ((disallowTeleportingToEnemyLandViaHomeCommand)&&(!Utilities.hasPermissionOrIsOp( player, permissionForHomeToEnemy ))) {
				//disallowed and no permission to bypass ? then check
				if ( lastExecutedCommandByPlayer.startsWith( "/home" ) ) {
					//TODO: think about having a list of commands here which when used to teleport into X territory 
					// would be denied; X is configurable too
					
					// it is home, then let us check if his home is in enemy territory
					Location targetLocation = event.getTo();
					if ( isEnemyLandAt( player, targetLocation ) ) {
						player.sendMessage( ChatColor.RED
							+ "You are not allowed to teleport to your /home which is now in enemy territory" );
						denyTeleport(event);
					}
				}
			}
			
			if (( reportSuccessfulByCommandTeleportsIntoEnemyLand )&&(!event.isCancelled())) {
				//yeah report even if player had bypass permission but only if it will be a successful teleport
				Location targetLocation = event.getTo();
				Faction fac = getFactionAt( targetLocation );
				if ( areEnemies( player, fac ) ) {
					FactionsPlusPlugin.info( constOneColor + "Player '" + ChatColor.DARK_AQUA+player.getName()
						+ constOneColor+"' teleported into enemy land faction '" + ChatColor.DARK_AQUA+fac.getTag() + constOneColor+"' using command: '"+ChatColor.AQUA
						+ lastExecutedCommandByPlayer+constOneColor+"'." );
				}
			}
		    }//homeTracking
			
			break;// cause COMMAND
			
		case ENDER_PEARL:
			if (disallowTeleportingToEnemyLandViaEnderPeals) {//not adding a perm for this
				Location targetLocation = event.getTo();
				if ( isEnemyLandAt( player, targetLocation ) ) {
					player.sendMessage( ChatColor.RED
						+ "You are not allowed to ender pearl teleport inside enemy territory" );
					denyTeleport(event);
				}
			}
			break;
		default:
			//unhandled cause ? do nothing
		}
	}
	
	private final void denyTeleport(PlayerTeleportEvent ptEvent) {
		ptEvent.setCancelled( true );
		// not just cancel it, make sure that the canceling isn't ignored
		// worst case they'll teleport in the same spot where the command was issued from
		Location from = ptEvent.getFrom();
		ptEvent.setTo( from );
	}
	
	private final boolean isEnemyLandAt(Player player, Location targetLocation) 
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
