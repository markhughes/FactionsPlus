package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Utilities;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PowerboostListener implements Listener{
	@EventHandler(priority=EventPriority.MONITOR)
	public void onEntityDeath(EntityDeathEvent event)
	{
		if ((event.getEntity() instanceof Player)) {
			Player p = (Player)event.getEntity();
			DamageCause causeOfDeath = event.getEntity().getLastDamageCause().getCause();
			if (p.getKiller() == null) {
				if ((causeOfDeath == DamageCause.ENTITY_ATTACK || causeOfDeath == DamageCause.PROJECTILE 
						|| causeOfDeath == DamageCause.ENTITY_EXPLOSION) &&
						(FactionsPlus.config.getDouble(FactionsPlus.confStr_extraPowerLossIfDeathByMob) > 0.0D)) {
					Utilities.removePower(p, FactionsPlus.config.getDouble(FactionsPlus.confStr_extraPowerLossIfDeathByMob));
					return;
				}
				if ((causeOfDeath == DamageCause.CONTACT) && 
						(FactionsPlus.config.getDouble(FactionsPlus.confStr_extraPowerLossIfDeathByCactus) > 0.0D)) {
					Utilities.removePower(p, FactionsPlus.config.getDouble(FactionsPlus.confStr_extraPowerLossIfDeathByCactus));
					return;
				}

				if ((causeOfDeath == DamageCause.BLOCK_EXPLOSION) && 
						(FactionsPlus.config.getDouble(FactionsPlus.confStr_extraPowerLossIfDeathByTNT) > 0.0D)) {
					Utilities.removePower(p, FactionsPlus.config.getDouble(FactionsPlus.confStr_extraPowerLossIfDeathByTNT));
					return;
				}

				if (((causeOfDeath == DamageCause.FIRE) || (causeOfDeath == DamageCause.FIRE_TICK)) && 
						(FactionsPlus.config.getDouble(FactionsPlus.confStr_extraPowerLossIfDeathByFire) > 0.0D)) {
					Utilities.removePower(p, FactionsPlus.config.getDouble(FactionsPlus.confStr_extraPowerLossIfDeathByFire));
					return;
				}

				if ((causeOfDeath == DamageCause.MAGIC) && 
						(FactionsPlus.config.getDouble(FactionsPlus.confStr_extraPowerLossIfDeathByPotion) > 0.0D)) {
					Utilities.removePower(p, FactionsPlus.config.getDouble(FactionsPlus.confStr_extraPowerLossIfDeathByPotion));
					return;
				}
				
				if ((causeOfDeath == DamageCause.SUICIDE)
						&& ( FactionsPlus.config.getDouble( FactionsPlus.confStr_extraPowerLossIfDeathBySuicide ) > 0.0D ) )
				{
					Utilities.removePower( p, FactionsPlus.config.getDouble( FactionsPlus.confStr_extraPowerLossIfDeathBySuicide ) );
					return;
				}
				
				if(FactionsPlus.config.getDouble(FactionsPlus.confStr_extraPowerLossIfDeathByOther) > 0) {
					Utilities.removePower(p, FactionsPlus.config.getDouble(FactionsPlus.confStr_extraPowerLossIfDeathByOther));
					return;
				}
			} else {//non-null killer
				if ( ( causeOfDeath == DamageCause.ENTITY_ATTACK ) || ( causeOfDeath == DamageCause.PROJECTILE ) ) {
					if ( FactionsPlus.config.getDouble( FactionsPlus.confStr_extraPowerLossIfDeathByPVP) > 0.0D ) {
						Utilities.removePower( p, FactionsPlus.config.getDouble( FactionsPlus.confStr_extraPowerLossIfDeathByPVP) );
					}
					
					if ( FactionsPlus.config.getDouble( FactionsPlus.confStr_extraPowerWhenKillPlayer) > 0.0D ) {
						Player k = p.getKiller();
						Utilities.addPower( k, FactionsPlus.config.getDouble( FactionsPlus.confStr_extraPowerWhenKillPlayer) );
					}
					return;
				}
			}
		}
	}
}
