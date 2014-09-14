package markehme.factionsplus;

import java.util.logging.Level;

import markehme.factionsplus.MCore.MConf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;


public abstract class FactionsPlusPlugin extends JavaPlugin {
	
	private boolean	didNotOverrideOnLoad = false;
	
	/**
	 * Disallow using onLoad as it causes issues, this checks if
	 * it's been override'd. 
	 */
	@Override
	public void onLoad() {
		didNotOverrideOnLoad = true;
	}
	
	/**
	 * Plugin will not work if onLoad is called 
	 */
	@Override
	public void onEnable() {
		if(!didNotOverrideOnLoad) {
			throw bailOut("You should not override & use onLoad() because it may cause problems, please just use onEnable()"
				+ " thus we won't allow bukkit to enable us at this time. This error is also caused by PlugMan not calling our " +
				"onLoad() method after having unloaded the Plugin class. Maybe try using /f reloadfp instead.");
		}
	}
	
	/**
	 * allowed to be used as: throw bailOut(..); so that eclipse won't
	 * complain about NPEs because it doesn't see that it can "throw"<br>
	 * the "throw " in front of the call, is also used to make sure the reader(and eclipse) knows that the call will
	 * unconditionally throw, as to
	 * not expect the code to continue<br>
	 * 
	 * @param msg
	 * @return is a dummy return so that it can be used like: throw bailOut(...);
	 */
	public static RuntimeException bailOut( String msg ) {
		throw bailOut( null, msg );
	}
	
	
	public static RuntimeException bailOut( Throwable cause, String msg ) {
		severe( cause, msg );
		throw new RuntimeException( msg, cause );// disableSelf( FactionsPlus.instance, true );
	}
	
	
	public static void warn( String logInfoMsg ) {
		tellConsole( ChatColor.GOLD + "[FactionsPlus] " + ChatColor.DARK_RED + "[WARNING]:" + ChatColor.RESET
			+ logInfoMsg );// they are logged with [INFO] level
	
	}
	
	
	public static void info( String logInfoMsg ) {
		tellConsole( ChatColor.GOLD + "[FactionsPlus] " + ChatColor.RESET + logInfoMsg );// they are logged with
																									// [INFO] level
	}
	
	
	public static void severe( String logInfoMsg ) {
		FactionsPlus.log.severe( "[FactionsPlus] " + logInfoMsg );// allowed so that [SEVERE] appears
		tellConsole( ChatColor.RED + "[FactionsPlus] " + ChatColor.DARK_PURPLE + logInfoMsg );
	}
	
	
	public static void severe( Throwable cause ) {
		severe( cause, null );
	}
	
	
	/**
	 * @param cause
	 *            if null => ignored
	 * @param logInfoMsg
	 */
	public static void severe( Throwable cause, String msg ) {
		String sevLog;
		
		if ( null == msg ) {
			sevLog = cause.getMessage() == null ? cause.getClass().getSimpleName() : cause.getMessage();
		} else {
			sevLog = msg;
		}
		String finmsg = "[FactionsPlus] " + sevLog;
		if ( null == cause ) {
			FactionsPlus.log.log( Level.SEVERE, finmsg); // allowed so that [SEVERE] appears
		} else {
			FactionsPlus.log.log( Level.SEVERE, finmsg, cause ); // allowed so that [SEVERE] appears
		}
		tellConsole( ChatColor.RED + "[FactionsPlus] " + ChatColor.DARK_PURPLE + sevLog );
	}
	
	/**
	 * Shows a debug message to the console, if it's enable in the configuration 
	 * @param message
	 */
	public static void debug(String message) {
		// Only show debug messages if we've enabled it
		if(MConf.get().debug) {
			tellConsole(ChatColor.GOLD + "[FactionsPlus] "+ChatColor.WHITE+"[DEBUG] " + ChatColor.RESET + message);
		}
	}
	
	/**
	 * allows the use of ChatColor in messages but they will be prefixed by [INFO]
	 * 
	 * @param msg
	 */
	public static void tellConsole(String msg) {
		Bukkit.getConsoleSender().sendMessage(msg); // this will log with [INFO] level
	}
	
	/**
	 * calling this will not stop execution at the point where the call is made, but will mark the plugin as disabled<br>
	 * ie. shown in red when /plugins is issued
	 * 
	 */
	public void disableSelf() {
		setEnabled(false); // it will call onDisable() if it was enabled
	}
	
	
}
