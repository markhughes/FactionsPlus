package markehme.factionsplus.listeners;

import markehme.factionsplus.config.Config;
import markehme.factionsplus.extras.FType;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.Acrobot.ChestShop.Events.PreShopCreationEvent;
import com.Acrobot.ChestShop.Events.PreShopCreationEvent.CreationOutcome;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.ps.PS;

public class ChestShopListener implements Listener {

	@EventHandler(ignoreCancelled=true)
	public void onChestShopCreate(PreShopCreationEvent event) {
		
		Faction factionAtSign = BoardColls.get().getFactionAt(PS.valueOf(event.getSign().getLocation()));
		FType factionType = FType.valueOf(factionAtSign);
		
		if(factionType == FType.WILDERNESS && !Config._extras._protection.allowShopsInWilderness._) {
			
			event.setOutcome( CreationOutcome.NO_PERMISSION_FOR_TERRAIN ) ;
			
			return;
		}
		
		if( factionType == FType.FACTION && !Config._extras._protection.allowShopsInTerritory._ 
				&& factionAtSign.getId() == UPlayer.get(event.getPlayer()).getFactionId()) {
			
			event.setOutcome( CreationOutcome.NO_PERMISSION_FOR_TERRAIN );
			
			return;
		}
		
		if( factionAtSign.getId() != UPlayer.get(event.getPlayer()).getFactionId() ) {
			
			
			
		}
	}
}
