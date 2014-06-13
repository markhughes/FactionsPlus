package markehme.factionsplus.extras;

import markehme.factionsplus.FactionsPlus;

import org.bukkit.plugin.Plugin;

import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;;

/**
 * - "factions" flag: 
 *   Which Factions can bypass the region check.
 *   It could be one faction (example: factiona) or multiple (example: factiona, factionb)
 *   
 * - "factions-enforcing" flag:
 *   If true, this will restrict usage to the values of factions-allow-*. 
 *   THIS ALSO means that only those in factions-allow-*
 *    
 * - "factions-allow-peaceful" flag:
 *   If factions-enforcing is enabled, this will allow peaceful factions to claim inside the region.
 *   
 * - "factions-allow-normal" flag:
 *   If factions-enforcing is enabled, Normal (not peaceful) factions can claim inside the region.  
 *   
 *
 */
public class WGFlagIntegration {
	
	/**
	 * The WorldGuard Custom Flags plugin
	 */
	WGCustomFlagsPlugin p;
	
	/**
	 * Our flag (not created though - done on class creation)
	 */
	public StringFlag FLAG_FACTIONS;
	public StateFlag FLAG_FACTIONS_ENFORCING;
	public StateFlag FLAG_FACTIONS_ALLOW_PEACEFUL;
	public StateFlag FLAG_FACTIONS_ALLOW_NORMAL;
	
	/**
	 * fetch the plugin, and create our flag
	 */
	public WGFlagIntegration() {
		p = getWGCustomFlags();
		
		FLAG_FACTIONS = new StringFlag("factions"); 
		FLAG_FACTIONS_ENFORCING = new StateFlag("factions-enforcing", false);
		FLAG_FACTIONS_ALLOW_PEACEFUL = new StateFlag("factions-allow-peaceful", true);
		FLAG_FACTIONS_ALLOW_NORMAL = new StateFlag("factions-allow-normal", true);
		
	}
	
	/**
	 * Adds the flag(s)
	 * In the future, more flags may be added
	 */
	public void addFlags() {
		p.addCustomFlag(FLAG_FACTIONS);
		p.addCustomFlag(FLAG_FACTIONS_ALLOW_PEACEFUL);
		p.addCustomFlag(FLAG_FACTIONS_ALLOW_NORMAL);		
	}
	
	/**
	 * Fetch the WGCustomFlags
	 * @return
	 */
	private WGCustomFlagsPlugin getWGCustomFlags() {
		Plugin plugin = FactionsPlus.instance.getServer().getPluginManager().getPlugin("WGCustomFlags");
		
		if(plugin == null || !(plugin instanceof WGCustomFlagsPlugin)) {
			return null;
		}
		
		return (WGCustomFlagsPlugin) plugin;
	}
}
