package markehme.factionsplus.listeners;

import markehme.factionsplus.*;
import markehme.factionsplus.config.*;

import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;

public class PowerboostListener implements Listener{
	@EventHandler(priority=EventPriority.MONITOR)
	public void onEntityDeath(EntityDeathEvent event)
	{
		if (!checkFactionsSettings(event)) {
			return;
		}
		if ((event.getEntity() instanceof Player)) {
			Player p = (Player)event.getEntity();
			DamageCause causeOfDeath = event.getEntity().getLastDamageCause().getCause();
			if (p.getKiller() == null) {
				if ((causeOfDeath == DamageCause.ENTITY_ATTACK || causeOfDeath == DamageCause.PROJECTILE 
						|| causeOfDeath == DamageCause.ENTITY_EXPLOSION) &&
						(Config._powerboosts.extraPowerLossIfDeathByMob._ > 0.0D)) {
					Utilities.removePower(p, Config._powerboosts.extraPowerLossIfDeathByMob._);
					return;
				}
				if ((causeOfDeath == DamageCause.CONTACT) && 
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
		if(event.getEntity() instanceof Monster) {
			if ( event.getEntity().getKiller() == null) {
				return;
			}
			if ( Config._powerboosts.extraPowerWhenKillMonster._ > 0.0D) {
				Player k = event.getEntity().getKiller();
				Utilities.addPower( k, Config._powerboosts.extraPowerWhenKillMonster._);
			}
		}
	}

	private boolean checkFactionsSettings(EntityDeathEvent event) {
		//FIXME: 1.7 doesn't have: warZonePowerLoss and wildernessPowerLoss
		if (!Config._powerboosts.respectFactionsWarZonePowerLossRules._ && (!com.massivecraft.factions.Conf.warZonePowerLoss || !com.massivecraft.factions.Conf.wildernessPowerLoss || com.massivecraft.factions.Conf.worldsNoPowerLoss != null)) {
			if (event.getEntity() == null || !(event.getEntity() instanceof Player)) {
				return true; // Means we should continue with the rest of the code, as the dead entity is not a player and as such not subject to factions
				//TODO: Block power GAINS in powerloss disabled regions as well
			}
			FLocation floc = new FLocation(event.getEntity().getLocation());
			Faction owningFaction = Board.getFactionAt(floc);
			if(Utilities.isWilderness( owningFaction) && !com.massivecraft.factions.Conf.wildernessPowerLoss) {
				return false;
			}
			if(Utilities.isWarZone( owningFaction) && !com.massivecraft.factions.Conf.warZonePowerLoss) {
				return false;
			}
			if(Utilities.isSafeZone(owningFaction)) {
				return false;
			}
		}
		return true;
	}
}
