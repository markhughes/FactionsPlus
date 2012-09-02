package markehme.factionsplus.listeners;

import markehme.factionsplus.*;
import markehme.factionsplus.FactionsBridge.*;
import markehme.factionsplus.config.*;

import org.bukkit.*;
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
		//done: allow warzone to powerloss even in nopowerloss worlds, as does Factions code. But also make sure this works in 1.7 too,
		//not just in 1.6
//		
		//1of2
		if ((event.getEntity() instanceof Player)) {
			Player p = (Player)event.getEntity();
			
			if (!shouldLosePowerWherePlayerIsAt(p)) {
				// this check is supposed to be for Player only, maybe move the call for this method below
				//we don't want this check to happen on every entity death on the server
				return;//this will deny extra gain or loss from below
			}else {
				//if it should lose power, then, we still don't allow it in nopowerloss worlds
				//FIXME: this is no good, since it would not allow warzone in nopowerloss worlds to lose power, but it should!
				if (Utilities.noPowerLossWorld(p.getWorld())) {
					return;
				}
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
		
		//2of2
		if(event.getEntity() instanceof Monster) {
			Player k = event.getEntity().getKiller();
			if (k == null) {
				return;
			}
			if ( Config._powerboosts.extraPowerWhenKillMonster._ > 0.0D) {
				if (shouldLosePowerWherePlayerIsAt(k)
						&& (!Utilities.noPowerLossWorld(k.getWorld()))
						) {//this IF is here to save some CPU cycles since likely the 0 value is above
					//allow increase only if player can also lose ie. disallow gain in worldsNoPowerLoss worlds 
					Utilities.addPower( k, Config._powerboosts.extraPowerWhenKillMonster._);
				}
			}
		}
	}

	
	public static final boolean shouldLosePowerWherePlayerIsAt(Player player) {
		if ( !Config._powerboosts.respectFactionsPluginPowerLossRules._ ) {
			return true;
		}
		return canLosePowerWherePlayerIsAt(player);
	}
	
	public static final boolean canLosePowerWherePlayerIsAt( Player player ) {
		// TODO: Block power GAINS in powerloss disabled regions as well: this is gonna be tricky
		Faction factionAtFeet = Board.getFactionAt( new FLocation( player.getLocation() ) );
		// FPlayer fp=FPlayers.i.get(p);
		// assert null != fp;
		return canLosePowerInThisFaction(factionAtFeet, player.getWorld());
	}
	
	public static final boolean shouldLosePowerInThisFaction(Faction faction, World worldName) {//this method is not needed;it's only for debug
		if ( !Config._powerboosts.respectFactionsPluginPowerLossRules._ ) {
			return true;
		}
		return canLosePowerInThisFaction(faction, worldName);
	}
	
	public static final boolean canLosePowerInThisFaction(Faction faction, World worldName) {
		if ( !Bridge.factions.getFlag( faction, FactionsAny.FFlag.POWERLOSS ) ) {//this handles safezone too
			return false;
		} else {
			// safezone check is not needed here because both 1.6 bridge and 1.7 powerloss are false for safezone,
			// except that 1.7 can set it to true if wanted but it's false by default in factions.json
			
			// warzone will always lose power regardless of worldsNoPowerLoss setting, Factions plugin does this too.
			if ( !Utilities.isWarZone( faction ) && null != worldName && Conf.worldsNoPowerLoss.contains( worldName.getName() ) )
			{
				// fplayer.msg("<i>You didn't lose any power due to the world you died in.");
				return false;
			}
		}
		//
		return true;
	}
}
