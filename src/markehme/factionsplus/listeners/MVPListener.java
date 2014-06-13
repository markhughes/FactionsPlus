package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.extras.FType;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.util.Txt;
import com.onarandombox.MultiversePortals.event.MVPortalEvent;

public class MVPListener implements Listener {
	
	public static boolean isMVPIntegrated = false;
	public static MVPListener mvplistener;
	

	@EventHandler(priority=EventPriority.HIGH)
	public void MVPlayerTouchedPortalEvent(MVPortalEvent e) {
		
		if(FPUConf.isDisabled(UPlayer.get(e.getTeleportee()).getUniverse())) return;

		UPlayer uPlayer = UPlayer.get(e.getTeleportee());
		Faction factionAt = uPlayer.getFaction();
		
		FPUConf fpUConf = FPUConf.get(uPlayer);
				
		Rel rel = factionAt.getRelationTo(factionAt);
		
		if(FactionsPlus.permission.has(e.getTeleportee(), "factionsplus.useanyportal") || uPlayer.isInOwnTerritory() || uPlayer.isUsingAdminMode() ) {
			return;
		}
		
		if(FType.valueOf(factionAt).equals(FType.SAFEZONE)) {
			if(fpUConf.multiverse.containsKey("usePortalsInSafezone")) {
				if(!fpUConf.multiverse.get("usePortalsInSafezone")) {
					uPlayer.msg(Txt.parse(LConf.get().multiverseCantUseThisPortal));
					return;
				}
			}
			
			return;
		}
		
		if(FType.valueOf(factionAt).equals(FType.WARZONE)) {
			if(fpUConf.multiverse.containsKey("usePortalsInWarzone")) {
				if(!fpUConf.multiverse.get("usePortalsInWarzone")) {
					uPlayer.msg(Txt.parse(LConf.get().multiverseCantUseThisPortal));
					return;
				}
			}
			
			return;
		}
		
		if(FType.valueOf(factionAt).equals(FType.WILDERNESS)) {
			if(fpUConf.multiverse.containsKey("usePortalsInWilderness")) {
				if(!fpUConf.multiverse.get("usePortalsInWilderness")) {
					uPlayer.msg(Txt.parse(LConf.get().multiverseCantUseThisPortal));
				}
			}
			
			return;
		}
		
		if(rel.equals(Rel.ALLY)) {
			if(fpUConf.multiverse.containsKey("usePortalsInAlly")) {
				if(!fpUConf.multiverse.get("usePortalsInAlly")) {
					uPlayer.msg(Txt.parse(LConf.get().multiverseCantUseThisPortal));
					return;
				}
			}
		}
		
		if(rel.equals(Rel.ENEMY)) {
			if(fpUConf.multiverse.containsKey("usePortalsInEnemy")) {
				if(!fpUConf.multiverse.get("usePortalsInEnemy")) {
					uPlayer.msg(Txt.parse(LConf.get().multiverseCantUseThisPortal));
					return;
				}
			}
		}
		
		if(rel.equals(Rel.TRUCE)) {
			if(fpUConf.multiverse.containsKey("usePortalsInTruce")) {
				if(!fpUConf.multiverse.get("usePortalsInTruce")) {
					uPlayer.msg(Txt.parse(LConf.get().multiverseCantUseThisPortal));
					return;
				}
			}
		}
		
		if(rel.equals(Rel.NEUTRAL)) {
			if(fpUConf.multiverse.containsKey("usePortalsInNeutral")) {
				if(!fpUConf.multiverse.get("usePortalsInNeutral")) {
					uPlayer.msg(Txt.parse(LConf.get().multiverseCantUseThisPortal));
					return;
				}
			}
		}
		
		// If we reach here, it's fine. 
	}
	
	public static final void enableOrDisable(FactionsPlus instance){
 		PluginManager pm = Bukkit.getServer().getPluginManager();
			
		boolean isMVPplugin = pm.isPluginEnabled("Multiverse-Portals");
		
		if (isMVPplugin && !isMVPIntegrated) {
			assert ( null == mvplistener );
			
			mvplistener = new MVPListener();
			pm.registerEvents( mvplistener, instance );
			
			if(null == mvplistener) {
				mvplistener = new MVPListener();
				Bukkit.getServer().getPluginManager().registerEvents(mvplistener, instance);
			}
			
			FactionsPlus.debug("Hooked into Multiverse-portals.");
		}	
	}
}
