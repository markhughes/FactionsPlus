package markehme.factionsplus;

import java.util.logging.Level;

import markehme.factionsplus.extras.BailingOutException;
import markehme.factionsplus.util.Q;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;



public abstract class FactionsPlusPlugin extends JavaPlugin {
	
	// private boolean allowPluginToEnable=true;
	private boolean	didNotOverrideOnLoad	= false;
	
	// public boolean isAllowPluginToEnable() {
	// return allowPluginToEnable;
	// }
	//
	// public void setDisAllowPluginToEnable() {
	// allowPluginToEnable = false;
	// }
	@Override
	public void onLoad() {
		/*
		 * don't use things like Bukkit.getConsoleSender() they will NPE here<br>
		 * XXX: best to avoid using onLoad()!! due to bukkit stuff not being inited
		 */
		
		// usually when you override onLoad on subclass, you don't call super(), we're counting on this to detect if coder tried
		// to do this and is unaware of the potential problems this may imply
		didNotOverrideOnLoad = true;
		// PlugMan actually never calls super.onLoad() which means we never get this variable to true, and thus plugman can
		// never enable us
//		System.out.println("---------- onLoad() ---------");//yep not even called by PlugMan
	}
	
	
	@Override
	public void onEnable() {
		if ( !didNotOverrideOnLoad ) {
			throw bailOut( "You should not override & use onLoad() because it may cause problems, please just use onEnable()"
				+ " thus we won't allow bukkit to enable us at this time. This error is also caused by PlugMan not calling our " +
				"onLoad() method after having unloaded the Plugin class. Maybe try using /f reloadfp instead." );
		}
	}
	
	
	@Override
	public FileConfiguration getConfig() {
		throw Q.ni();// just in case something accidentally calls this, for now
	}
	
	
	@Override
	public void saveConfig() {
		throw Q.ni();// just in case something accidentally calls this, for now
	}
	
	
	@Override
	public void reloadConfig() {
		throw Q.ni();// just in case something accidentally calls this, for now
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
		throw new BailingOutException( msg, cause );// disableSelf( FactionsPlus.instance, true );
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
	
	public static void debug( String debugMessage ) {
		tellConsole( ChatColor.GOLD + "[FactionsPlus] "+ChatColor.WHITE+"[DEBUG] " + ChatColor.RESET + debugMessage );// they are logged with
																									// [INFO] level
	}
	
	/**
	 * allows the use of ChatColor in messages but they will be prefixed by [INFO]
	 * 
	 * @param msg
	 */
	public static void tellConsole( String msg ) {
		// nvm; find another way to display colored msgs in console without having [INFO] prefix
		// there's no other way it's done via ColouredConsoleSender of craftbukkit
		// there are only two ways: colors+[INFO] prefix, or no colors + whichever prefix
		Bukkit.getConsoleSender().sendMessage( msg );// this will log with [INFO] level
	}
	
	
	// public static RuntimeException disableSelf( FactionsPlus fpInstance, boolean forceStop ) {
	// fpInstance.disableSelf();
	// if ( forceStop ) {
	// // FactionsPlus.log.log( Level.INFO, )
	// throw new RuntimeException( FactionsPlus.FP_TAG_IN_LOGS
	// + " execution stopped by disableSelf() which means read the above colored message" );
	// }
	// return null;
	// }
	
	
	/**
	 * calling this will not stop execution at the point where the call is made, but will mark the plugin as disabled<br>
	 * ie. shown in red when /plugins is issued
	 * 
	 */
	public void disableSelf() {
		setEnabled( false );// it will call onDisable() if it was enabled
		// Bukkit.getPluginManager().disablePlugin( this );
		// it won't deregister commands ie. /f fc will still work apparently, likely cause Factions plugin won't be disabled and
		// it is
		// it that handles those commands that we potentially already registered to it (assuming this was called after)
	}
	
	
}
