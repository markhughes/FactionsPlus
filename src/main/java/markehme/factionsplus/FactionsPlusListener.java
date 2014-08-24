package markehme.factionsplus;

import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.extras.LocketteFunctions;
import markehme.factionsplus.listeners.CannonsListener;
import markehme.factionsplus.listeners.ChestShopListener;
import markehme.factionsplus.listeners.CreativeGatesListener;
import markehme.factionsplus.listeners.DisguiseListener;
import markehme.factionsplus.listeners.MVPListener;
import markehme.factionsplus.listeners.ShowCaseStandaloneListener;
import markehme.factionsplus.sublisteners.*;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.massivecraft.factions.entity.UConf;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.EventFactionsMembershipChange;

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
		CannonsListener.enableOrDisable(instance);
		LocketteFunctions.enableOrDisable(instance); // TODO: This should all be in the listener
        DisguiseListener.enableOrDisable(instance);
        MVPListener.enableOrDisable(instance); // TODO: rename this listener
        CreativeGatesListener.enableOrDisable(instance); 
        CannonsListener.enableOrDisable(instance);
        ChestShopListener.enableOrDisable(instance);;
        ShowCaseStandaloneListener.enableOrDisable(instance);
	}
	
	/*
	 * Start all the event handlers 
	 */
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void playerFishEvent(PlayerFishEvent event) {
		if(!isEnabled(UPlayer.get(event.getPlayer()).getUniverse())) return;
		//FPUConf fpuconf = FPUConf.get(UPlayer.get(event.getPlayer()).getUniverse());
		
		// Animal Damage Events
		event = animalDamageSubListener.playerFishEvent(event);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void entityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if(!isEnabled(event.getDamager().getWorld())) return;
		//FPUConf fpuconf = FPUConf.get(event.getDamager().getWorld());
		
		// Animal Damage Events
		event = animalDamageSubListener.entityDamageByEntityEvent(event);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void playerJoinEvent(PlayerJoinEvent event) {
		if(!isEnabled(event.getPlayer().getWorld())) return;
		FPUConf fpuconf = FPUConf.get(event.getPlayer().getWorld());
		
		// Announcements Events
		if(fpuconf.announcementsEnabled) event = annoucementsSubListener.playerJoinEvent(event);
		if(fpuconf.jailsEnabled) event = jailSubListener.playerJoinEvent(event);
		
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void playerMoveEvent(PlayerMoveEvent event) {
		if(!isEnabled(UPlayer.get(event.getPlayer()).getUniverse())) return;
		FPUConf fpuconf = FPUConf.get(UPlayer.get(event.getPlayer()).getUniverse());
		
		if(fpuconf.jailsEnabled) event = jailSubListener.playerMoveEvent(event);
		if(fpuconf.announcementsEnabled) event = annoucementsSubListener.playerMoveEvent(event);
		
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void eventFactionsMembershipChange(EventFactionsMembershipChange event) { 
		if(!isEnabled(event.getUPlayer().getUniverse())) return;
		FPUConf fpuconf = FPUConf.get(event.getUPlayer().getUniverse());
		
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
}
