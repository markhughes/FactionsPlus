package markehme.factionsplus.listeners;

import org.bukkit.Server;
import org.bukkit.event.Listener;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;

import com.massivecraft.factions.event.EventFactionsChunkChange;
import com.massivecraft.factions.event.EventFactionsCreate;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsRelationChange;

public class CoreListener implements Listener {
	
	public static Server fp;
	
	@Deprecated
	public void onFactionsEventRelationChange(EventFactionsRelationChange event) {
		
	}
	
	@Deprecated 
	public void onPlayerThrowPotion(ProjectileLaunchEvent event) {
		
	}
	
	@Deprecated
	public void onPlayerBowShoot(EntityShootBowEvent event) {

	}
	
	@Deprecated
	public void onPlayerAttack(final EntityDamageByEntityEvent event) {
		
	}
	
	@Deprecated
	public void onPlayerMove(PlayerMoveEvent event) {
		
	}
	
	@Deprecated
	public void onLandClaim(EventFactionsChunkChange event) {
		
	}
	
	@Deprecated
	public void onEntityDamage(EntityDamageEvent event) {
		
	}
	
	@Deprecated
	public void onVillagerTrade(InventoryClickEvent event) {
		
	}
	
	@Deprecated
	public void onPlayerCreateFaction(EventFactionsCreate event) {
		
	}
	
	@Deprecated
	public void onPlayerFish(PlayerFishEvent event) {
		
	}
	
	@Deprecated
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		
	}
	
	@Deprecated
	public void onPlayerMilkEvent(PlayerInteractEntityEvent event) {
		
	}
    
	@Deprecated
	public void onPlayerShearEntityEvent(PlayerShearEntityEvent event) {
	    
	}
	
	@Deprecated
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		
	}
	
	@Deprecated
	public void onPlayerLeave(PlayerQuitEvent e) {

	}
	
	@Deprecated
	public void onRunBlockedCommand(PlayerCommandPreprocessEvent event) {

	}
	
	@Deprecated
	public void onFactionsMembershipChange(EventFactionsMembershipChange event) {
		
	}
	
	@Deprecated
	public void onPlayerDeath(PlayerDeathEvent event) {
		
	}
}
