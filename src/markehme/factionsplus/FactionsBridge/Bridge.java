package markehme.factionsplus.FactionsBridge;

import org.bukkit.*;
import org.bukkit.plugin.*;

import markehme.factionsplus.*;


/**
 * this bridge is used to allow using features from both 1.6 and 1.7 factions without modifying FactionsPlus code<br />
 * all bindings are updated on init()<br>
 * for now used only for Factions
 */
public abstract class Bridge {
	public static FactionsAny factions;
	private final static String factionsPluginName="Factions";//case sensitive
	
	//technically this should not have a deinit() unless plugin totally disables when onDisable() which it doesn't
	//ie. commands still work, in which case we don't want NPEs due to Bridge.factions being null
	public static void init() {
		
		Plugin plugin = Bukkit.getPluginManager().getPlugin(factionsPluginName);
		if (null == plugin) {
			throw FactionsPlus.bailOut( "missing required plugin "+factionsPluginName );
		} else {//I think the depend inside plugin.yml will make sure Factions is enabled prior to FactionsPlus yes?
			if ( !plugin.isEnabled() ) {
				throw FactionsPlus.bailOut( factionsPluginName+" is not yet enabled" );
			}
		}
		
		String factionsVersion = plugin.getDescription().getVersion();
		
		if(factionsVersion.startsWith("1.6")) {
			factions=new Factions16();
		} else {
			if (factionsVersion.startsWith("1.7")) {
				factions=new Factions17();
			}else {
				throw FactionsPlus.bailOut( "Unknown "+factionsPluginName+" version `"+factionsVersion+"`" );
			}
		}
		
		assert null != factions;//stating the obvious
	}
}
