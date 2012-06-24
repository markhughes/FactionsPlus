package markehme.factionsplus.extras;

import org.bukkit.*;
import org.bukkit.plugin.*;

import markehme.factionsplus.*;


/**
 * this bridge is used to allow using features from both 1.6 and 1.7 factions without modifying FactionsPlus code<br />
 * all bindings are updated on init()<br>
 * for now used only for Factions
 */
public class Bridge {
	public static FactionsAny factions;
	private final static String factionsPluginName="Factions";//case sensitive
	
	public static void init(FactionsPlus pluginSelf) {
		
		Plugin plugin = Bukkit.getPluginManager().getPlugin(factionsPluginName);
		if (null == plugin) {
			throw FactionsPlus.bailOut( pluginSelf, "missing required plugin "+factionsPluginName );
		}
		
		String factionsVersion = plugin.getDescription().getVersion();
		
		if(factionsVersion.startsWith("1.6")) {
			factions=new Factions16(pluginSelf);
		} else {
			if (factionsVersion.startsWith("1.7")) {
				factions=new Factions17(pluginSelf);
			}else {
				throw FactionsPlus.bailOut( pluginSelf, "Unknown "+factionsPluginName+" version `"+factionsVersion+"`" );
			}
		}
		
		assert null != factions;//stating the obvious
	}
}
