package markehme.factionsplus.listeners;

import markehme.factionsplus.*;
import markehme.factionsplus.config.*;
import markehme.factionsplus.events.*;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;



public class FPConfigLoadedListener implements Listener {
	
	@EventHandler
	public void onConfigLoaded( @SuppressWarnings( "unused" ) FPConfigLoadedEvent event ) {
        Config._economy.enableOrDisableEconomy();
        //TODO: add more here and make sure they can change states between on/off just like they would by a server 'reload' command
        //because this hook is called every time the config is reloaded, which means some things could have been previously enabled
        //and now the config may dictate that they are disabled (state changed) so we must properly handle that behaviour.
        TeleportsListener.initOrDeInit(FactionsPlus.instance);
	}
}
