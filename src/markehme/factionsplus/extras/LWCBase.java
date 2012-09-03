package markehme.factionsplus.extras;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;


public abstract class LWCBase {
	private static final String pluginName_LWC="LWC";
	private static LWC lwc;//moved this to here, to avoid devs using this directly, use getLWC() instead which will try get LWCInstance 
	//before NPE-ing, if it does NPE then you simply forgot to check if LWC is enabled before calling it's functions ;)
	
	public static void refreshLWC() {
		lwc=getLWCInstance();
	}
	
	public static LWC getLWC() {
		//FIXME: caching this may be evil when something like plugman unloads/reloads lwc; maybe use LWC.getInstance(), 
		//for now refreshLWC() does it, but imagine if plugman reloads only the LWC plugin
		if (null == lwc) {
			lwc=getLWCInstance();//can still be null
		}
		return lwc;
	}
	
	public static boolean isLWC() {
		return null != getLWC();
	}
	
	public static LWC getLWCInstance() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		LWCPlugin plugin=(LWCPlugin)pm.getPlugin(pluginName_LWC);
		if ((null == plugin)||( ! pm.isPluginEnabled(pluginName_LWC))) {
			//FIXME: LWC can be not yet enabled, but could be loaded and will be enabled later
			//so maybe hook PluginEnableEvent, even tho LWC may always be enabled when this point is reached
			//due to soft-depend being in plugin.yml which would maybe have LWC load first, or does that happen only on (hard) depend?
			return null;
		}

		assert null != plugin;
		
		return plugin.getLWC(); 
	}
}
