package markehme.factionsplus;

import java.util.ArrayList;
import java.util.List;

import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.extras.LocketteFunctions;
import markehme.factionsplus.listeners.*;
import markehme.factionsplus.sublisteners.*;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.massivecraft.factions.entity.UConf;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.EventFactionsChunkChange;
import com.massivecraft.factions.event.EventFactionsCreate;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsRelationChange;

/**
 * Because of the nature of Factions, different worlds may or may not use features. So therefore, we create
 * our own sub-listeners, that we only call if in that world it is needed.
 * 
 * @author MarkehMe <mark@markeh.me>
 *
 */
public class FactionsPlusListener implements Listener {
	
	AnimalDamageSubListener animalDamageSubListener		= new AnimalDamageSubListener();
	AnnoucementsSubListener annoucementsSubListener		= new AnnoucementsSubListener();
	BanSubListener banSubListener						= new BanSubListener();
	DenyClaimSubListener denyClaimSubListener			= new DenyClaimSubListener();
	LiquidFlowSubListener liquidFlowSubListener			= new LiquidFlowSubListener();
	JailSubListener jailSubListener						= new JailSubListener();
	PowerBoostSubListener powerBoostSubListener			= new PowerBoostSubListener();
	PeacefulSubListener peacefulSubListener				= new PeacefulSubListener();
	CoreSubListener coreSubListener						= new CoreSubListener();
	
	public static List<String> pluginFeaturesEnabled = new ArrayList<String>();
	
	/**
	 * isEnabled ensures Factions and FactionsPlus are enabled in that world.
	 * 
	 * @param oid
	 * @return
	 */
	private boolean isEnabled(Object oid) {
		if(!UConf.get(oid).enabled || !FPUConf.get(oid).enabled) return false;		
		
		return true;
	}
	
	/**
	 * These are all plugin dependent, they have their own listeners
	 * this may change in the future - however, we can't have them in
	 * here!
	 */
	public void setupPermanentListeners(FactionsPlus instance) {
		pluginFeaturesEnabled.clear();
		
		if(FactionsPlus.pm.isPluginEnabled("Cannons"))
			CannonsListener.enableOrDisable(instance);
		
		if(FactionsPlus.pm.isPluginEnabled("Lockette"))
			LocketteFunctions.enableOrDisable(instance); // TODO: This should all be in the listener
		
		DisguiseListener.enableOrDisable(instance);
		
		if(FactionsPlus.pm.isPluginEnabled("Multiverse-Portals"))
			MultiversePortals.enableOrDisable(instance);
		
		if(FactionsPlus.pm.isPluginEnabled("CreativeGates"))
			CreativeGatesListener.enableOrDisable(instance); 
		
		if(FactionsPlus.pm.isPluginEnabled("ChestShop"))
			ChestShopListener.enableOrDisable(instance);
			
		if(FactionsPlus.pm.isPluginEnabled("ShowCaseStandalone"))
        	ShowCaseStandaloneListener.enableOrDisable(instance);
        
		if(FactionsPlus.pm.isPluginEnabled("Magic"))
        	MagicListener.enableOrDisable(instance);
        
        
	}
	
	/*
	 * Start all the event handlers 
	 */
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void playerFishEvent(PlayerFishEvent event) {
		if(!isEnabled(UPlayer.get(event.getPlayer()).getUniverse())) return;
		FPUConf fpuconf = FPUConf.get(UPlayer.get(event.getPlayer()).getUniverse());
		
		// Animal Damage Events
		event = animalDamageSubListener.playerFishEvent(event);
		
		// Core Events 
		if(fpuconf.strictFarming) event = coreSubListener.playerFishEvent(event);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void entityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if(!isEnabled(event.getDamager().getWorld())) return;
		FPUConf fpuconf = FPUConf.get(event.getDamager().getWorld());
		
		// Animal Damage Events
		event = animalDamageSubListener.entityDamageByEntityEvent(event);
		
		// Core Events
		if(fpuconf.allowAttackingWhileFlying) event = coreSubListener.entityDamageByEntityEvent(event);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void playerJoinEvent(PlayerJoinEvent event) {
		if(!isEnabled(event.getPlayer().getWorld())) return;
		FPUConf fpuconf = FPUConf.get(event.getPlayer().getWorld());
		
		// Announcements Events
		event = coreSubListener.playerJoinEvent(event);
		if(fpuconf.announcementsEnabled) event = annoucementsSubListener.playerJoinEvent(event);
		if(fpuconf.jailsEnabled) event = jailSubListener.playerJoinEvent(event);
		
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void playerMoveEvent(PlayerMoveEvent event) {
		if(!isEnabled(UPlayer.get(event.getPlayer()).getUniverse())) return;
		FPUConf fpuconf = FPUConf.get(UPlayer.get(event.getPlayer()).getUniverse());
		
		if(fpuconf.jailsEnabled) event = jailSubListener.playerMoveEvent(event);
		if(fpuconf.announcementsEnabled) event = annoucementsSubListener.playerMoveEvent(event);
		if(event.getPlayer().hasPermission("factionsplus.flightinownterritory")) event = coreSubListener.playerMoveEvent(event);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void eventFactionsMembershipChange(EventFactionsMembershipChange event) { 
		if(!isEnabled(event.getUPlayer().getUniverse())) return;
		FPUConf fpuconf = FPUConf.get(event.getUPlayer().getUniverse());
		
		event = coreSubListener.eventFactionsMembershipChange(event);
		if(fpuconf.bansEnabled) event = banSubListener.eventFactionsMembershipChange(event);
		if(fpuconf.jailsEnabled) event = jailSubListener.eventFactionsMembershipChange(event);
		if(fpuconf.enablePeacefulBoost) event = peacefulSubListener.eventFactionsMembershipChange(event);
		
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void blockFromToEvent(BlockFromToEvent event) {
		if(!isEnabled(event.getToBlock().getLocation())) return;
		FPUConf fpuconf = FPUConf.get(event.getBlock().getLocation());
		
		if(fpuconf.fixes.get("crossBorderLiquidFlowBlock")) event = liquidFlowSubListener.blockFromToEvent(event, fpuconf.fixes.get("crossBorderLiquidFlowBlockMakeCobblestone"));
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void playerRespawnEvent(PlayerRespawnEvent event) {
		if(!isEnabled(UPlayer.get(event.getPlayer()).getUniverse())) return;
		FPUConf fpuconf = FPUConf.get(event.getPlayer());

		if(fpuconf.jailsEnabled) event = jailSubListener.playerRespawnEvent(event);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void blockBreakEvent(BlockBreakEvent event) {
		if(!isEnabled(UPlayer.get(event.getPlayer()).getUniverse())) return;
		FPUConf fpuconf = FPUConf.get(event.getPlayer());
		
		if(fpuconf.jailsEnabled) event = jailSubListener.blockBreakEvent(event);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void blockPlaceEvent(BlockPlaceEvent event) {
		if(!isEnabled(UPlayer.get(event.getPlayer()).getUniverse())) return;
		FPUConf fpuconf = FPUConf.get(event.getPlayer());
		
		if(fpuconf.jailsEnabled) event = jailSubListener.blockPlaceEvent(event);

	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void asyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		if(!isEnabled(UPlayer.get(event.getPlayer()).getUniverse())) return;
		FPUConf fpuconf = FPUConf.get(event.getPlayer());
		
		if(fpuconf.jailsEnabled && fpuconf.denyChatWhileJailed) event = jailSubListener.asyncPlayerChatEvent(event);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void playerTeleportEvent(PlayerTeleportEvent event) {
		FPUConf fpuconf = FPUConf.get(event.getPlayer());
		
		if(fpuconf.jailsEnabled) event = jailSubListener.playerTeleportEvent(event);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void entityDeathEvent(EntityDeathEvent event) {
		if(!isEnabled(event.getEntity())) return;
		//FPUConf fpuconf = FPUConf.get(event.getEntity());
		
		event = powerBoostSubListener.entityDeathEvent(event);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void eventFactionsRelationChange(EventFactionsRelationChange event) {
		if(!isEnabled(event.getUSender())) return;
		//FPUConf fpuconf = FPUConf.get(event.getEntity());
		
		event = coreSubListener.eventFactionsRelationChange(event);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void projectileLaunchEvent(ProjectileLaunchEvent event) {
		if(!isEnabled(event.getEntity())) return;
		FPUConf fpuconf = FPUConf.get(event.getEntity());
		
		if(!fpuconf.allowSplashPotionsWhileFlying) event = coreSubListener.projectileLaunchEvent(event);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void entityShootBowEvent(EntityShootBowEvent event) {
		if(!isEnabled(event.getEntity())) return;
		FPUConf fpuconf = FPUConf.get(event.getEntity());
		
		if(!fpuconf.allowAttackingWhileFlying) event = coreSubListener.entityShootBowEvent(event);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void eventFactionsChunkChange(EventFactionsChunkChange event) {
		if(!isEnabled(event.getUSender())) return;
		FPUConf fpuconf = FPUConf.get(event.getUSender());
		
		if(fpuconf.enableWorldGuardRegionCheck) event = coreSubListener.eventFactionsChunkChange(event);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void entityDamageEvent(EntityDamageEvent event) {
		if(!isEnabled(event.getEntity())) return;
		FPUConf fpuconf = FPUConf.get(event.getEntity());
		
		if(fpuconf.makeSafeZoneExtraSafe) event = coreSubListener.entityDamageEvent(event); 
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void inventoryClickEvent(InventoryClickEvent event) {
		if(!isEnabled(event.getWhoClicked())) return;
		FPUConf fpuconf = FPUConf.get(event.getWhoClicked());
		
		if(fpuconf.disallowAccessToVillagersToOtherFactions) event = coreSubListener.inventoryClickEvent(event);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void eventFactionsCreate(EventFactionsCreate event) {
		if(!isEnabled(event.getUSender())) return;
		FPUConf fpuconf = FPUConf.get(event.getUSender());
		
		if(fpuconf.factionNameForceFirstLetterUppercase) event = coreSubListener.eventFactionsCreate(event); 
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void playerInteractEntityEvent(PlayerInteractEntityEvent event) {
		if(!isEnabled(event.getPlayer())) return;
		//FPUConf fpuconf = FPUConf.get(event.getPlayer());
		
		event = coreSubListener.playerInteractEntityEvent(event);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void playerShearEntityEvent(PlayerShearEntityEvent event) {
		if(!isEnabled(event.getPlayer())) return;
		FPUConf fpuconf = FPUConf.get(event.getPlayer());
		
		if(fpuconf.strictFarming) event = coreSubListener.playerShearEntityEvent(event);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void playerQuitEvent(PlayerQuitEvent event) {
		if(!isEnabled(event.getPlayer())) return;
		FPUConf fpuconf = FPUConf.get(event.getPlayer());
		
		if(fpuconf.factionJoinLeaveMessagesLockedToFaction) event = coreSubListener.playerQuitEvent(event); 
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void playerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		if(!isEnabled(event.getPlayer())) return;
		//FPUConf fpuconf = FPUConf.get(event.getPlayer());
		
		event = coreSubListener.playerCommandPreprocessEvent(event);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void playerDeathEvent(PlayerDeathEvent event) {
		if(!isEnabled(event.getEntity())) return;
		//FPUConf fpuconf = FPUConf.get(event.getEntity());

		event = coreSubListener.playerDeathEvent(event);
	}
	
}
