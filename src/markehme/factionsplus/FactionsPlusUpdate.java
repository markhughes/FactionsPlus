package markehme.factionsplus;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import markehme.factionsplus.config.Config;
import markehme.factionsplus.references.FP;
import markehme.factionsplus.references.FPP;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;



@SuppressWarnings( "unused" )
public class FactionsPlusUpdate implements Runnable {
	
	private static final long	DELAY	= 5*20;//5 sec delay on startup before checking for updates

	private static final long	PERIOD	= 4*60*60*20;//20 ticks per sec, check every 4 hours

	private static FactionsPlusUpdate	once	= null;

	// TODO: fix when no-internet access allowed and does 'reload' two times, the second waits 10 sec for the first run of
	// thread to timeout on reading due to unknown host (after 10 sec)
	
	private static volatile int			taskId=Integer.MIN_VALUE;		// if modified in two threads
		
	static {
		if ( PERIOD < 60 * 20){
//			FactionsPlus.instance.disableSelf();
			throw FactionsPlus.bailOut("Please set the repeating delay to at least every 60 seconds though it should be much more");
			//yeah this will still not stop it
		}
	}
	
	static public void checkUpdates( FactionsPlus instance ) {
		synchronized ( FactionsPlusUpdate.class ) {
			FactionsPlus.update_avab = false;
			
			if ( null == once ) {
				once = new FactionsPlusUpdate();
			}
			taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(instance, once, DELAY, PERIOD);
			//taskId = Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask( instance, once, DELAY, PERIOD );
			if ( taskId < 0 ) {// not possible
				FactionsPlus.warn( "Failed to start the check-for-updates thread!" );
			}
		}
	}
	
	public static boolean isRunning() {
		synchronized ( FactionsPlusUpdate.class ) {
			return (taskId >= 0) && (once != null);
		}
	}
	
	public static void ensureNotRunning() {
		
		synchronized ( FactionsPlusUpdate.class ) {
			
			if ( taskId >= 0 ) {
				BukkitScheduler sched = Bukkit.getServer().getScheduler();
				
				//this is nolonger relevant, since we're checking for updates every x hours, it will always be detected 
				//as still running here (except when never started)
//				if ( sched.isCurrentlyRunning( taskId ) ) {// not possible due to lock
//					FactionsPlus.warn( "The check-for-updates thread was still running" );
//				}
				sched.cancelTask( taskId );// yeah it's doing the same thing as I did
				// thread still runs even though plugin restarted, and now runs twice
				taskId = Integer.MIN_VALUE;
				if ( sched.isCurrentlyRunning( taskId ) ) {// not reached!
					FactionsPlus.warn( "Stopped the check-for-updates thread" );
				}
			}
			
			once=null;
		}
	}
	
	
	public static void enableOrDisableCheckingForUpdates() {
		synchronized ( FactionsPlusUpdate.class ) {
			if ( Config._extras.disableUpdateCheck._ ) {
				FactionsPlusUpdate.ensureNotRunning();
				FactionsPlus.info("Never checking for updates");
			} else {
				// enable
				if ( !isRunning() ) {
					FactionsPlus.info("Will now check for updates every "+(PERIOD/20/60/60)+" hours (and on startup)");
					FactionsPlusUpdate.checkUpdates( FactionsPlus.instance );
				}else{
					//still running
					FactionsPlus.info("Still checking for updates every "+(PERIOD/20/60/60)+" hours (and on startup)");
//					next check is in "
//						Bukkit.getServer().getScheduler().);
				}
			}
		}
	}
	
	@Override
	public void run() {
		synchronized ( FactionsPlusUpdate.class ) {
			String content = null;
			URLConnection connection = null;
			String v = FactionsPlus.version;
			
			FPP.info( "Checking for updates ... " );
			
			Scanner scanner = null;
			try {
				
				connection = new URL( "http://www.markeh.me/factionsplus.php?v=" + v ).openConnection();
								
				connection.setReadTimeout(15 * 1000); // Read time out after 15 seconds. 
				connection.setConnectTimeout(15 * 1000); // Connect time out after 15 seconds
				
				scanner = new Scanner( connection.getInputStream() );
				scanner.useDelimiter( "\\Z" );
				content = scanner.next();
			} catch (java.net.UnknownHostException uhe) {
				FPP.info( "Failed to check for updates. Cannot resolve host "+uhe.getMessage() );
				return;
				
			} catch (java.net.ConnectException ce) {
				FPP.info( "Failed to check for updates. "+ce.getMessage() );
				return;
			} catch( java.net.SocketTimeoutException ste ) {
				FPP.info( "Failed to check for updates, the connection timed out (15 seconds): "+ste.getMessage() );
				return;
			} catch ( Exception ex ) {
				ex.printStackTrace();
				FPP.info( "Failed to check for updates." );
				return;
			} finally {
				if ( null != scanner ) {
					scanner.close();
				}
			}
			
			// advanced checking
			if ( !content.trim().equalsIgnoreCase( v.trim() ) ) {
				int web, current;
				String tempWeb = content.trim().replace( ".", "" );
				String tempThis = v.trim().replace( ".", "" );
				
				web = Integer.parseInt( tempWeb );
				current = Integer.parseInt( tempThis );
				
				// Check if version lengths are the same
				if ( tempWeb.length() == tempThis.length() ) {
					if ( web > current ) {
						// Version lengths different, unable to advance compare
						FactionsPlus.log.warning( "! -=====================================- !" );
						FactionsPlus.log.warning( "FactionsPlus has an update, you can " );
						FactionsPlus.log.warning( "upgrade to version " + content.trim() + " via" );
						FactionsPlus.log.warning( "http://dev.bukkit.org/bukkit-plugins/factionsplus/" );
						FactionsPlus.log.warning( "! -=====================================- !" );
						
						FactionsPlus.update_avab = true;
						
						for (Player player : Bukkit.getServer().getOnlinePlayers()) {
							if (player.isOp()) {
								player.sendMessage(ChatColor.RED + "FactionsPlus version " + ChatColor.GOLD + content.trim() + ChatColor.RED + " is out! You should upgrade to avoid bugs, and deprecated code. (+ new features, come on!) " );
							}
						}
						
					} else {
						FPP.info( "Up to date!" );
					}
				} else {
					// Version lengths different, unable to advance compare
					FactionsPlus.log.warning( "! -=====================================- !" );
					FactionsPlus.log.warning( "FactionsPlus has an update, you can " );
					FactionsPlus.log.warning( "upgrade to version " + content.trim() + " via" );
					FactionsPlus.log.warning( "http://dev.bukkit.org/bukkit-plugins/factionsplus/" );
					FactionsPlus.log.warning( "! -=====================================- !" );
					
					FactionsPlus.update_avab = true;
					
					// Doesn't need seperate thread as it's already in one
					for ( Player player : Bukkit.getServer().getOnlinePlayers() ) {
						
						if ( player.isOp() ) {
							
							player.sendMessage( ChatColor.RED + "FactionsPlus version " + ChatColor.GOLD + content.trim() + ChatColor.RED + " is out! You should upgrade to avoid bugs, and deprecated code. (+ new features, come on!) " );
							
						}
						
					}
					
				}
			} else {
				
				FPP.info( "FactionsPlus is up to date." );
				
			}
		}
	}
}
