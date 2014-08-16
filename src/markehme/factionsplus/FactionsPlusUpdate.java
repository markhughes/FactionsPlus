package markehme.factionsplus;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import markehme.factionsplus.MCore.MConf;
import markehme.factionsplus.extras.Updater;
import markehme.factionsplus.extras.Updater.UpdateResult;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;



@SuppressWarnings( "unused" )
public class FactionsPlusUpdate implements Runnable {
	
	private static final long	DELAY	= 5*20; //5 sec delay on startup before checking for updates

	private static final long	PERIOD	= 4*60*60*20;//20 ticks per sec, check every 4 hours

	private static FactionsPlusUpdate	once	= null;
	private static volatile int			taskId=Integer.MIN_VALUE;		// if modified in two threads
	
	public static Boolean updateAvailable = false;
	
	static {
		if ( PERIOD < 60 * 20){
//			FactionsPlus.instance.disableSelf();
			throw FactionsPlus.bailOut("Please set the repeating delay to at least every 60 seconds though it should be much more");
			//yeah this will still not stop it
		}
	}
	
	static public void checkUpdates( FactionsPlus instance ) {
		synchronized ( FactionsPlusUpdate.class ) {
			
			updateAvailable = false;
			
			if ( null == once ) {
				once = new FactionsPlusUpdate();
			}
			taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(instance, once, DELAY, PERIOD);
			
			if ( taskId < 0 ) {// not possible
				FactionsPlus.warn( "Failed to start the check-for-updates thread!" );
			}
		}
	}
	
	public static boolean isRunning() {
		synchronized(FactionsPlusUpdate.class) {
			return(taskId >= 0) && (once != null);
		}
	}
	
	/**
	 * If the Updater is running when this method is called
	 * it will attempt to stop it.
	 */
	public static void ensureNotRunning() {
		synchronized(FactionsPlusUpdate.class) {
			if (taskId >= 0) {
				updateAvailable = false;
				
				BukkitScheduler sched = Bukkit.getServer().getScheduler();
				
				sched.cancelTask(taskId);
				
				taskId = Integer.MIN_VALUE;
				
				if(sched.isCurrentlyRunning(taskId))  {
					FactionsPlus.warn("Stopped the check-for-updates thread.");
				}
			}
			
			once = null;
		}
	}
	
	
	public static void enableOrDisableCheckingForUpdates() {
		synchronized ( FactionsPlusUpdate.class ) {
			
			if(!MConf.get().updateCheck) {
				// Updates are not enabled, so ensure we're not running.
				FactionsPlusUpdate.ensureNotRunning();
			} else {
				if(!isRunning()) {
					// Is not running yet, so get it going.
					FactionsPlus.info("Checking for updates every "+(PERIOD/20/60/60)+" hours.");
					FactionsPlusUpdate.checkUpdates( FactionsPlus.instance );
				} else {
					// Is running - just give some information.
					FactionsPlus.info("Still checking for updates every "+(PERIOD/20/60/60)+" hours.");
				}
			}
		}
	}
	
	@Override
	public void run() {
		synchronized ( FactionsPlusUpdate.class ) {
			String content = null;
			URLConnection connection = null;
						
			Updater updater = new Updater(FactionsPlus.instance, 38249, FactionsPlus.thefile, Updater.UpdateType.NO_DOWNLOAD, false);
			
			if(updater.getResult().equals(UpdateResult.UPDATE_AVAILABLE)) {
				FactionsPlus.log.warning( "! -=========================================- !" );
				FactionsPlus.log.warning( "FactionsPlus has an update, you can " );
				FactionsPlus.log.warning( "upgrade to " + updater.getLatestName() + " via" );
				FactionsPlus.log.warning( "http://dev.bukkit.org/bukkit-plugins/factionsplus/" );
				FactionsPlus.log.warning( "! -=========================================- !" );
				
				updateAvailable = true;
				
				for (Player player : Bukkit.getServer().getOnlinePlayers()) {
					if (player.isOp()) {
						player.sendMessage(ChatColor.RED + "FactionsPlus version " + ChatColor.GOLD + updater.getLatestName() + ChatColor.RED + " is out! You should upgrade to avoid bugs, and for new features! " );
					}
				}
			}
		}
	}
}