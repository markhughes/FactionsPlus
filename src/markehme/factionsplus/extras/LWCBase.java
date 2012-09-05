package markehme.factionsplus.extras;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;


public abstract class LWCBase {
	private static final String pluginName_LWC="LWC";
	
	
	public final static LWC getLWC() {
		return LWC.getInstance();
	}
	
	public final static boolean isLWCPluginPresent() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		LWCPlugin plugin=(LWCPlugin)pm.getPlugin(pluginName_LWC);
		if ((null == plugin)||( ! pm.isPluginEnabled(pluginName_LWC))) {
			//old comment: LWC can be not yet enabled, but could be loaded and will be enabled later
			//so maybe hook PluginEnableEvent, even tho LWC may always be enabled when this point is reached
			//due to soft-depend being in plugin.yml which would maybe have LWC load first, or does that happen only on (hard) depend?
			//new:anyway, here plugman may have unloaded/disabled only LWC plugin, so we have to had checked for isPluginEnabled(LWC)
			return false;
		}else{
			return true;
		}

//		assert null != plugin;
//		
//		return plugin.getLWC(); 
	}
}
