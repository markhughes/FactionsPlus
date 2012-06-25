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
	private static Listener	preventTeleports	= new TeleportsListener();
	
	
	public static void init( Plugin plugin ) {
		if ( !plugin.isEnabled() ) {
			return;
		}
		if (null == FactionsPlus.config) {
			throw FactionsPlus.bailOut( "bad call order while java coding, call this after config is loaded" );
		}
		
		if ( !FactionsPlus.config.getBoolean( configForDisallowHomeToEnemy ) ) {
			// since this is the only thing this class does at this time, we can do this 'if'
			// which means, if this config isn't <"true" aka disallow>, then we don't even do the hooks
			return;
		}
		
		Bukkit.getServer().getPluginManager().registerEvents( preventTeleports, plugin );
	}
	
	// keeps track of the last executed command for each of the online players
	private Map<Player, String>	mapLastExecutedCommand	= new HashMap<Player, String>();
	
	
	@EventHandler( priority = EventPriority.MONITOR )
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
	
	
	
	/**
	 * this will prevent teleports by "/home" command (only) if they land into enemy territory,
	 * unless you have a specific permission node
	 * 
	 * @param event
	 */
	// LOWEST=executed prior to others with like NORMAL or HIGHEST priorities
	// however if two plugins use the same, one of them will be executed first
	// but in this case we don't need a specific priority, tested with HIGHEST and it still worked with setCancelled()
	@EventHandler( priority = EventPriority.NORMAL )
	public void onTeleport( PlayerTeleportEvent event ) {
		Player player = event.getPlayer();
		if ( event.isCancelled() ) {
			// already cancelled, we don't care then, though some tricky plugin could cancel it in LOWEST and reenable it
			// in HIGHEST, thus totally bypassing us here; this could be avoided if we were to use MONITOR prio but not entirely
			// avoided
			return;
		}
		
		// TODO: investigate how Factions is preventing /sethome in enemy land cause it's better than Event seemingly
		TeleportCause cause = event.getCause();
		if ( cause == PlayerTeleportEvent.TeleportCause.COMMAND ) {
			// possibly could be the /home command
			// now we check if the last command the player executed was /home
			String lastExecutedCommandByPlayer = mapLastExecutedCommand.get( player );
			if ( ( null != lastExecutedCommandByPlayer ) && ( lastExecutedCommandByPlayer.startsWith( "/home" ) ) ) {
				if ( player.hasPermission( permissionForHomeToEnemy ) ) {
					return;
				}
				// TODO: have a list of commands here which when used to teleport into X territory would be denied
				// X is configurable too
				
				// oh it is home, then let us check if his home is in enemy territory
				Location targetLocation = event.getTo();
				Faction factionAtTarget = Board.getFactionAt( new FLocation( targetLocation ) );
				FPlayer fp = FPlayers.i.get( player );
				if ( FactionsAny.Relation.ENEMY == Bridge.factions.getRelationBetween( factionAtTarget, fp ) ) {
					player.sendMessage( ChatColor.RED
						+ "You are not allowed to teleport to your /home which is now in enemy territory" );
					event.setCancelled( true );
					// not just cancel it, make sure that the cancelling isn't ignored
					// worst case they'll teleport in the same spot where the command was issued from
					Location from = event.getFrom();
					event.setTo( from );
				}// otherwise just allow it
			}
		}
	}
}
