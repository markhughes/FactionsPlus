package markehme.factionsplus.listeners;

import java.util.*;

import markehme.factionsplus.*;
import markehme.factionsplus.FactionsBridge.*;
import markehme.factionsplus.FactionsBridge.FactionsAny.Relation;
import markehme.factionsplus.config.*;
import markehme.factionsplus.extras.*;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.material.*;
import org.bukkit.permissions.*;
import org.bukkit.plugin.*;

import com.avaje.ebeaninternal.server.subclass.*;
import com.earth2me.essentials.*;
import com.massivecraft.factions.*;



public class TeleportsListener implements Listener {
	
//	private static final Permission		permissionForHomeToEnemy	=
//																		new Permission(
//																			"factionsplus.allowTeleportingToEnemyLandViaHomeCommand",
//																			PermissionDefault.FALSE );
	private static final ChatColor		constOneColor				= ChatColor.DARK_RED;
	private static final String			homeCMD						= "/home";							// lowercase pls
	private static final String			defaultHardCodedHomeName	= "home";							// ie. /home does /home
																										// home
	private static TeleportsListener	preventTeleports			= new TeleportsListener();
	private static boolean				tpInited					= false;
	
	
	// XXX: this might bite if we add a /f reload config option, it won't be updated unless we call .init() [as it is now]
	
	
	public synchronized static void init( Plugin plugin ) {
		if ( !plugin.isEnabled() ) {
			return;
		}
		if ( !Config.isLoaded() ) {
			throw FactionsPlusPlugin.bailOut( "bad call order while java coding, call this after config is loaded" );
		}
		
		if ( !Config._teleports.isAnySet() ) {
			// don't hook if neither of the two are set
			if ( isInited() ) {
				deInit();
			}
			return;
		}
		
		// if ( !EssentialsIntegration.isHooked() ) {
		// FactionsPlus.warn( "Due to failing to hook into Essentials plugin" + " the following enabled config option "
		// + ( EssentialsIntegration.isLoadedButNotEnabled() ? "MAY" : "WILL" ) + " has no effect: "
		// + Config._teleports._disallowInto.enemyViaHome._dottedName_asString);
		// // + "\nHowever you may try /f reloadfp to cause this recheck" );// FIXME:
		// }
		
		
		preventTeleports.registerSelf( plugin );
		tpInited = true;
	}
	
	
	private synchronized static void deInit() {
		preventTeleports.unregisterSelf();
		tpInited = false;
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
	
	
	private final static boolean isHomeTracking() {// private/final so it can be inlined by compiler, supposedly
		return Config._teleports.shouldReportCommands() || Config._teleports.shouldPreventHomeTelepors();
	}
	
	
	private final static boolean isEnderPearling() {
		return Config._teleports.shouldPreventEnderPearlsTeleports();
	}
	
	
	
	// done: investigate what happens on reload(from console) when the hooks here were loaded and now the flag says don't load
	// them
	// are the hooks still on ? since there's no deRegisterEvents... ? - ok, looks like they are gone on reload;
	// I guess then only disablePlugin keeps them on still.
	
	// keeps track of the last executed command for each of the online players
	private Map<Player, String>	mapLastExecutedCommand	= new HashMap<Player, String>();
	
	
	// TODO: unsure here if it should be ConcurrentHashMap instead, i assume though they are not parallelizing events
	
	
	@EventHandler(
			priority = EventPriority.MONITOR )
	// MONITOR means it will be called last, after ie. HIGHEST
		public
		void onCommand( PlayerCommandPreprocessEvent event ) {// XXX: doesn't trigger on console commands
		if ( !isHomeTracking() ) {
			return;
		}
		// this hook will trigger on any command ie. only those chat messages preceded by "/"
		Player playerInGame = event.getPlayer();
		// System.out.println(sender+" "+sender.getClass());
		
		String cmd = event.getMessage();
		// TODO: think about having a list of commands here which when used to teleport into X territory
		// would be denied; X is configurable too
		mapLastExecutedCommand.put( playerInGame, cmd );
		// playerInGame.sendMessage( "oh hi1 " + Utilities.hasPermissionOrIsOp( playerInGame, permissionForHomeToEnemy ) + " "
		// + playerInGame.isOp() + " " + playerInGame.hasPermission( permissionForHomeToEnemy ) );
		
		if ( ( Config._teleports.shouldPreventHomeTelepors() ) && ( EssentialsIntegration.isHooked() )
		 && ( !playerInGame.isOp()))
		{
			// disallowed and no permission to bypass ? then check
			if ( cmd.toLowerCase().startsWith( homeCMD ) ) {
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
				} else {
					if ( ar.length == 2 ) {
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
						playerInGame.sendMessage( ChatColor.RED + FactionsPlus.FP_TAG_IN_LOGS
							+ "Internal error occurred calling Essentials, command ignored. Check console." );
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
					playerInGame.sendMessage( ChatColor.RED + "You have no homes/bed set. Command ignored." );
					event.setCancelled( true );
					return;
				}
				
				Location potentiallyModifiedTarget = null;
				try {
					potentiallyModifiedTarget = Util.getSafeDestination( targetLocation );
				} catch ( Exception e ) {
					e.printStackTrace();
					playerInGame.sendMessage( ChatColor.RED + FactionsPlus.FP_TAG_IN_LOGS
						+ "Internal error occurred calling Essentials, command ignored. Check console." );
					event.setCancelled( true );
					assert null == potentiallyModifiedTarget;
					return;
				}
				
				// assert null != potentiallyModifiedTarget;
				
				boolean allowed = false;
				count = 2;
				FactionsAny.Relation rel = null;
				Location locToCheck = targetLocation;
				while ( count > 0 ) {
					rel = getRelation( playerInGame, locToCheck );
//					System.out.println("check: "+playerInGame+" "+rel+" current="+allowed);
					switch ( rel ) {
					case ALLY:
						if ( !Config._teleports._into._allyTerritory._deny.viaHome._ ) {
							allowed = true;
						}else {
							allowed=false;//yes needed
						}
						break;
					case ENEMY:
						if ( !Config._teleports._into._enemyTerritory._deny.viaHome._ ) {
//								|| actually no permissions yet
//								(playerInGame.hasPermission( permissionForHomeToEnemy )) ) {
							allowed = true;
						}else {
							allowed=false;//yes needed
						}
						break;
					case NEUTRAL:
						if ( !Config._teleports._into._neutralTerritory._deny.viaHome._ ) {
							allowed = true;
						}else {
							allowed=false;//yes needed
						}
						break;
					case MEMBER:
						allowed=true;//auto allow /home-ing into own faction land
						break;
					default:
						playerInGame.sendMessage( ChatColor.RED + "Internal error, thus command denied" );
						event.setCancelled( true );
						throw new RuntimeException( "will never happen: " + rel );
					}
					if ( !allowed ) {
						break;
					}// else check the getSafeLocation one too
					count--;
					locToCheck = potentiallyModifiedTarget;
				}// while
//				System.out.println("final: "+playerInGame+" "+rel+" current="+allowed);
				if ( !allowed ) {
					playerInGame.sendMessage( ChatColor.RED + "You are not allowed to teleport to your /home which is now in "
						+ rel + " territory" );
					event.setCancelled( true );
					return;
//				}else {
//					System.out.println("allowed "+rel);
				}
			}
		}
	}
	
	
	
	@EventHandler(
			priority = EventPriority.MONITOR )
	public void onPlayerLogout( PlayerQuitEvent event ) {
		if ( !isHomeTracking() ) {
			return;
		}
		// this hook will trigger whenever a player quits/disconnects
		// this will prevent the map from getting too big by no longer keeping track of dc-ed players' last executed cmd
		mapLastExecutedCommand.remove( event.getPlayer() );
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
//		event.getPlayer().sendMessage(player.getName()+" "+FPlayers.i.get( player ).getFaction()+" "+getRelation( player, FPlayers.i.get( player ).getFaction()));
		
		// FIXME: problem is if the player can execute another command before the teleport is issued such as if warmup delays
		// are enabled for teleports, it will completely bypass this, because /home won't be the last seen command
		// find another way to fix this: maybe deny all teleports(to enemy land) unless the last command is in the
		// whitelist of allowed ones
		// this will be fixed soon
		
		TeleportCause cause = event.getCause();
		switch ( cause ) {
		case COMMAND:
			if ( isHomeTracking() ) {
				// possibly could be the /home command
				// now we check if the last command the player executed was /home
				String lastExecutedCommandByPlayer = mapLastExecutedCommand.get( player );
				assert null != lastExecutedCommandByPlayer;
				// this actually shouldn't be null here if tp cause was COMMAND if it ever is, then we need to investigate
				// ( null != lastExecutedCommandByPlayer ) && (
				// checkIfHomeTeleportWouldLandInEnemyTerritory(event, lastExecutedCommandByPlayer);
				
				if ( ( Config._teleports.shouldReportCommands() ) && ( !event.isCancelled() ) ) {
					// yeah report even if player had bypass permission but only if it will be a successful teleport
					Location targetLocation = event.getTo();
					Faction fac = getFactionAt( targetLocation );
					FactionsAny.Relation rel = getRelation( player, fac );
					boolean report = true;
					switch ( rel ) {
					case ALLY:
						if ( !Config._teleports._into._allyTerritory._report.viaCommand._ ) {
							report = false;
						}
						break;
					case ENEMY:
						if ( !Config._teleports._into._enemyTerritory._report.viaCommand._ ) {
							report = false;
						}
						break;
					case NEUTRAL:
						if ( !Config._teleports._into._neutralTerritory._report.viaCommand._ ) {
							report = false;
						}
						break;
					case MEMBER:
						report=false;
						break;
					default:
						player.sendMessage( ChatColor.RED + "Internal error" );
						// event.setCancelled( true );
						throw new RuntimeException( "will never happen: " + rel );
					}
					if ( report ) {
						FactionsPlusPlugin.info( constOneColor + "Player '" + ChatColor.DARK_AQUA + player.getName()
							+ constOneColor + "'"+(player.isOp()?"(op)":"")+" teleported into " + rel + " land faction '" + ChatColor.DARK_AQUA
							+ fac.getTag() + constOneColor + "' using command: '" + ChatColor.AQUA
							+ lastExecutedCommandByPlayer + constOneColor + "'." );
					}
				}
			}// homeTracking
			
			break;// cause COMMAND
			
		case ENDER_PEARL:
			if ( isEnderPearling() ) {
				Location targetLocation = event.getTo();
				Faction fac = getFactionAt( targetLocation );
				FactionsAny.Relation rel = getRelation( player, fac );
				boolean allowed = false;
				boolean report=true;
				switch ( rel ) {
				case ALLY:
					if ( !Config._teleports._into._allyTerritory._deny.viaPearls._ ) {
						allowed = true;
					}
					if (!Config._teleports._into._allyTerritory._report.viaPearls._) {
						report=false;
					}
					break;
				case ENEMY:
					if ( !Config._teleports._into._enemyTerritory._deny.viaPearls._ ) {
						allowed = true;
					}
					if (!Config._teleports._into._enemyTerritory._report.viaPearls._) {
						report=false;
					}
					break;
				case NEUTRAL:
					if ( !Config._teleports._into._neutralTerritory._deny.viaPearls._ ) {
						allowed = true;
					}
					if (!Config._teleports._into._neutralTerritory._report.viaPearls._) {
						report=false;
					}
					break;
				case MEMBER:
					allowed=true;//you may ender into your own territory
					report=false;//and don't report this obviously
					break;
				default:
					denyTeleport( event );
					player.sendMessage( ChatColor.RED + "Internal error, thus teleporting denied" );
					throw new RuntimeException( "will never happen: " + rel );
				}
				
				if ( !allowed ) {
					player.sendMessage( ChatColor.RED + "You are not allowed to ender pearl teleport inside " + rel
						+ " territory" );
					denyTeleport( event );
					break;
				}
				
				if ( ( report )&&(!event.isCancelled()) ) {
					FactionsPlusPlugin.info( constOneColor + "Player '" + ChatColor.DARK_AQUA + player.getName()
						+ constOneColor + "'"+(player.isOp()?"(op)":"")+" teleported into " + rel + " land faction '" + ChatColor.DARK_AQUA
						+ fac.getTag() + constOneColor + "' using " + ChatColor.AQUA
						+ "ender pearls" );
				}
			}
			break;
		default:
			// unhandled cause ? do nothing
		}
	}
	
	
	// private boolean isHomeTeleportLandingInEnemyTerritory( PlayerEvent event, Location targetLocation, String commandUsed ) {
	// Player player = event.getPlayer();
	// if ( ( Config._teleports.disallowTeleportingToEnemyLandViaHomeCommand._ )
	// && ( !Utilities.hasPermissionOrIsOp( player, permissionForHomeToEnemy ) ) )
	// {
	// // disallowed and no permission to bypass ? then check
	// if ( commandUsed.startsWith( "/home" ) ) {
	// // TODO: think about having a list of commands here which when used to teleport into X territory
	// // would be denied; X is configurable too
	//
	// // it is home, then let us check if his home is in enemy territory
	// if ( isEnemyLandAt( player, targetLocation ) ) {
	// player.sendMessage( ChatColor.RED
	// + "You are not allowed to teleport to your /home which is now in enemy territory" );
	// return true;
	// denyTeleport( event );
	// }
	// }
	// }
	// }
	
	
	private final void denyTeleport( PlayerTeleportEvent ptEvent ) {
		ptEvent.setCancelled( true );
		// not just cancel it, make sure that the canceling isn't ignored
		// worst case they'll teleport in the same spot where the command was issued from
		Location from = ptEvent.getFrom();
		ptEvent.setTo( from );
	}
	
	
	// private final boolean isEnemyLandAt( Player player, Location targetLocation ) {
	// Faction factionAtTarget = getFactionAt( targetLocation );// Board.getFactionAt( new FLocation( targetLocation ) );
	// return areEnemies( player, factionAtTarget );
	// }
	//
	//
	// private boolean areEnemies( Player player, Faction faction ) {
	// FPlayer fp = FPlayers.i.get( player );// should be able to get offline players too, js
	// if ( FactionsAny.Relation.ENEMY == Bridge.factions.getRelationBetween( faction, fp ) ) {
	// return true;
	// } else {
	// return false;
	// }
	// }
	
	private final FactionsAny.Relation getRelation( Player player, Location targetLocation ) {
		assert null != player;
		assert null != targetLocation;
		Faction factionAtTarget = getFactionAt( targetLocation );
		assert null != factionAtTarget;
		return getRelation( player, factionAtTarget );
	}
	
	
	private final FactionsAny.Relation getRelation( Player player, Faction fac ) {
		assert null != player;
		assert null != fac;
		FPlayer fp = FPlayers.i.get( player );// it will never be null
		assert null != fp;
		return Bridge.factions.getRelationBetween( fac, fp );
	}
	
	
	private Faction getFactionAt( Location targetLocation ) {
		return Board.getFactionAt( new FLocation( targetLocation ) );
	}
}
