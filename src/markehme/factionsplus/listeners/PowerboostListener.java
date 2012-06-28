package markehme.factionsplus.listeners;

import markehme.factionsplus.*;

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
						(Config.config.getDouble(Config.confStr_extraPowerLossIfDeathByMob) > 0.0D)) {
					Utilities.removePower(p, Config.config.getDouble(Config.confStr_extraPowerLossIfDeathByMob));
					return;
				}
				if ((causeOfDeath == DamageCause.CONTACT) && 
						(Config.config.getDouble(Config.confStr_extraPowerLossIfDeathByCactus) > 0.0D)) {
					Utilities.removePower(p, Config.config.getDouble(Config.confStr_extraPowerLossIfDeathByCactus));
					return;
				}

				if ((causeOfDeath == DamageCause.BLOCK_EXPLOSION) && 
						(Config.config.getDouble(Config.confStr_extraPowerLossIfDeathByTNT) > 0.0D)) {
					Utilities.removePower(p, Config.config.getDouble(Config.confStr_extraPowerLossIfDeathByTNT));
					return;
				}

				if (((causeOfDeath == DamageCause.FIRE) || (causeOfDeath == DamageCause.FIRE_TICK)) && 
						(Config.config.getDouble(Config.confStr_extraPowerLossIfDeathByFire) > 0.0D)) {
					Utilities.removePower(p, Config.config.getDouble(Config.confStr_extraPowerLossIfDeathByFire));
					return;
				}

				if ((causeOfDeath == DamageCause.MAGIC) && 
						(Config.config.getDouble(Config.confStr_extraPowerLossIfDeathByPotion) > 0.0D)) {
					Utilities.removePower(p, Config.config.getDouble(Config.confStr_extraPowerLossIfDeathByPotion));
					return;
				}
				
				if ((causeOfDeath == DamageCause.SUICIDE)
						&& ( Config.config.getDouble( Config.confStr_extraPowerLossIfDeathBySuicide ) > 0.0D ) )
				{
					Utilities.removePower( p, Config.config.getDouble( Config.confStr_extraPowerLossIfDeathBySuicide ) );
					return;
				}
				
				if(Config.config.getDouble(Config.confStr_extraPowerLossIfDeathByOther) > 0) {
					Utilities.removePower(p, Config.config.getDouble(Config.confStr_extraPowerLossIfDeathByOther));
					return;
				}
			} else {//non-null killer
				if ( ( causeOfDeath == DamageCause.ENTITY_ATTACK ) || ( causeOfDeath == DamageCause.PROJECTILE ) ) {
					if ( Config.config.getDouble( Config.confStr_extraPowerLossIfDeathByPVP) > 0.0D ) {
						Utilities.removePower( p, Config.config.getDouble( Config.confStr_extraPowerLossIfDeathByPVP) );
					}
					
					if ( Config.config.getDouble( Config.confStr_extraPowerWhenKillPlayer) > 0.0D ) {
						Player k = p.getKiller();
						Utilities.addPower( k, Config.config.getDouble( Config.confStr_extraPowerWhenKillPlayer) );
					}
					return;
				}
			}
		}
	}
}
