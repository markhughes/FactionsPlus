package markehme.factionsplus.listeners;

import java.util.HashMap;

import markehme.factionsplus.FactionsPlus;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;



public abstract class Listen {
	
	public static HashMap<Class<? extends Listener>, Listener>	bunchaListeners	= new HashMap<Class<? extends Listener>, Listener>();
	
	
	public static void startOrStopListenerAsNeeded( boolean expectedState, Class<? extends Listener> listenerClass ) {
		
		Listener listener = bunchaListeners.get( listenerClass );
		
		if ( expectedState ) {
			if ( null == listener ) {// listener wasn't already active, but it needs to be
				Throwable err = null;
				try {
					listener = listenerClass.newInstance();// it should have(a public) and expect to be new-ed using default
															// constructor
				} catch ( InstantiationException e ) {
					err = e;
				} catch ( IllegalAccessException e ) {
					err = e;
				} finally {
					if ( null != err ) {
						throw FactionsPlus.bailOut( err, "failed to create a new instance of listener " + listenerClass );
					}
				}
				Listener existing = bunchaListeners.put( listenerClass, listener );
				assert null == existing : "bad logic, could not have already existed and be here";
				Bukkit.getPluginManager().registerEvents( listener, FactionsPlus.instance );
				FactionsPlus.info( "Started "+listenerClass.getSimpleName()+" listener" );
			}// else already listening
			else {
				//TODO: perhaps find a way to notify of this and Started only on /f reloadfp and not onEnable, to avoid unneeded notifications
				FactionsPlus.info( listenerClass.getSimpleName()+" listener is still active" );
			}
		} else {
			if ( null != listener ) {// already listening while expected to not listen?
				HandlerList.unregisterAll( listener );
				Listener same1 = bunchaListeners.put( listenerClass, null );
				assert same1 == listener;
				// listener=null;
				FactionsPlus.info( "Removed "+listenerClass.getSimpleName()+" listener" );
			}
		}
	}
}
