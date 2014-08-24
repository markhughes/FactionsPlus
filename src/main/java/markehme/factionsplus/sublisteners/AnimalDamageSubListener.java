package markehme.factionsplus.sublisteners;

import java.util.Arrays;
import java.util.List;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.MCore.LConf;
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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.potion.Potion;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;

public class AnimalDamageSubListener {
	
	/**
	 * List of 'safe mobs' that can be protected
	 */	
	private List<EntityType> protectedEntities = Arrays.asList(
		EntityType.CHICKEN,
		EntityType.COW,
		EntityType.MUSHROOM_COW,
		EntityType.OCELOT,
		EntityType.WOLF,
		EntityType.PIG,
		EntityType.IRON_GOLEM,
		EntityType.BAT,
		EntityType.SNOWMAN,
		EntityType.VILLAGER,
		EntityType.HORSE,
		EntityType.SQUID,
		EntityType.SHEEP
	);
	
	/**
	 * Called on fish events related to the animal damage features
	 * 
	 * @param event
	 * @return
	 */
	public PlayerFishEvent playerFishEvent(PlayerFishEvent event) {
		// Ensure something was caught			
		if(event.getCaught() != null) {
			// Fetch the UPlayer Object 
			UPlayer uPlayer = UPlayer.get(event.getPlayer());
			
			// Fetch the entity that we caught 
			EntityType entity = event.getCaught().getType();
			
			if(protectedEntities.contains(entity) && !uPlayer.isUsingAdminMode() && !FactionsPlus.permission.has(event.getPlayer(), "factionsplus.cankillallmobs") && !canKillCheck(uPlayer)) {	
				uPlayer.msg(Txt.parse(LConf.get().fpCantDamageThisMob));
				event.setCancelled(true);
			}
		}
		
		return event;
	}
	
	/**
	 * Called on entity damage events related to animal damage features
	 * 
	 * @param event
	 * @return
	 */
	public EntityDamageByEntityEvent entityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if(event.getDamager() == null) return event;
		
		// This is to check if the player is damaging an animal
		if(event.getDamager() instanceof Player) {
			
			// Fetch the player 
			Player damagingPlayer = (Player) event.getDamager();
			UPlayer UDamagingPlayer = UPlayer.get(damagingPlayer);
			
			// Fetch the entity being damaged
			EntityType entity = event.getEntityType();
			
			if(protectedEntities.contains(entity) && !UDamagingPlayer.isUsingAdminMode() && !FactionsPlus.permission.has(damagingPlayer, "factionsplus.cankillallmobs") && !canKillCheck(UDamagingPlayer)) {	
				damagingPlayer.sendMessage(Txt.parse(LConf.get().fpCantDamageThisMob));
				event.setCancelled(true) ;
				
				return event;
			}
		}
		
		// The next few things are to check damages done by players, but using something else (e.g. arrow, snowball, etc)
		Projectile projectile = null;
		
		if((event.getDamager() instanceof Arrow)) {
			projectile = (Arrow) event.getDamager();
			
			if(!(projectile.getShooter() instanceof Player)) return event;
					
		} else if((event.getDamager() instanceof Snowball)) {
			projectile = (Snowball) event.getDamager();
			
			if(!(projectile.getShooter() instanceof Player)) return event;
			
		} else if((event.getDamager() instanceof Potion)) {
			projectile = (ThrownPotion) event.getDamager();
			
			if(!(projectile.getShooter() instanceof Player)) return event;	
			
		} else if((event.getDamager() instanceof ThrownPotion)) {
			projectile = (ThrownPotion) event.getDamager();
			
			if(!(projectile.getShooter() instanceof Player)) return event;
			
		} else if((event.getDamager() instanceof EnderPearl)) {
			projectile = (EnderPearl) event.getDamager();
			
			if(!(projectile.getShooter() instanceof Player)) return event;
			
		} else if((event.getDamager() instanceof Egg)) {
			projectile = (Egg) event.getDamager();
			
			if(!(projectile.getShooter() instanceof Player)) return event;
			
		} else if((event.getDamager() instanceof Fireball)) {
			projectile = (Fireball) event.getDamager();
			
			if(!(projectile.getShooter() instanceof Player)) return event;
			
		} 
		
		if(projectile != null) {
			EntityType entity = event.getEntityType();

			Player damagingPlayer = (Player) projectile.getShooter();
			UPlayer UDamagingPlayer = UPlayer.get(damagingPlayer);
			
			if(protectedEntities.contains(entity) && !UDamagingPlayer.isUsingAdminMode() && !FactionsPlus.permission.has(damagingPlayer, "factionsplus.cankillallmobs") && !canKillCheck(UDamagingPlayer)) {
				damagingPlayer.sendMessage(Txt.parse(LConf.get().fpCantDamageThisMob));
				event.setCancelled(true);
				
				return event;
			}
		}
		
		return event;
	}
	
	/**
	 * This does a check on players location vs. configuration to see if it is allowed 
	 * 
	 * @param uPlayer
	 * @return
	 */
	private boolean canKillCheck(UPlayer uPlayer) {
		FPUConf fpuconf = FPUConf.get(uPlayer.getUniverse());
		
		// If the player is part of WILDERNESS (none) and is in a normal factions land, then deny 
		if(FType.valueOf( uPlayer.getFaction() ) == FType.WILDERNESS
				&& FType.valueOf(BoardColls.get().getFactionAt(PS.valueOf(uPlayer.getPlayer().getLocation()))) == FType.FACTION) {
			
			return false;
			
		}
		
		// If we're in a safezone, and protecting safezone passive mobs, then deny
		if(fpuconf.protectPassiveMobsSafeZone
				&& FType.valueOf(BoardColls.get().getFactionAt(PS.valueOf(uPlayer.getPlayer().getLocation()))) == FType.SAFEZONE) {
			return false;
		}
		
		// Confirm allyMob check 
		if(!fpuconf.allowFactionKill.get("allyMobs")) {
			if(BoardColls.get().getFactionAt(PS.valueOf(uPlayer.getPlayer().getLocation())).getRelationTo(uPlayer).equals(Rel.ALLY)) {
				return false;
			}
		}
		
		// Confirm neutralMob check
		if(!fpuconf.allowFactionKill.get("neturalMobs")) {
			if(BoardColls.get().getFactionAt(PS.valueOf(uPlayer.getPlayer().getLocation())).getRelationTo(uPlayer).equals(Rel.ENEMY)) {
				return false;
			}
		}
		
		// Confirm enemyMob check
		if(fpuconf.allowFactionKill.get("enemyMobs")) {
			if(BoardColls.get().getFactionAt(PS.valueOf(uPlayer.getPlayer().getLocation())).getRelationTo(uPlayer).equals(Rel.NEUTRAL) && 
					!BoardColls.get().getFactionAt(PS.valueOf( uPlayer.getPlayer().getLocation())).isNone()) {
				return false;		
			}
		}
		
		// Confirm truceMob check
		if(fpuconf.allowFactionKill.get("truceMobs")) {			
			if(BoardColls.get().getFactionAt(PS.valueOf(uPlayer.getPlayer().getLocation())).getRelationTo(uPlayer).equals(Rel.TRUCE)) {
				return false;
			}
		}
		
		// All clear
		return true;
	}
}
