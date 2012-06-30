package markehme.factionsplus.listeners;

import markehme.factionsplus.*;
import markehme.factionsplus.config.*;

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
						(Config.powerboosts.extraPowerLossIfDeathByMob > 0.0D)) {
					Utilities.removePower(p, Config.powerboosts.extraPowerLossIfDeathByMob);
					return;
				}
				if ((causeOfDeath == DamageCause.CONTACT) && 
						(Config.powerboosts.extraPowerLossIfDeathByCactus) > 0.0D) {
					Utilities.removePower(p, Config.powerboosts.extraPowerLossIfDeathByCactus);
					return;
				}

				if ((causeOfDeath == DamageCause.BLOCK_EXPLOSION) && 
						(Config.powerboosts.extraPowerLossIfDeathByTNT > 0.0D)) {
					Utilities.removePower(p, Config.powerboosts.extraPowerLossIfDeathByTNT);
					return;
				}

				if (((causeOfDeath == DamageCause.FIRE) || (causeOfDeath == DamageCause.FIRE_TICK)) && 
						(Config.powerboosts.extraPowerLossIfDeathByFire > 0.0D)) {
					Utilities.removePower(p, Config.powerboosts.extraPowerLossIfDeathByFire);
					return;
				}

				if ((causeOfDeath == DamageCause.MAGIC) && 
						(Config.powerboosts.extraPowerLossIfDeathByPotion > 0.0D)) {
					Utilities.removePower(p, Config.powerboosts.extraPowerLossIfDeathByPotion);
					return;
				}
				
				if ((causeOfDeath == DamageCause.SUICIDE)
						&& ( Config.powerboosts.extraPowerLossIfDeathBySuicide > 0.0D ) )
				{
					Utilities.removePower( p, Config.powerboosts.extraPowerLossIfDeathBySuicide  );
					return;
				}
				
				if(Config.powerboosts.extraPowerLossIfDeathByOther > 0) {
					Utilities.removePower(p, Config.powerboosts.extraPowerLossIfDeathByOther);
					return;
				}
			} else {//non-null killer
				if ( ( causeOfDeath == DamageCause.ENTITY_ATTACK ) || ( causeOfDeath == DamageCause.PROJECTILE ) ) {
					if ( Config.powerboosts.extraPowerLossIfDeathByPVP > 0.0D ) {
						Utilities.removePower( p, Config.powerboosts.extraPowerLossIfDeathByPVP );
					}
					
					if ( Config.powerboosts.extraPowerWhenKillPlayer > 0.0D ) {
						Player k = p.getKiller();
						Utilities.addPower( k, Config.powerboosts.extraPowerWhenKillPlayer) ;
					}
					return;
				}
			}
		}
	}
}
