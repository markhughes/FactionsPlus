package markehme.factionsplus.listeners;

import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;

import org.bukkit.World;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;

import com.massivecraft.factions.FFlag;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.mcore.ps.PS;

public class PowerboostListener implements Listener{
	
	private static PowerboostListener powerboostlistener = null;
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDeath( EntityDeathEvent event ) {
		
		if( ( event.getEntity() instanceof Player ) ) {
			
			Player p = (Player)event.getEntity();
			
			if( ! canLosePowerWherePlayerIsAt( p ) ) {
				
				return;
				
			}
			
			EntityDamageEvent ldc = event.getEntity().getLastDamageCause();
			
			if( null == ldc ) {
			
				if( Config._powerboosts.extraPowerLossIfDeathByOther._ > 0 ) {
					Utilities.removePower( p, Config._powerboosts.extraPowerLossIfDeathByOther._ );
				}
				
				return;
				
			}
			
			DamageCause causeOfDeath = ldc.getCause();
			
			if( p.getKiller() == null ) {
				
				if(
					( causeOfDeath == DamageCause.ENTITY_ATTACK || causeOfDeath == DamageCause.PROJECTILE || causeOfDeath == DamageCause.ENTITY_EXPLOSION )
					&& Config._powerboosts.extraPowerLossIfDeathByMob._ > 0.0D
				) {
					
					Utilities.removePower(p, Config._powerboosts.extraPowerLossIfDeathByMob._);
					
					return;
					
				}
				
				if( ( causeOfDeath == DamageCause.CONTACT ) && 
						(Config._powerboosts.extraPowerLossIfDeathByCactus._ > 0.0D)) {
					Utilities.removePower(p, Config._powerboosts.extraPowerLossIfDeathByCactus._);
					return;
				}

				if ((causeOfDeath == DamageCause.BLOCK_EXPLOSION) && 
						(Config._powerboosts.extraPowerLossIfDeathByTNT._ > 0.0D)) {
					Utilities.removePower(p, Config._powerboosts.extraPowerLossIfDeathByTNT._);
					return;
				}

				if (((causeOfDeath == DamageCause.FIRE) || (causeOfDeath == DamageCause.FIRE_TICK)) && 
						(Config._powerboosts.extraPowerLossIfDeathByFire._ > 0.0D)) {
					Utilities.removePower(p, Config._powerboosts.extraPowerLossIfDeathByFire._);
					return;
				}

				if ((causeOfDeath == DamageCause.MAGIC) && 
						(Config._powerboosts.extraPowerLossIfDeathByPotion._ > 0.0D)) {
					Utilities.removePower(p, Config._powerboosts.extraPowerLossIfDeathByPotion._);
					return;
				}
				
				if ((causeOfDeath == DamageCause.SUICIDE)
						&& ( Config._powerboosts.extraPowerLossIfDeathBySuicide._ > 0.0D ) )
				{
					Utilities.removePower( p, Config._powerboosts.extraPowerLossIfDeathBySuicide._  );
					return;
				}
				
				if(Config._powerboosts.extraPowerLossIfDeathByOther._ > 0) {
					Utilities.removePower(p, Config._powerboosts.extraPowerLossIfDeathByOther._);
					return;
				}
			} else {
				if ( ( causeOfDeath == DamageCause.ENTITY_ATTACK ) || ( causeOfDeath == DamageCause.PROJECTILE ) ) {
					if ( Config._powerboosts.extraPowerLossIfDeathByPVP._ > 0.0D ) {
						Utilities.removePower( p, Config._powerboosts.extraPowerLossIfDeathByPVP._ );
					}
					
					if ( Config._powerboosts.extraPowerWhenKillPlayer._ > 0.0D ) {
						Player k = p.getKiller();
						Utilities.addPower( k, Config._powerboosts.extraPowerWhenKillPlayer._) ;
					}
					return;
				}
			}
		}
		
		//2of2
		if(event.getEntity() instanceof Monster) {
			Player k = event.getEntity().getKiller();
			if (k == null) {
				return;
			}
			if ( Config._powerboosts.extraPowerWhenKillMonster._ > 0.0D) {
				// done: Block power GAINS in powerloss disabled regions as well: this is gonna be tricky
				if (canLosePowerWherePlayerIsAt(k)) {//this IF is here to save some CPU cycles since likely the 0 value is above
					//allow increase only if player can also lose ie. disallow gain in worldsNoPowerLoss worlds 
					Utilities.addPower( k, Config._powerboosts.extraPowerWhenKillMonster._);
				}
			}
		}
	}

	
	public static final boolean canLosePowerWherePlayerIsAt( Player player ) {
		Faction factionAtFeet = BoardColls.get().getFactionAt( PS.valueOf( player.getLocation() ) );
		return canLosePowerInThisFaction(factionAtFeet, player.getWorld());
	}
	
	public static final boolean canLosePowerInThisFaction(Faction faction, World worldName) {
		if ( ! faction.getFlag(FFlag.POWERLOSS) ) { //this handles safezone too
			return false;
		} else {
			// safezone check is not needed here because both 1.6 bridge and 1.7 powerloss are false for safezone,
			// except that 1.7 can set it to true if wanted but it's false by default in factions.json
			
			// warzone will always lose power regardless of worldsNoPowerLoss setting, Factions plugin does this too.
			if ( !Utilities.isWarZone( faction ) && null != worldName && MConf.get().worldsNoPowerLoss.contains( worldName.getName() ) )
			{
				return false;
			}
		}
		//
		return true;
	}


}
