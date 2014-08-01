package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsPlusPlugin;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.extras.LWCBase;
import markehme.factionsplus.extras.LWCFunctions;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.griefcraft.scripting.event.LWCProtectionRegisterEvent;
import com.massivecraft.factions.FFlag;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.EventFactionsChunkChange;
import com.massivecraft.massivecore.util.Txt;



public class LWCListener implements Listener {
		
	@EventHandler(priority = EventPriority.MONITOR )
	public void onLandClaim(EventFactionsChunkChange event) {
		if(FPUConf.isDisabled(event.getUSender().getUniverse())) return;
		
		if(!FPUConf.get(event.getUSender().getUniverse()).removeLWCProtectionOn) return;


		UPlayer fPlayer = event.getUSender();
		
		try {
			if(!LWCBase.isLWCPluginPresent()) { //ie. run this on server: plugman unload lwc
				fPlayer.sendMessage(ChatColor.RED+"LWC plugin is not active.");
				return;
			}
			
			int removedProtections = LWCFunctions.clearLocks(event.getChunk().getLocation(), fPlayer );
			if(removedProtections > 0 ) {
				fPlayer.sendMessage(Txt.parse(LConf.get().LWCLocksRemoved, removedProtections));
			}
		} catch ( Exception cause ) {
			event.setCancelled(true); // disallow claim
			FactionsPlusPlugin.severe(cause, "internal error clearing LWC locks on land claim, inform admin to check console." );
			fPlayer.sendMessage( "[FactionsPlus] " + "internal error clearing LWC locks on land claim, inform admin to check console." );
		}
	}
	
	@EventHandler
	public void onCreateProtection(LWCProtectionRegisterEvent event) {
		if(FPUConf.isDisabled(UPlayer.get(event.getPlayer()).getUniverse())) return;

		if(FPUConf.get(UPlayer.get(event.getPlayer()).getUniverse()).onlyPeacefulCreateLWCProtections) {
			UPlayer uPlayer = UPlayer.get(event.getPlayer());
			
			if(!uPlayer.getFaction().getFlag(FFlag.PEACEFUL)) {
				uPlayer.msg(Txt.parse(LConf.get().LWCOnlyPeaceful));
				event.setCancelled(true);
			}
		}
	}
}
