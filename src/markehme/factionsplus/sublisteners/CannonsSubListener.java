package markehme.factionsplus.sublisteners;

import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.extras.FType;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;

import at.pavlov.cannons.event.CannonUseEvent;

public class CannonsSubListener {
	
	public CannonUseEvent cannonUseEvent(CannonUseEvent event) {
		UPlayer uPlayer = UPlayer.get(event.getPlayer());
		
		if(uPlayer == null) return event;
		
		if(FPUConf.get(uPlayer.getUniverse()).cannons.get("useCannonsInEnemy")) {
			if(uPlayer.isInEnemyTerritory()) {
				uPlayer.msg(Txt.parse(LConf.get().cannonsCantUseInEnemy));
				event.setCancelled(true);
				return event;
			}
		}
		
		if(FPUConf.get(uPlayer.getUniverse()).cannons.get("useCannonsInOwn")) {
			if(uPlayer.isInOwnTerritory() ) {
				uPlayer.msg(Txt.parse(LConf.get().cannonsCantUseInOwn));
				event.setCancelled(true);
				return event;
			}
		}
		
		if(FPUConf.get(uPlayer.getUniverse()).cannons.get("useCannonsInWilderness")) {
			if(FType.valueOf(BoardColls.get().getFactionAt(PS.valueOf(uPlayer.getPlayer().getLocation()))) == FType.WILDERNESS) {
				uPlayer.msg(Txt.parse(LConf.get().cannonsCantUseInWilderness));
				event.setCancelled( true );
				return event;
			}
		}
		
		return event;
	}
}
