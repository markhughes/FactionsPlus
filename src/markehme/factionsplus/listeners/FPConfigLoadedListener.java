package markehme.factionsplus.listeners;

import markehme.factionsplus.config.*;
import markehme.factionsplus.events.*;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;



public class FPConfigLoadedListener implements Listener {
	
	@EventHandler
	public void onConfigLoaded( @SuppressWarnings( "unused" ) FPConfigLoadedEvent event ) {
        Config._economy.enableOrDisableEconomy();//TODO: move this into onConfigLoaded method which triggers on that event(not yet done)
	}
}
