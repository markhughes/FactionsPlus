package markehme.factionsplus;


import markehme.factionsplus.references.FP;
import net.ess3.api.InvalidWorldException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;

import com.earth2me.essentials.IEssentials;
import com.earth2me.essentials.Teleport;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.utils.LocationUtil;
import com.massivecraft.factions.entity.UConf;

public abstract class EssentialsIntegration {
	private static final String	pluginName			= "Essentials";
	private static IEssentials ess					= null;
	private static boolean	isLoadedButNotEnabled	= false;
							
	
	public synchronized static void onDisable(){
		ess=null;
		isLoadedButNotEnabled=false;
	}
	
	/**
	 * with lazy init, due to the fact that Essentials being a soft-depend, my guess is that there is a possibility it can be 
	 * not enabled, ie. it may enable after our plugin enables<br>
	 * 
	 * @return the instance or null
	 */
	private synchronized static final IEssentials getEssentialsInstance() {
		
		Plugin essPlugin;
		
		if( ( null == ess ) 
				|| (!ess.isEnabled())
				//short circuit may not eval the next one
				|| (ess != (essPlugin = Bukkit.getPluginManager().getPlugin( pluginName ))) ) {
			
			// lazyly init or : maybe add depend (not soft) in plugin.yml
			essPlugin = Bukkit.getPluginManager().getPlugin( pluginName );
			
			if ( ( null != essPlugin ) && ( essPlugin.isEnabled() ) ) {
				ess = (IEssentials)essPlugin;
				isLoadedButNotEnabled=!essPlugin.isEnabled();
//				haveEssentials = ESS_HAVE.INITED_AND_HAVE;
//			} else {
//				haveEssentials = ESS_HAVE.INITED_AND_NOT_HAVE;
//			}else{
//				isLoadedButNotEnabled=false;
				FactionsPlus.info( "Updated the cached reference to Essentials' instance: `"+ess+"`." );
			}else {
				if (null != ess) {
					FactionsPlus.info( "Essentials plugin is nolonger on the system. FactionsPlus can't hook!");
				}
				ess=null;
				
			}
			
		}
		
		return( ess ); // can be null
	}
	
	
	public final static boolean isLoadedButNotEnabled() {
		
		return( !isHooked() && isLoadedButNotEnabled );
		
	}
	
	public synchronized final static boolean isHooked() {
		
		return( null != getEssentialsInstance() );
		
	}
	
	public final static Location getHomeForPlayer( Player player, String homeName ) throws Exception {
		
		checkInvariants();
		try {
			return( getEssentialsInstance().getUser( player ).getHome( homeName ) );
		} catch (InvalidWorldException e) {
			
			player.sendMessage(ChatColor.RED + "The home " + homeName + " was in the world "+e.getWorld()+", but that world is no longer existant..");
			return( null );
		}
		
	}
	
	
	public final static int getHomesCount( Player player ) {
		
		checkInvariants();
		
		return getEssentialsInstance().getUser( player ).getHomes().size();
		
	}
	

	/**
	 * @param player
	 * @return can be null
	 */
	public final static Location getLastLocation( Player player) {
		
		checkInvariants();
		
		return( getEssentialsInstance().getUser( player ).getLastLocation() );
		
	}
	
	
	private final static void checkInvariants() {
		
		if ( !isHooked() ) {
			
			throw new RuntimeException( "Internal coding error: using "+pluginName+" functions while it was not hooked" );
			
		}
	}

	public static Location getSafeDestination( Location targetLocation ) {
		if ( isHooked() ) {
			try { 
				
				return LocationUtil.getSafeDestination( targetLocation );
				
			} catch( Exception e) {
				
				FP.severe( "Can't get Safe Destination using Essentials." );
				
				return( targetLocation );
				
			}
		}else{
			//not running Essentials on server? return same location
			return targetLocation;
		}
	}
	
	@SuppressWarnings("cast")
	public static boolean handleTeleport(Player player, Location loc) {
		if( ! isHooked() ) {
			return false;
		}
		
		try {
			Teleport teleport = (Teleport) getEssentialsInstance().getUser(player).getTeleport();
			
			Trade trade = new Trade( (int) UConf.get(player).econCostHome, (net.ess3.api.IEssentials) getEssentialsInstance() );
			new Trade(0, null);
			
			teleport.teleport( loc, trade, TeleportCause.PLUGIN );
			
		} catch (Exception e) {
			player.sendMessage(ChatColor.RED.toString()+e.getMessage());
			
			return false;
		}
		return true;
	}
	
}
