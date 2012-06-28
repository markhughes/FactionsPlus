package markehme.factionsplus.listeners;

import markehme.factionsplus.*;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.massivecraft.factions.event.FPlayerJoinEvent;

public class PeacefulListener implements Listener{
	@EventHandler
	public void onFPlayerJoinEvent(FPlayerJoinEvent event) {
		if(event.isCancelled()) {
			return;
		}
		int boostValue = Config.config.getInt(FactionsPlus.confStr_powerBoostIfPeaceful) ;
		if(boostValue> 0) {
			if(event.getFaction().isPeaceful()) { // TODO: Prepare for 1.7.x and the removal of isPeaceful()
				Utilities.addPower(event.getFPlayer(),boostValue);
			}
		}
	}

}
