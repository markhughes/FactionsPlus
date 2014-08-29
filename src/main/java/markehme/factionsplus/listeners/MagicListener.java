package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusListener;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.MCore.LConf;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import com.elmakers.mine.bukkit.api.event.PreCastEvent;
import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UConf;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;

public class MagicListener implements Listener {
	
	/**
	 * Boolean to define if it is hooked in
	 */
	public static boolean isHooked = false;
	
	/**
	 * Store our listener 
	 */
	public static MagicListener listener;
	
	/**
	 * Plugin Name
	 */
	public static String pluginName = "Magic";
	
	/**
	 * Determine if a plugin is enabled, and if so - setup our listeners 
	 * @param instance
	 */
	public static final void enableOrDisable(FactionsPlus instance) {
 		PluginManager pm = Bukkit.getServer().getPluginManager();
			
 		// Check if plugin is enabled, and check if the plugin is integrated
		if(pm.isPluginEnabled(pluginName) && !isHooked) {
			listener = new MagicListener();
			
			pm.registerEvents(listener, instance);
			
			// Try again
			if(listener == null) {
				listener = new MagicListener();
				pm.registerEvents(listener, instance);
			}
			
			FactionsPlus.debug("Hooked into plugin: "+pluginName);
			FactionsPlusListener.pluginFeaturesEnabled.add(pluginName);
			
			// additional stuff
			magicAPI = (MagicAPI) Bukkit.getPluginManager().getPlugin("Magic");
		}
	}
	
	
	/********/
	
	public static MagicAPI magicAPI = null;
	
	public void preCastEvent(PreCastEvent event) {
		UPlayer uPlayer = UPlayer.get(event.getMage().getPlayer());
		UConf uconf = UConf.get(uPlayer);
		FPUConf fpuconf = FPUConf.get(uPlayer);
		
		if(fpuconf.magicRequiresMinimumPowerOf > 0) {
			if(!(uPlayer.getPower() >= fpuconf.magicRequiresMinimumPowerOf)) {
				uPlayer.msg(Txt.parse(LConf.get().magicNotEnoughPower, fpuconf.magicRequiresMinimumPowerOf));
				event.setCancelled(true);
				return;
			}
		}
		
		if(fpuconf.magicMustBeInAFactionToUse) {
			if(!uPlayer.hasFaction()) {
				uPlayer.msg(Txt.parse(LConf.get().magicNeedAFaction));
				event.setCancelled(true);
				return;
			}
		}
		
		Faction factionAtMage = BoardColls.get().getFactionAt(PS.valueOf(event.getMage().getLocation()));
		Faction factionAtSpell = BoardColls.get().getFactionAt(PS.valueOf(event.getSpell().getLocation()));
		Faction factionAtTarget = BoardColls.get().getFactionAt(PS.valueOf(event.getSpell().getTargetLocation()));
		
		Rel relToFactionAtMage = factionAtMage.getRelationTo(uPlayer);
		Rel relToFactionAtSpell = factionAtSpell.getRelationTo(uPlayer);
		Rel relToFactionAtTarget = factionAtTarget.getRelationTo(uPlayer);
		
		if(!fpuconf.magicAllowInWilderness) {
			if(factionAtMage.isNone() || factionAtSpell.isNone() || factionAtTarget.isNone()) {
				uPlayer.msg(Txt.parse(LConf.get().magicDenyInThisLand));
				event.setCancelled(true);
				return;	
			}
		}
		
		if(fpuconf.magicDisallowInFactions.contains(factionAtMage.getId()) || fpuconf.magicDisallowInFactions.contains(factionAtMage.getId()) || fpuconf.magicDisallowInFactions.contains(factionAtMage.getId())) {
			uPlayer.msg(Txt.parse(LConf.get().magicDenyInThisLand));
			event.setCancelled(true);
			return;
		}
		
		if(!fpuconf.magicAllowInOwnFaction) {
			if(factionAtMage.getId() == uPlayer.getFactionId() || factionAtSpell.getId() == uPlayer.getFactionId() || factionAtTarget.getId() == uPlayer.getFactionId()) {
				uPlayer.msg(Txt.parse(LConf.get().magicDenyInThisLand));
				event.setCancelled(true);
				return;			
			}
		}
		
		if(!fpuconf.magicAllowInAllyFaction) {
			if(relToFactionAtMage.equals(Rel.ALLY) || relToFactionAtSpell.equals(Rel.ALLY) || relToFactionAtTarget.equals(Rel.ALLY)) {
				uPlayer.msg(Txt.parse(LConf.get().magicDenyInThisLand));
				event.setCancelled(true);
				return;	
			}
		}
		
		if(!fpuconf.magicAllowInEnemyFaction) {
			if(relToFactionAtMage.equals(Rel.ENEMY) || relToFactionAtSpell.equals(Rel.ENEMY) || relToFactionAtTarget.equals(Rel.ENEMY)) {
				uPlayer.msg(Txt.parse(LConf.get().magicDenyInThisLand));
				event.setCancelled(true);
				return;	
			}
		}
		
		if(!fpuconf.magicAllowInWarzone) {
			if(factionAtMage.getId() == uconf.factionIdWarzone || factionAtSpell.getId() == uconf.factionIdWarzone || factionAtTarget.getId() == uconf.factionIdWarzone) {
				uPlayer.msg(Txt.parse(LConf.get().magicDenyInThisLand));
				event.setCancelled(true);
				return;	
			}
		}
		
		if(!fpuconf.magicAllowInSafezone) {
			if(factionAtMage.getId() == uconf.factionIdSafezone || factionAtSpell.getId() == uconf.factionIdSafezone || factionAtTarget.getId() == uconf.factionIdSafezone) {
				uPlayer.msg(Txt.parse(LConf.get().magicDenyInThisLand));
				event.setCancelled(true);
				return;	
			}
		}	
	}
}
