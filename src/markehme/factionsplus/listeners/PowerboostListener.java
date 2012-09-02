package markehme.factionsplus.listeners;

import markehme.factionsplus.*;
import markehme.factionsplus.FactionsBridge.*;
import markehme.factionsplus.config.*;

import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.massivecraft.factions.*;
import com.massivecraft.factions.struct.*;

public class PowerboostListener implements Listener{
	@EventHandler(priority=EventPriority.MONITOR)
	public void onEntityDeath(EntityDeathEvent event)
	{
		
		//ok this worked but it's integrated below inside canLosePowerInThisArea() because Factions allows 
		//powerloss in warzone of nopowerloss worlds, and by commenting this out we also do that, and save a few CPU cycles since
		//the checks are not done for every mob, but for every player (and for every mob only when extraPowerWhenKillMonster>0)
		//FIXME: allow warzone to powerloss even in nopowerloss worlds, as does Factions code. But also make sure this works in 1.7 too,
		//not just in 1.6
		if (Utilities.noPowerLossWorld(event.getEntity().getWorld())) {
			//this also means, you don't gain extra power when killing monster(see below code)
			return;
		}
		
		if ((event.getEntity() instanceof Player)) {
			Player p = (Player)event.getEntity();
			
			if (!canLosePowerInThisArea(p)) {
				// this check is supposed to be for Player only, maybe move the call for this method below
				//we don't want this check to happen on every entity death on the server
				return;//this will deny extra gain or loss from below
			}
//			FactionsPlus.info( "here");
			
			
			EntityDamageEvent ldc = event.getEntity().getLastDamageCause();//it can be null, see issue 60
			if (null == ldc) {
				//"null if hitherto unharmed"
				//how odd that it died and yet the last damage event is null, did it dot die via prev damage event ? 
				//and last one got cancelled?
				if(Config._powerboosts.extraPowerLossIfDeathByOther._ > 0) {
					Utilities.removePower(p, Config._powerboosts.extraPowerLossIfDeathByOther._);
				}
				return;
			}
			DamageCause causeOfDeath = ldc.getCause();
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
				if (canLosePowerInThisArea(k)) {
					//allow increase only if player can also lose ie. disallow gain in worldsNoPowerLoss worlds 
					Utilities.addPower( k, Config._powerboosts.extraPowerWhenKillMonster._);
				}
			}
		}
	}

	/**
	 * @param p player
	 * @return always true in Factions 1.7
	 */
	private final boolean canLosePowerInThisArea(Player p) {
		//TODO: ok, looks like faction.getFlag(FFlag.POWERLOSS) is Conf.warZonePowerLoss  and Conf.wildernessPowerLoss 
		if ( Bridge.factions.isFactions16() ) {
			// Factions 1.7 doesn't have: warZonePowerLoss and wildernessPowerLoss, so we only check these if we're alongside
			// 1.6.x, this still means that we cannot compile this with Factions 1.7, it will compile err below, but it's ok since
			//we're using the same FP jar for both anyway (just compile with Factions 1.6, it will run alongside 1.7) 
			//fixing: the logic here is broken, if this config is false, then the powerloss are respected! but they should be 
			//when this is true, right?
			
			if ( Config._powerboosts.respectFactionsWarZonePowerLossRules._
				&& ( !Conf.warZonePowerLoss 
						|| !Conf.wildernessPowerLoss 
						|| ((null != Conf.worldsNoPowerLoss)
						   &&(!Conf.worldsNoPowerLoss.isEmpty()))
				   ) )
			{
				//so, if respect?  and one of warzone/wilderness/nopowerlossworlds is set to not lose power on player death then:
					// TODO: Block power GAINS in powerloss disabled regions as well: this is gonna be tricky
				FLocation floc = new FLocation( p.getLocation() );
				Faction factionAtFeet = Board.getFactionAt( floc );
//				FPlayer fp=FPlayers.i.get(p);
//				assert null != fp;
				
				
				if ( !Conf.wildernessPowerLoss && Utilities.isWilderness( factionAtFeet ) ) {
					return false;
				}
				
				//if warZonePowerLoss is true then it overrides worldsNoPowerLoss as per Factions code seen here:
				//com.massivecraft.factions.listeners.FactionsEntityListener.onEntityDeath(EntityDeathEvent)  (F3 on the class not the method)
				if (Utilities.isWarZone( factionAtFeet ) ) {
					//always lose power in warzone, even in worldsNoPowerLoss, unless warZonePowerLoss is false (then you don't lose)
					return Conf.warZonePowerLoss;
				}else {
					//not in warzone ? maybe we're in a nopowerloss world in which case we lose no power
					if ( (null != Conf.worldsNoPowerLoss) && (Conf.worldsNoPowerLoss.contains(p.getWorld().getName())) ) {
						return false;
					}
				}
				
				if ( Utilities.isSafeZone( factionAtFeet ) ) {
					return false;
				}
			}
//			return true;
//		}else {
//			return (null != Conf.worldsNoPowerLoss) && (Conf.worldsNoPowerLoss.contains(p.getWorld().getName()));
		}
		return true;
	}
}
