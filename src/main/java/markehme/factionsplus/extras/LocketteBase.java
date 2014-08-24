package markehme.factionsplus.extras;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.listeners.LocketteListener;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

public class LocketteBase {
	private static final String 				pluginName				=		"Lockette";
	protected static final LocketteListener 	LocketteListener		=		new LocketteListener();
	protected static boolean					alreadyRegistered		=		false;
	
	
	public final static boolean isLockettePluginPresent() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		
		if ( ! pm.isPluginEnabled( pluginName ) ) {
			
			return false;
			
		} else {
			
			return true;
			
		}
		
	}
	
	public static void deregListenerIfNeeded() {
		if ( alreadyRegistered ) {
			try {
				HandlerList.unregisterAll( LocketteListener );
				
				FactionsPlus.info( "Stopped Lockette listener." );
			} finally {
				alreadyRegistered = false;
			}
		}
	}
}
