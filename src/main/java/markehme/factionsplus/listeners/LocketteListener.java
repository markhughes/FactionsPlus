package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsPlusPlugin;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.extras.LocketteBase;
import markehme.factionsplus.extras.LocketteFunctions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.EventFactionsChunkChange;
import com.massivecraft.massivecore.util.Txt;

public class LocketteListener implements Listener  {
	
	@EventHandler(priority = EventPriority.MONITOR )
	public void onLandClaim(EventFactionsChunkChange event) {
		
		UPlayer uPlayer = event.getUSender();
		
		if(!FPUConf.get(uPlayer.getUniverse()).enabled) return;
		if(!FPUConf.get(uPlayer.getUniverse()).removeSignProtectionOn) return;
		
		try {
			if (!LocketteBase.isLockettePluginPresent()) return;
			
			int removedProtections = LocketteFunctions.removeLocketteLocks(event.getChunk(), uPlayer);
			if (removedProtections > 0) {
				uPlayer.sendMessage(Txt.parse(LConf.get().locketteLocksRemoved, removedProtections));
			}
			
		} catch (Exception cause) {
			event.setCancelled(true); 
			FactionsPlusPlugin.severe(cause, "Internal error clearing Lockette locks on land claim, inform admin to check console.");
			uPlayer.msg("[FactionsPlus] Internal error clearing Lockette locks on land claim, inform admin to check console.");
		}
	}
}
