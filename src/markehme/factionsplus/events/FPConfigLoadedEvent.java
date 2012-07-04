package markehme.factionsplus.events;

import org.bukkit.event.*;


/**
 * this is just to allow running your code after the config was loaded<br>
 * you cannot cancel this, but you can throw(tho it won't propagate so it's useless throwing)<br> 
 */
public class FPConfigLoadedEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
    
    @Override
	public HandlerList getHandlers() {
        return handlers;
    }
     
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public FPConfigLoadedEvent() {
    	super();//not async
    	
    }
}
