package markehme.factionsplus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

import markehme.factionsplus.MCore.MConf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class FactionsPlusUpdate implements Runnable {
	
	/**
	 * The delay before the update starts
	 */
	public static final long DELAY			= 5*20;
	
	/**
	 * How often we should check, if this case - 4 hours
	 */
	public static final long PERIOD		= 4*60*60*20; 
	
	/**
	 * FactionsPlusUpdate Regularity
	 */
	private static FactionsPlusUpdate once	= null;
	
	/**
	 * TaskID for the updater
	 */
	private static volatile int	taskId		= Integer.MIN_VALUE;
	
	/**
	 * Is there an update available 
	 */
	private static Boolean updateAvailable = false;
	
	/**
	 * Returns true if an update is available 
	 * @return
	 */
	public static Boolean isUpdateAvailable() {
		synchronized(FactionsPlusUpdate.class) {
			return updateAvailable;
		}
	}
	
	/**
	 * Returns true if the updater is running 
	 * @return
	 */
	public static boolean isRunning() {
		synchronized(FactionsPlusUpdate.class) {
			return(taskId >= 0) && (once != null);
		}
	}
	
	static public void checkUpdates( FactionsPlus instance ) {
		synchronized (FactionsPlusUpdate.class) {
			
			// Set the default valut 
			updateAvailable = false;
			
			// Ensure our FPUpdate instance is in place
			if(once == null) once = new FactionsPlusUpdate();
			
			// Start the scheduler, store the taskID
			taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(instance, once, DELAY, PERIOD);
			
			// Check it was a valid task
			if(taskId < 0) {
				FactionsPlus.warn( "Failed to start the check-for-updates thread!" );
			}
		}
	}
	
	
	
	/**
	 * If the Updater is running when this method is called
	 * it will attempt to stop it.
	 */
	public static void ensureNotRunning() {
		synchronized(FactionsPlusUpdate.class) {
			if(taskId >= 0) {
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
	
	
	/**
	 * This will enable or disable the update checker
	 */
	public static void enableOrDisableCheckingForUpdates() {
		synchronized(FactionsPlusUpdate.class) {
			
			if(!MConf.get().updateCheck) {
				// Updates are not enabled, so ensure we're not running.
				FactionsPlusUpdate.ensureNotRunning();
			} else {
				// Check if we're running or not
				if(!isRunning()) {
					// Is not running yet, so get it going.
					FactionsPlus.info("Checking for updates every "+(PERIOD/20/60/60)+" hours.");
					FactionsPlusUpdate.checkUpdates(FactionsPlus.instance);
				} else {
					// Is running - just give some information.
					FactionsPlus.info("Still checking for updates every "+(PERIOD/20/60/60)+" hours.");
				}
			}
		}
	}
	
	/**
	 * This is the actual update checker. Based off the Updater 2.3 by Gravity. Had to modify
	 * it to make it thread-safe. 
	 */
	@Override
	public void run() {
		synchronized (FactionsPlusUpdate.class) {
			try {
				URL url = new URL("https://api.curseforge.com/servermods/files?projectIds=38249");
				
				final URLConnection conn = url.openConnection();
				conn.setConnectTimeout(5000);
	
				if(MConf.get().CurseAPIKey != null && MConf.get().CurseAPIKey.trim() != "") {
					conn.addRequestProperty("X-API-Key", MConf.get().CurseAPIKey);
				}
				conn.addRequestProperty("User-Agent", "FactionsPlusUpdate Class");
	
				conn.setDoOutput(true);
	
				final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				final String response = reader.readLine();
	
				final JSONArray array = (JSONArray) JSONValue.parse(response);
	
				if(array.isEmpty()) {
					FactionsPlus.log.warning("The updater could not find any files for the project!");
					
					if(MConf.get().CurseAPIKey == null && MConf.get().CurseAPIKey.trim() == "") {
					FactionsPlus.log.warning("Please configure CurseAPIKey in the FactionsPlus MConf File");
					 } else {
					 	 FactionsPlus.log.warning("CurseAPI may be down?");
					 }
					 
					return;
				}
	
				JSONObject latestUpdate = (JSONObject) array.get(array.size() - 1);
				String versionName = (String) latestUpdate.get("name");
				// Might use these later: 
				//String versionLink = (String) latestUpdate.get("downloadUrl");
				//String versionType = (String) latestUpdate.get("releaseType");
				//String versionGameVersion = (String) latestUpdate.get("gameVersion");
	
				String localVersion = FactionsPlus.instance.getDescription().getVersion();
                String remoteVersion = versionName.split("^v|[\\s_-]v")[1].split(" ")[0];

                if(localVersion.equalsIgnoreCase(remoteVersion)) {
    				notifyUpdates(versionName);
                }
                
				return;
			} catch (final IOException e) {
				if (e.getMessage().contains("HTTP response code: 403")) {
					FactionsPlus.log.warning("dev.bukkit.org rejected the API key provided in the FactionsPlus MConf File");
					FactionsPlus.log.warning("Please check the CurseAPIKey value.");
					FactionsPlus.log.warning("Provided value: " + MConf.get().CurseAPIKey);
				} else {
					FactionsPlus.log.warning("Could not connect to dev.bukkit.org to check for updates!");
				}
				FactionsPlus.instance.getLogger().log(Level.SEVERE, null, e);	
			}
		}
	}
	
	/**
	 * This will notify console and op's about a new version being available 
	 * @param version
	 */
	public void notifyUpdates(String version) {
		synchronized (FactionsPlusUpdate.class) {
			// Notify console of updates 
			FactionsPlus.log.warning( "! -=========================================- !" );
			FactionsPlus.log.warning( "FactionsPlus has an update, you can " );
			FactionsPlus.log.warning( "upgrade to " + version + " via" );
			FactionsPlus.log.warning( "http://dev.bukkit.org/bukkit-plugins/factionsplus/" );
			FactionsPlus.log.warning( "! -=========================================- !" );
			
			// Inform op's 		
			for(Player player : Bukkit.getServer().getOnlinePlayers()) {
				if(player.isOp()) {
					player.sendMessage(ChatColor.RED + "There is an update for FactionsPlus! " + ChatColor.GOLD + version + ChatColor.RED + " has been submitted. You should upgrade to avoid bugs, and for new features!");
				}
			}
		}
	}
}