package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Utilities;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.massivecraft.factions.event.FPlayerJoinEvent;

public class PeacefulListener implements Listener{
	@EventHandler
	public void onFPlayerJoinEvent(FPlayerJoinEvent event) {
		if(event.isCancelled()) {
			return;
		}
		if(FactionsPlus.config.getInt(FactionsPlus.confStr_powerBoostIfPeaceful) > 0) {
			if(event.getFaction().isPeaceful()) { // TODO: Prepare for 1.7.x and the removal of isPeaceful()
				Utilities.addPower(event.getFPlayer(), FactionsPlus.config.getInt("confStr_powerBoostIfPeaceful"));
			}
		}
	}

}
