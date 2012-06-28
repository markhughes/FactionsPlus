package markehme.factionsplus;

import java.io.*;
import java.util.logging.*;

import markehme.factionsplus.extras.*;

import org.bukkit.*;
import org.bukkit.configuration.file.*;
import org.bukkit.plugin.java.*;

import com.avaje.ebean.*;


public abstract class FactionsPlusPlugin extends JavaPlugin {
	private boolean allowPluginToEnable=true;

	public boolean isAllowPluginToEnable() {
		return allowPluginToEnable;
	}

	public void setDisAllowPluginToEnable() {
		allowPluginToEnable = false;
	}
	
	@Override
	public void onEnable() {
		if (!isAllowPluginToEnable()) {
			throw bailOut( "Failed to load(scroll up 1 or more pages to see error)" +
					" thus we won't allow bukkit to enable at this time" );
		}
	}
	
	@Override
	public FileConfiguration getConfig() {
		throw Q.ni();//just in case something accidentally calls this, for now
	}
	
	@Override
	public void saveConfig() {
		throw Q.ni();//just in case something accidentally calls this, for now
	}
	
	@Override
	public void reloadConfig() {
		throw Q.ni();//just in case something accidentally calls this, for now
	}

	/**
	 * allowed to be used as: throw bailOut(..); so that eclipse won't 
	 * complain about NPEs because it doesn't see that it can "throw"<br>
	 * the "throw " in front of the call, is also used to make sure the reader(and eclipse) knows that the call will 
	 * unconditionally throw, as to
	 * not expect the code to continue<br>
	 * @param msg
	 * @return is a dummy return so that it can be used like: throw bailOut(...);
	 */
	public static RuntimeException bailOut(String msg) {
		throw bailOut(null, msg);
	}
	
	public static RuntimeException bailOut(Throwable cause, String msg) {
		severe(cause, msg);
		throw disableSelf(FactionsPlus.instance, true);
	}

	public static void warn(String logInfoMsg) {
	//		log.warn( FP_TAG_IN_LOGS+logInfoMsg );
			tellConsole(ChatColor.GOLD+FactionsPlus.FP_TAG_IN_LOGS+ChatColor.DARK_RED+"[WARNING]:"+ChatColor.RESET+logInfoMsg );//they are logged with [INFO] level
		}

	public static void info(String logInfoMsg) {
	//		log.info( FP_TAG_IN_LOGS+logInfoMsg );//log.info won't handle colors btw
			tellConsole(ChatColor.GOLD+FactionsPlus.FP_TAG_IN_LOGS+ChatColor.RESET+logInfoMsg );//they are logged with [INFO] level
		}

	public static void severe(String logInfoMsg) {
		FactionsPlus.log.severe( FactionsPlus.FP_TAG_IN_LOGS+logInfoMsg );//allowed so that [SEVERE] appears
		tellConsole(ChatColor.RED+FactionsPlus.FP_TAG_IN_LOGS+ChatColor.DARK_PURPLE+logInfoMsg);
	}
	
	
	/**
	 * @param cause if null => ignored
	 * @param logInfoMsg
	 */
	public static void severe( Throwable cause, String logInfoMsg ) {
		String msg = FactionsPlus.FP_TAG_IN_LOGS + logInfoMsg;
		if ( null == cause ) {
			FactionsPlus.log.severe( msg );// allowed so that [SEVERE] appears
		} else {
			FactionsPlus.log.log( Level.SEVERE, msg, cause );// allowed so that [SEVERE] appears
		}
		tellConsole( ChatColor.RED + FactionsPlus.FP_TAG_IN_LOGS + ChatColor.DARK_PURPLE + logInfoMsg );
	}
	
	/**
	 * allows the use of ChatColor in messages but they will be prefixed by [INFO]
	 * @param msg
	 */
	public static void tellConsole(String msg) {
		//nvm; find another way to display colored msgs in console without having [INFO] prefix
		//there's no other way it's done via ColouredConsoleSender of craftbukkit
		//there are only two ways: colors+[INFO] prefix, or no colors + whichever prefix
		Bukkit.getConsoleSender().sendMessage( msg);//this will log with [INFO] level
	}

	public static RuntimeException disableSelf(FactionsPlus fpInstance, boolean forceStop) {
		fpInstance.disableSelf();
		if (forceStop) {
//			FactionsPlus.log.log( Level.INFO, )
			throw new RuntimeException(FactionsPlus.FP_TAG_IN_LOGS+" execution stopped by disableSelf() which means read the above colored message");
		}
		return null;
	}

	/**
	 * calling this will not stop execution at the point where the call is made, but will mark the plugin as disabled<br>
	 * ie. shown in red when /plugins  is issued
	 * @param fpInstance
	 */
	public void disableSelf() {
		setEnabled( false );//it will call onDisable() if it was enabled
//		Bukkit.getPluginManager().disablePlugin( this );
		//it won't deregister commands ie. /f fc  will still work apparently, likely cause Factions plugin won't be disabled and it is
		//it that handles those commands that we potentially already registered to it (assuming this was called after)
	}

	
}
