package markehme.factionsplus.listeners;

import java.util.HashMap;
import java.util.Map;

import markehme.factionsplus.EssentialsIntegration;
import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusPlugin;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.extras.FType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Board;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.ps.PS;


/**
 * The TeleportsListener is used for all teleportaion denying! 
 * 
 * TODO: integrate with more /home /back plugins BESIDES essentials? 
 * TODO: extensive clean up of code (so messy and documented) 
 * TODO: universe support 
 *
 */
public class TeleportsListener implements Listener {
	
//	private static final Permission		permissionForHomeToEnemy	=
//																		new Permission(
//																			"factionsplus.allowTeleportingToEnemyLandViaHomeCommand",
//																			PermissionDefault.FALSE );
	private static final ChatColor		constOneColor				= ChatColor.DARK_RED;
	private static final String			homeCMD						= "/home";							// lowercase pls
	private static final String			ehomeCMD					= "/ehome";							// lowercase pls
	private static final String			defaultHardCodedHomeName	= "home";							// ie. /home does /home
																										// home
	private static final String			backCMD						= "/back";							// lowercase pls
	private static final String			ebackCMD					= "/eback";
	
	private static TeleportsListener	preventTeleports			= new TeleportsListener();
	private static boolean				tpInited					= false;
		
	
	public synchronized static void initOrDeInit(Plugin plugin) {
		if(!plugin.isEnabled() ) return;
		if(isInited()) return;
		
		preventTeleports.registerSelf(plugin);
		
		tpInited = true;
		
		FactionsPlus.debug("Started listening for teleport events");
	}
	
	
	private synchronized static void deInit() {
		preventTeleports.unregisterSelf();
		tpInited = false;
		FactionsPlus.debug("Stopped listening for teleport events");
	}
	
	
	public synchronized static boolean isInited() {
		return tpInited;
	}
	
	
	private final void registerSelf( Plugin plugin ) {
		Bukkit.getPluginManager().registerEvents( preventTeleports, plugin );
	}
	
	
	private final void unregisterSelf() {
		HandlerList.unregisterAll( preventTeleports );
	}
	
	
	public boolean doAction(UPlayer uPlayer, FPUConf fpUConf, Location checkLocation, String via) {
		Faction faction = Board.get(uPlayer.getUniverse()).getFactionAt(PS.valueOf(checkLocation));
		
		Rel rel = faction.getRelationTo(uPlayer.getFaction());
		
		if(fpUConf.denyTeleportIntoAlly.get(via) && rel.equals(Rel.ALLY)) {
			return true;
		}
		if(fpUConf.denyTeleportIntoEnemy.get(via) && rel.equals(Rel.ENEMY)) {
			return true;
		}
		if(fpUConf.denyTeleportIntoNeutral.get(via) && rel.equals(Rel.NEUTRAL)) {
			return true;
		}
		if(fpUConf.denyTeleportIntoSafeZone.get(via) && FType.valueOf(faction).equals(FType.SAFEZONE)) {
			return true;
		}
		if(fpUConf.denyTeleportIntoWarZone.get(via) && FType.valueOf(faction).equals(FType.WARZONE)) {
			return true;
		}
		
		return false;
	}
	
	
	// keeps track of the last executed command for each of the online players
	private Map<Player, String>	mapLastExecutedCommand	= new HashMap<Player, String>();	
	
	
	@EventHandler(priority = EventPriority.MONITOR ) // MONITOR means it will be called last, after ie. HIGHEST
	public void onCommand(PlayerCommandPreprocessEvent event) {
		
		Player playerInGame = event.getPlayer();
		
		UPlayer uPlayer = UPlayer.get(playerInGame);
		FPUConf fpUConf = FPUConf.get(uPlayer.getUniverse());
		
		String cmd = event.getMessage();
		
		Location whereTo = EssentialsIntegration.getLastLocation( playerInGame );
		Location safeTo = null;
		
		try {
			safeTo = EssentialsIntegration.getSafeDestination(whereTo);
		} catch(Exception e) {
			e.printStackTrace();
			
			playerInGame.sendMessage(ChatColor.RED + "[FactionsPlus] Internal error occurred calling Essentials, command ignored. Ask administrator to check console.");
			
			event.setCancelled(true);
			
			return;
		}	
		
		if(doAction(uPlayer, fpUConf, safeTo, "reportToConsole") || doAction(uPlayer, fpUConf, whereTo, "reportToConsole")) {
			mapLastExecutedCommand.put(playerInGame, cmd);
		}

		String realCmd = cmd.trim().toLowerCase();
		
		
		// Check /back and /eback
		if(EssentialsIntegration.isHooked()  && !playerInGame.isOp()) {
			if(realCmd.toLowerCase().startsWith(backCMD) || realCmd.toLowerCase().startsWith(ebackCMD)) {
				// We have a /back or /eback
				
				String[] ar = cmd.split( "\\s+" );// whitespace, one or more
				
				if(!ar[0].equalsIgnoreCase(backCMD)) return;
				if(!ar[0].equalsIgnoreCase(ebackCMD)) return;
				
				if(doAction(uPlayer, fpUConf, safeTo, "viaBack") || doAction(uPlayer, fpUConf, whereTo, "viaBack")) {
					uPlayer.msg(ChatColor.RED + "You are not allowed to go use back commands in that territory" );
					event.setCancelled(true);
					return;
				}
			}
		}
		
		// check /home and /ehome
		if(EssentialsIntegration.isHooked()  && !playerInGame.isOp()) {
			if ( realCmd.toLowerCase().startsWith(homeCMD) || realCmd.toLowerCase().startsWith(ehomeCMD) ) {
				// playerInGame.sendMessage( "oh hi" );
				
				String[] ar = cmd.split( "\\s+" );// whitespace, one or more
				// for ( int j = 0; j < ar.length; j++ ) {
				// System.out.println("!"+ar[j]+"!");
				// }
				if ( !ar[0].equalsIgnoreCase( homeCMD ) ) {
					// maybe it was /homesomething ie. not the /home command
					return;
				}
				String homeName = null;
				
				if ( ar.length == 1 ) {
					// no params, 2 cases: 1. it will list all homes, 2. it will do /home home if only default home is set
					if ( EssentialsIntegration.getHomesCount( playerInGame ) <= 1 ) {
						// even if no homes are set apparently... it will tp to bed !!
						homeName = defaultHardCodedHomeName;
					} else {
						// it's going to list the homes, because we're in case 1. we have multiple homes and /home will list
						// them
						return;
					}
				} else if ( ar.length == 2 ) {
					// just one parameter
					homeName = ar[1];
				} else {
					if ( ar.length > 2 ) {
						// too many params
						playerInGame.sendMessage( "You specified more than one parameter to command `" + ar[0] + "`" );
						event.setCancelled( true );
						return;
					}
					
				}
				
				assert null != homeName;
				assert !homeName.isEmpty();
				
				Location targetLocation = null;
				// using /home without having any homes set will try do /home bed
				// if you never used a bed but had homes set previously(which you lated deleted) then /home bed is something
				// around the lines of the last known home
				Location bedLocation = playerInGame.getBedSpawnLocation();
				
				int count = 0;
				while ( ( null == targetLocation ) && ( count <= 2 ) ) {
					try {
						targetLocation = EssentialsIntegration.getHomeForPlayer( playerInGame, homeName );
					} catch ( Exception e ) {
						e.printStackTrace();
						playerInGame.sendMessage( ChatColor.RED
							+ "[FactionsPlus] Internal error occurred calling Essentials, command ignored. Check console." );
						assert null == targetLocation;
					}
					
					if ( null == targetLocation ) {
						// means player's parameter was ignored because he has only the default home set, so it's like doing a
						// /home without params
						if ( homeName != defaultHardCodedHomeName ) {
							homeName = defaultHardCodedHomeName;
						} else {
							// it already was:
							targetLocation = bedLocation;
							// done: must also check if exact tp location isn't in faction land anyway due to 1 block difference
							// from bed location
							// the bed location should be X blocks away from enemy land to prevent exploit, X appears to be at
							// least 21 ffs
						}
					}
					
					count++;
				}
				
				
				// assert null != targetLocation;//even in this case, it will still tp to a the previous "home"
				if ( null == targetLocation ) {
					assert null == bedLocation : "else above loop failed";
					// this means player could've used "/home bed" even without any homes set or just "/home" without homes set
					// would also do that equivalent of "/home bed"
					// targetLocation=playerInGame.getBedSpawnLocation();
					
					// So, there was no location at all? So we don't need to cancel. As they'll get the default error message
					// from another plugin anyway.
					return;
				}
				
				Location potentiallyModifiedTarget = null;
				try {
					potentiallyModifiedTarget = EssentialsIntegration.getSafeDestination(targetLocation);
				} catch ( Exception e ) {
					e.printStackTrace();
					playerInGame.sendMessage( ChatColor.RED 
						+ "[FactionsPlus] Internal error occurred calling Essentials, command ignored. Check console." );
					event.setCancelled( true );
					assert null == potentiallyModifiedTarget;
					return;
				}
				
				// assert null != potentiallyModifiedTarget;
				if(doAction(uPlayer, fpUConf, targetLocation, "viaHome")) {
					uPlayer.msg(ChatColor.RED + "You are not allowed to go use home commands in that territory" );
					event.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerLogout( PlayerQuitEvent event ) {
		if(mapLastExecutedCommand.containsKey(event.getPlayer())) {
			mapLastExecutedCommand.remove( event.getPlayer() );
		}
	}
	
	
	// TODO: detect if essentials warmup>0 and suggest, on console, using boosCooldown for warmup to avoid bypassing this
	// prevention of /home into enemy land
	
	/**
	 * this will prevent teleports by "/home" command (only) if they land into enemy territory,
	 * unless you have a specific permission node<br />
	 * warning: if something happens between the command event and the actual teleport event, so that you can actually
	 * send another command inbetween those, then this prevention won't work; ie. essentials warmup can allow many other
	 * commands
	 * to be executed after doing /home until the warmup timer reaches 0 and the teleport event happens;
	 * use boosCooldown plugin for warmups for commands like /home /tp etc. instead of essentials' warmup, if you care about
	 * this
	 * prevention working
	 * 
	 * @param event
	 */
	// LOWEST=executed prior to others with like NORMAL or HIGHEST priorities
	// however if two plugins use the same, one of them will be executed first
	// but in this case we don't need a specific priority, tested with HIGHEST and it still worked with setCancelled()
	@EventHandler(
			priority = EventPriority.MONITOR )
	// using this to be the last or as last to be called as possible just to allow other plugins to deny
		public
		void onTeleport( PlayerTeleportEvent event ) {
		if ( event.isCancelled() ) {
			// already cancelled, we don't care then, though some tricky plugin could cancel it in LOWEST and reenable it
			// in HIGHEST, thus totally bypassing us here; this could be avoided if we were to use MONITOR prio but not entirely
			// avoided
			return;
		}
		Player player = event.getPlayer();
		UPlayer uPlayer = UPlayer.get(player);
		
		FPUConf fpUConf = FPUConf.get(uPlayer.getUniverse());
		
		// FIXME: problem is if the player can execute another command before the teleport is issued such as if warmup delays
		// are enabled for teleports, it will completely bypass this, because /home won't be the last seen command
		// find another way to fix this: maybe deny all teleports(to enemy land) unless the last command is in the
		// whitelist of allowed ones
		// this will be fixed soon
		
		TeleportCause cause = event.getCause();
		
		Location targetLocation = null;
		
		switch (cause) {
		case COMMAND:
				// possibly could be the /home command
				// now we check if the last command the player executed was /home
			
				String lastExecutedCommandByPlayer = mapLastExecutedCommand.get( player );
				
				//assert null != lastExecutedCommandByPlayer; apparently this can be NULL see issue 60
				// this actually shouldn't be null here if tp cause was COMMAND if it ever is, then we need to investigate
				// ( null != lastExecutedCommandByPlayer ) && (
				// checkIfHomeTeleportWouldLandInEnemyTerritory(event, lastExecutedCommandByPlayer);
				
				// yeah report even if player had bypass permission but only if it will be a successful teleport
				targetLocation = event.getTo();
				
				if(doAction(uPlayer, fpUConf, targetLocation, "reportToConsole")) {
					// these values are created to provide information tot he console (if required)
					Faction fac = BoardColls.get().getFactionAt( PS.valueOf( targetLocation ) );
					Rel rel = getRelation( player, fac );
					
					FactionsPlusPlugin.info(constOneColor + "Player '" + ChatColor.DARK_AQUA + player.getName()
							+ constOneColor + "'"+(player.isOp()?"(op)":"")+" teleported into " + rel + " land faction '" + ChatColor.DARK_AQUA
							+ fac.getName() + constOneColor + "'. Their last typed command: '" + ChatColor.AQUA
							+ lastExecutedCommandByPlayer + constOneColor + "'."+ChatColor.RESET+
							"\n(doesn't mean this command was the cause of the teleport!)");
					
					return;
				}
			
			break;// cause COMMAND
			
		case ENDER_PEARL:
				targetLocation = event.getTo();
				Faction fac = BoardColls.get().getFactionAt( PS.valueOf( targetLocation ) );
				
				Rel rel = getRelation( player, fac );
				
				if(doAction(uPlayer, fpUConf, targetLocation, "viaEnderPearls")) {
					player.sendMessage( ChatColor.RED + "You are not allowed to ender pearl teleport inside " + fac.getName()
							+ " territory" );
					
					denyTeleport( event );
					
					if(doAction(uPlayer, fpUConf, targetLocation, "reportToConsole")) {
						FactionsPlusPlugin.info( constOneColor + "Player '" + ChatColor.DARK_AQUA + player.getName()
								+ constOneColor + "'" + ( player.isOp() ? "(op)" : "" ) + " teleported into " + rel + " land faction '"
								+ ChatColor.DARK_AQUA + fac.getName() + constOneColor + "' using " + ChatColor.AQUA + "ender pearls" );

					}
				}
				
			break;
		default:
			// unhandled cause ? do nothing
		}
	}
	
	
	
	private final void denyTeleport( PlayerTeleportEvent ptEvent ) {
		ptEvent.setCancelled( true );
		// not just cancel it, make sure that the canceling isn't ignored
		// worst case they'll teleport in the same spot where the command was issued from
		Location from = ptEvent.getFrom();
		ptEvent.setTo( from );
	}
	
	private final Rel getRelation( Player player, Faction fac ) {
		assert null != player;
		assert null != fac;
		
		UPlayer fp = UPlayer.get( player ); // it will never be null
		
		assert null != fp;
		
		return fac.getRelationTo(fp);
	}
}
