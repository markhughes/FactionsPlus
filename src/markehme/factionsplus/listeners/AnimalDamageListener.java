package markehme.factionsplus.listeners;

import markehme.factionsplus.config.Config;
import markehme.factionsplus.extras.FType;
import markehme.factionsplus.references.FP;

import org.bukkit.ChatColor;
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
import com.massivecraft.mcore.ps.PS;

public class AnimalDamageListener implements Listener {

	private static AnimalDamageListener		ADL			= null;

	@EventHandler(priority=EventPriority.LOW, ignoreCancelled = true)
	public void onFishingHook( PlayerFishEvent event ) {
		
		// This will check for damage via a fishing hook
		
		// is it possible that it couldn't be ..?
		if( event.getPlayer() instanceof Player ) {
			
			if( event.getCaught() != null ) {
				
				Player damagingPlayer = (Player) event.getPlayer();
				
				UPlayer UDamagingPlayer = UPlayer.get( damagingPlayer );
				
				EntityType entity = event.getCaught().getType();
				if(
						protectEntity( entity )
						&& ( !UDamagingPlayer.isUsingAdminMode() )
						&& ( !FP.permission.has( damagingPlayer, "factionsplus.cankillallmobs") )
						&& ( ! canKillCheck( UDamagingPlayer ) )
					) {
						
						damagingPlayer.sendMessage( ChatColor.RED + "You can't damage this mob type in this Faction land." );
						event.setCancelled( true ) ;
							
					}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOW, ignoreCancelled = true)
	public void onEntityAttacked( EntityDamageByEntityEvent event ) {
		
		// This is to check if the player is damaging an animal
		
		// is the "damager" a player? 
		if( ( event.getDamager() instanceof Player ) ) {
			
			Player damagingPlayer = (Player) event.getDamager();
			
			UPlayer UDamagingPlayer = UPlayer.get(damagingPlayer);
			
			EntityType entity = event.getEntityType();
			
			if(
				protectEntity( entity )
				&& ( !UDamagingPlayer.isUsingAdminMode() )
				&& ( !FP.permission.has( damagingPlayer, "factionsplus.cankillallmobs") )
				&& ( ! canKillCheck( UDamagingPlayer ) )
			) {
				
				// nup, nup. no do that *snaps fingers* 
				damagingPlayer.sendMessage( ChatColor.RED + "You can't damage this mob type in this Faction land." );
				event.setCancelled( true ) ;
					
			}
		
		}
		
	}
	
	@EventHandler(priority=EventPriority.LOW, ignoreCancelled = true)
	public void onEntityAttackThing( EntityDamageByEntityEvent event ) {
		
		
		// this is to check if the player is attacking with an arrow, etc
		Projectile projectile = null;
		
		if( ( event.getDamager() instanceof Arrow ) ) {
			
			projectile = (Arrow) event.getDamager();
			
			if( ! ( projectile.getShooter() instanceof Player ) ) {
				
				return;
				
			}
					
		} else if( ( event.getDamager() instanceof Snowball ) ) {
			
			projectile = (Snowball) event.getDamager();
			
			if( ! ( projectile.getShooter() instanceof Player ) ) {
				
				return;
				
			}
			
		} else if( ( event.getDamager() instanceof Potion ) ) {
			
			// TODO: (do we cast ThrownPotion here?)
			projectile = (ThrownPotion) event.getDamager();
			
			if( ! ( projectile.getShooter() instanceof Player ) ) {
				
				return;
				
			}

		} else if( ( event.getDamager() instanceof ThrownPotion ) ) {
			
			projectile = (ThrownPotion) event.getDamager();
			
			if( ! ( projectile.getShooter() instanceof Player ) ) {
				
				return;
				
			}
			
		} else if( ( event.getDamager() instanceof EnderPearl ) ) {
			
			projectile = (EnderPearl) event.getDamager();
			
			if( ! ( projectile.getShooter() instanceof Player ) ) {
				
				return;
				
			}
			
		} else if( ( event.getDamager() instanceof Egg ) ) {
			
			projectile = (Egg) event.getDamager();
			
			if( ! ( projectile.getShooter() instanceof Player ) ) {
				
				return;
				
			}
			
		} else if( ( event.getDamager() instanceof Fireball ) ) {
			
			projectile = (Fireball) event.getDamager();
			
			if( ! ( projectile.getShooter() instanceof Player ) ) {
				
				return;
				
			}
		} 
		
		if( projectile != null ) {
			
			EntityType entity = event.getEntityType();

			Player damagingPlayer = (Player) projectile.getShooter();
			
			UPlayer UDamagingPlayer = UPlayer.get(damagingPlayer);
			
			if(
					protectEntity( entity )
					&& ( ! UDamagingPlayer.isUsingAdminMode() )
					&& ( !FP.permission.has( damagingPlayer, "factionsplus.cankillallmobs") )
					&& ( ! canKillCheck( UDamagingPlayer ) )
				) {
					
					// nup, nup. no do that *snaps fingers* 
					damagingPlayer.sendMessage( ChatColor.RED + "You can't damage this mob type in this Faction land." );
					event.setCancelled( true ) ;
						
				}
		}
	}
	
	private boolean protectEntity(EntityType entity) {
		
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
	
	private boolean canKillCheck(UPlayer uPlayer) {
		
		// If the player is part of WILDERNESS (none) and is in a normal factions land, deny! 
		if( FType.valueOf( uPlayer.getFaction() ) == FType.WILDERNESS
				&& FType.valueOf(  BoardColls.get().getFactionAt( PS.valueOf( uPlayer.getPlayer().getLocation() ) ) ) == FType.FACTION ) {
			
			return false;
			
		}
		
		if ( Config._extras._protection.protectSafeAnimalsInSafeZone._ ) {
			
			if ( FType.valueOf( BoardColls.get().getFactionAt( PS.valueOf( uPlayer.getPlayer().getLocation() ) ) ) == FType.SAFEZONE ) {

				return false;
				
			}

		}
		
		if( ! Config._extras._protection.allowFactionKillAlliesMobs._ ) {
			
			if ( BoardColls.get().getFactionAt( PS.valueOf( uPlayer.getPlayer().getLocation() ) ).getRelationTo( uPlayer ).equals( Rel.ALLY ) ) {

				return false;
				
			}
		}
	
		if( ! Config._extras._protection.allowFactionKillEnemyMobs._ ) {
			
			if( BoardColls.get().getFactionAt( PS.valueOf( uPlayer.getPlayer().getLocation() )).getRelationTo( uPlayer ).equals( Rel.ENEMY ) ) {
				
				return false;
				
			}
		}
		
		if( ! Config._extras._protection.allowFactionKillNeutralMobs._ ) {
			
			if( BoardColls.get().getFactionAt( PS.valueOf( uPlayer.getPlayer().getLocation() )).getRelationTo( uPlayer ).equals( Rel.NEUTRAL ) &&
				!BoardColls.get().getFactionAt( PS.valueOf( uPlayer.getPlayer().getLocation() )).isNone()) {
				
				return false;
				
			}
		}
		
		return true;
	}
}
