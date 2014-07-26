package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.extras.FType;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.potion.Potion;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;

/**
 * The AnimalDamagerListener adds protection for certain mobs 
 * inside of Faction land.
 *
 */
public class AnimalDamageListener implements Listener {
	
	/**
	 * Protects mobs from fishing hooks. 
	 * 
	 * @param event
	 */
	@EventHandler(priority=EventPriority.LOW)
	public void onFishingHook(PlayerFishEvent event) {
		
		if(!FPUConf.get(UPlayer.get(event.getPlayer()).getUniverse()).enabled) return; // Universe support

		// This will check for damage via a fishing hook			
		if(event.getCaught() != null) {
			UPlayer uPlayer = UPlayer.get(event.getPlayer());
			
			EntityType entity = event.getCaught().getType();
			
			if(
				protectEntity(entity)
				&& (!uPlayer.isUsingAdminMode())
				&& (!FactionsPlus.permission.has(event.getPlayer(), "factionsplus.cankillallmobs"))
				&& (!canKillCheck(uPlayer))
			) {
					
				uPlayer.msg(Txt.parse(LConf.get().fpCantDamageThisMob));
				event.setCancelled(true) ;
						
			}
		}
	}
	
	/**
	 * Protects mobs being attacked by players. 
	 * 
	 * @param event
	 */
	@EventHandler(priority=EventPriority.LOW)
	public void onEntityAttacked(EntityDamageByEntityEvent event) {
		
		if(!FPUConf.get(UPlayer.get(event.getDamager()).getUniverse()).enabled) return; // Universe support
		
		// This is to check if the player is damaging an animal
		if((event.getDamager() instanceof Player)) {
			
			Player damagingPlayer = (Player) event.getDamager();
			
			UPlayer UDamagingPlayer = UPlayer.get(damagingPlayer);
			
			EntityType entity = event.getEntityType();
			
			if(
				protectEntity(entity)
				&& (!UDamagingPlayer.isUsingAdminMode())
				&& (!FactionsPlus.permission.has(damagingPlayer, "factionsplus.cankillallmobs"))
				&& (!canKillCheck(UDamagingPlayer))
			) {
				
				// nup, nup. no do that *snaps fingers* 
				damagingPlayer.sendMessage(Txt.parse(LConf.get().fpCantDamageThisMob));
				event.setCancelled(true) ;
					
			}
		
		}
		
	}
	
	@EventHandler(priority=EventPriority.LOW, ignoreCancelled = true)
	public void onEntityAttackThing( EntityDamageByEntityEvent event ) {
		
		if(!FPUConf.get(UPlayer.get(event.getDamager()).getUniverse()).enabled) return; // Universe support
		
		// this is to check if the player is attacking with an arrow, etc
		Projectile projectile = null;
		
		if(event.getDamager() == null) {
			return;
		}
		
		if((event.getDamager() instanceof Arrow)) {
			projectile = (Arrow) event.getDamager();
			
			if(!(projectile.getShooter() instanceof Player)) {
				return;
			}
					
		} else if((event.getDamager() instanceof Snowball)) {
			projectile = (Snowball) event.getDamager();
			
			if(!(projectile.getShooter() instanceof Player)) {
				return;
			}
			
		} else if((event.getDamager() instanceof Potion)) {
			
			projectile = (ThrownPotion) event.getDamager();
			
			if(!(projectile.getShooter() instanceof Player)) {
				return;	
			}

		} else if((event.getDamager() instanceof ThrownPotion)) {
			
			projectile = (ThrownPotion) event.getDamager();
			
			if(!(projectile.getShooter() instanceof Player)) {
				return;
			}
			
		} else if((event.getDamager() instanceof EnderPearl)) {
			
			projectile = (EnderPearl) event.getDamager();
			
			if(!(projectile.getShooter() instanceof Player)) {
				return;
			}
			
		} else if((event.getDamager() instanceof Egg)) {
			
			projectile = (Egg) event.getDamager();
			
			if(!(projectile.getShooter() instanceof Player)) {
				return;
			}
			
		} else if((event.getDamager() instanceof Fireball)) {
			
			projectile = (Fireball) event.getDamager();
			
			if(!(projectile.getShooter() instanceof Player)) {
				return;
			}
		} 
		
		if(projectile != null) {
			
			EntityType entity = event.getEntityType();

			Player damagingPlayer = (Player) projectile.getShooter();
			
			UPlayer UDamagingPlayer = UPlayer.get(damagingPlayer);
			
			if(
				protectEntity(entity)
				&& (!UDamagingPlayer.isUsingAdminMode())
				&& (!FactionsPlus.permission.has(damagingPlayer, "factionsplus.cankillallmobs"))
				&& (!canKillCheck(UDamagingPlayer))
			) {
					
				damagingPlayer.sendMessage(Txt.parse(LConf.get().fpCantDamageThisMob));
				event.setCancelled(true);
						
			}
		}
	}
	
	/**
	 * Confirms that the entity type needs to be protected 
	 * @param entity
	 * @return
	 */
	private boolean protectEntity(EntityType entity) {
		
		// This list is for mobs that don't attack players, and won't attack back.
		// If outdated or missing something, please make a pull request! 
		if( 
			entity == EntityType.CHICKEN || 
			entity == EntityType.COW  || 
			entity == EntityType.MUSHROOM_COW || 
			entity == EntityType.OCELOT ||
			entity == EntityType.WOLF ||
			entity == EntityType.PIG ||
			entity == EntityType.IRON_GOLEM ||
			entity == EntityType.BAT ||
			entity == EntityType.SNOWMAN ||
			entity == EntityType.VILLAGER ||
			entity == EntityType.HORSE ||
			entity == EntityType.SQUID ||
			entity == EntityType.SHEEP 
		) {
			
			return true;
			
		} else {
			
			return false;
			
		}
	}
	
	/**
	 * This does a check on players location vs. configuration to see if it is allowed 
	 * @param uPlayer
	 * @return
	 */
	private boolean canKillCheck(UPlayer uPlayer) {
		
		// If the player is part of WILDERNESS (none) and is in a normal factions land, then deny 
		if(FType.valueOf( uPlayer.getFaction() ) == FType.WILDERNESS
				&& FType.valueOf(BoardColls.get().getFactionAt(PS.valueOf(uPlayer.getPlayer().getLocation()))) == FType.FACTION) {
			
			return false;
			
		}
		
		// If we're in a safezone, and protecting safezone passive mobs, then deny
		if(FPUConf.get(uPlayer.getUniverse()).protectPassiveMobsSafeZone
				&& FType.valueOf(BoardColls.get().getFactionAt(PS.valueOf(uPlayer.getPlayer().getLocation()))) == FType.SAFEZONE) {
			return false;
		}
		
		// Confirm allyMob check 
		if(!FPUConf.get(uPlayer.getUniverse()).allowFactionKill.get("allyMobs")) {
			if(BoardColls.get().getFactionAt(PS.valueOf(uPlayer.getPlayer().getLocation())).getRelationTo(uPlayer).equals(Rel.ALLY)) {
				return false;
			}
		}
		
		// Confirm neutralMob check
		if(!FPUConf.get(uPlayer.getUniverse()).allowFactionKill.get("neturalMobs")) {
			if(BoardColls.get().getFactionAt(PS.valueOf(uPlayer.getPlayer().getLocation())).getRelationTo(uPlayer).equals(Rel.ENEMY)) {
				return false;
			}
		}
		
		// Confirm enemyMob check
		if(FPUConf.get(uPlayer.getUniverse()).allowFactionKill.get("enemyMobs")) {
			if(BoardColls.get().getFactionAt(PS.valueOf(uPlayer.getPlayer().getLocation())).getRelationTo(uPlayer).equals(Rel.NEUTRAL) && 
					!BoardColls.get().getFactionAt(PS.valueOf( uPlayer.getPlayer().getLocation())).isNone()) {
				return false;		
			}
		}
		
		// Confirm truceMob check
		if(FPUConf.get(uPlayer.getUniverse()).allowFactionKill.get("truceMobs")) {			
			if(BoardColls.get().getFactionAt(PS.valueOf(uPlayer.getPlayer().getLocation())).getRelationTo(uPlayer).equals(Rel.TRUCE)) {
				return false;
			}
		}
		
		// No issues - allow it.
		return true;
	}
}
