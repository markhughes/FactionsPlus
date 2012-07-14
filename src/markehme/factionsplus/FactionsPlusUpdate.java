package markehme.factionsplus;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

import org.bukkit.*;
import org.bukkit.scheduler.*;

import markehme.factionsplus.config.*;



public class FactionsPlusUpdate implements Runnable {
	
	private static FactionsPlusUpdate	once	= null;
	// private static Thread updateThread = null;
	// TODO: fix when no-internet access allowed and does 'reload' two times, the second waits 10 sec for the first run of
	// thread to timeout on reading due to unknown host (after 10 sec)
	
	private static volatile int			taskId;		// if modified in two threads
														
														
	// private static ReentrantLock rl = new ReentrantLock();
	// private static final int MAXWAITTIME = 5;
	
	
	static public void checkUpdates( FactionsPlus instance ) {
		synchronized ( FactionsPlusUpdate.class ) {
			if ( null == once ) {
				once = new FactionsPlusUpdate();
				// assert null == updateThread;
				// updateThread = new Thread( once );
				// updateThread.setDaemon( false );
			}
			// else {
			// updateThread.stop();
			// }
			// updateThread.start();
			// }
			// if (taskId > 0) {//will never detect this, due to load/unload aka reinit of all fields
			// FactionsPlus.warn("The check-for-updates thread was already running from the last time the plugin was started!");
			// }
			
			// try {
			// rl.tryLock( MAXWAITTIME, TimeUnit.SECONDS );
			// } catch ( InterruptedException e ) {
			// e.printStackTrace();
			// }
			// if ( rl.isLocked() ) {
			// try {
			// synchronized ( FactionsPlusUpdate.class ) {
			taskId = Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask( instance, once );
			if ( taskId < 0 ) {// not possible
				FactionsPlus.warn( "Failed to start the check-for-updates thread!" );
			}
		}
		// } finally {
		// rl.unlock();
		// }
		// }
	}
	
	
	public static void ensureNotRunning() {
		// try {
		// rl.tryLock( MAXWAITTIME, TimeUnit.SECONDS );
		// } catch ( InterruptedException e ) {
		// e.printStackTrace();
		// }
		// if ( rl.isLocked() ) {
		// try {
		synchronized ( FactionsPlusUpdate.class ) {
			if ( taskId > 0 ) {
				BukkitScheduler sched = Bukkit.getServer().getScheduler();
				if ( sched.isCurrentlyRunning( taskId ) ) {// not possible due to lock
					FactionsPlus.warn( "The check-for-updates thread was still running" );
				}
				sched.cancelTask( taskId );// yeah it's doing the same thing as I did
				// thread still runs even though plugin restarted, and now runs twice
				taskId = Integer.MIN_VALUE;
				if ( sched.isCurrentlyRunning( taskId ) ) {// not reached!
					FactionsPlus.warn( "Stopped the check-for-updates thread" );
				}
			}
		}
		// } finally {
		// rl.unlock();
		// }
		// }
		//
		// if ( ( null != updateThread ) && ( updateThread.isAlive() ) ) {
		// updateThread.interrupt();
		// if ( !updateThread.isInterrupted() ) {
		// FactionsPlus.warn( "Killing update thread!" );
		// updateThread.stop();
		// }
		// synchronized ( FactionsPlusUpdate.class ) {
		// assert !updateThread.isAlive();
		// }
		// }
	}
	
	
	@Override
	public void run() {
		synchronized ( FactionsPlusUpdate.class ) {
			// try {
			// rl.tryLock( MAXWAITTIME, TimeUnit.SECONDS );
			// } catch ( InterruptedException e ) {
			// e.printStackTrace();
			// }
			// if ( rl.isLocked() ) {
			// try {
			String content = null;
			URLConnection connection = null;
			String v = FactionsPlus.version;
			
			if ( Config._extras.disableUpdateCheck._ ) {
				return;
			}
			
			FactionsPlusPlugin.info( "Checking for updates ... " );
			
			Scanner scanner = null;
			try {
				//TODO: find a way to kill blocking scanner when plugin needs to disable/shutdown, currently it delays shutdown
				connection = new URL( "http://www.markeh.me/factionsplus.php?v=" + v ).openConnection();
				scanner = new Scanner( connection.getInputStream() );
				scanner.useDelimiter( "\\Z" );
				content = scanner.next();
			}catch (java.net.UnknownHostException uhe) {
				FactionsPlusPlugin.info( "Failed to check for updates. Cannot resolve host "+uhe.getMessage() );
				return;
			}catch ( Exception ex ) {
				ex.printStackTrace();
				FactionsPlusPlugin.info( "Failed to check for updates." );
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
						FactionsPlus.log.warning( "FactionsPlus has an update, you" );
						FactionsPlus.log.warning( "can upgrade to version " + content.trim() + " via" );
						FactionsPlus.log.warning( "http://dev.bukkit.org/server-mods/factionsplus/" );
						FactionsPlus.log.warning( "! -=====================================- !" );
					} else {
						FactionsPlusPlugin.info( "Up to date!" );
					}
				} else {
					// Version lengths different, unable to advance compare
					FactionsPlus.log.warning( "! -=====================================- !" );
					FactionsPlus.log.warning( "FactionsPlus has an update, you" );
					FactionsPlus.log.warning( "can upgrade to version " + content.trim() + " via" );
					FactionsPlus.log.warning( "http://dev.bukkit.org/server-mods/factionsplus/" );
					FactionsPlus.log.warning( "! -=====================================- !" );
				}
			} else {
				FactionsPlusPlugin.info( "Up to date!" );
			}
		}
		// } finally {
		// rl.unlock();
		// }
		// }
	}
}
