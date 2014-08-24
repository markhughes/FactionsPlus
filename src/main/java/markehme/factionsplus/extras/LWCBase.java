package markehme.factionsplus.extras;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.listeners.LWCListener;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;


public abstract class LWCBase {
	private static final String pluginName_LWC="LWC";
	protected static final LWCListener lwcListener=new LWCListener();
	protected static boolean	alreadyRegistered=false;
	
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
	
	
	public static void deregListenerIfNeeded() {
		//by moving this from LWCFunctions unit into LWCBase, we can call it without getting NoClassDefFoundError
		if ( alreadyRegistered ) {
			try {
//				if ( LWCBase.isLWCPluginPresent() ) {<-this not needed
					// without the above "if", the following comment is true
					// Prints massive error when trying to run this method when LWC is not on the server
					// Reason being is because you are referencing com.griefcraft.scripting.Module (part of LWC)

					//this will fail to be reached when LWC is ie. unloaded by ie. plugman
					//but that doesn't mean that we don't still have registered the handler from before,
					//even if LWC isn't running/existing, the handler is still being triggered in this case
					//so must find a way to deregister it without causing LWC classes not found exceptions
					HandlerList.unregisterAll( lwcListener );
					FactionsPlus.info( "Stopped LWC listener" );
//				}
			} finally {
				alreadyRegistered = false;
			}
		}
	}
}
