package markehme.factionsplus.listeners;

import markehme.factionsplus.Utilities;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.extras.FType;

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
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.ps.PS;

public class PowerboostListener implements Listener{
		
	@EventHandler(priority=EventPriority.MONITOR)
	public void onEntityDeath( EntityDeathEvent event ) {
		FPUConf fpUConf = FPUConf.get(UPlayer.get((Player) event.getEntity()));
		
		if(!fpUConf.enabled) return;
		
		if((event.getEntity() instanceof Player ) ) {
			
			Player p = (Player)event.getEntity();
			
			if( ! canLosePowerWherePlayerIsAt( p ) ) {
				
				return;
				
			}
			
			EntityDamageEvent ldc = event.getEntity().getLastDamageCause();
			
			if( null == ldc ) {
				if(fpUConf.extraPowerLoss.containsKey("whenDeathByOther")) {
					if(fpUConf.extraPowerLoss.get("whenDeathByOther") > 0) {
						Utilities.removePower( p, fpUConf.extraPowerLoss.get("whenDathByOther"));

					}
				}
				
				return;
				
			}
			
			DamageCause causeOfDeath = ldc.getCause();
			
			if( p.getKiller() == null ) {
				
				if((causeOfDeath == DamageCause.ENTITY_ATTACK || causeOfDeath == DamageCause.PROJECTILE || causeOfDeath == DamageCause.ENTITY_EXPLOSION)) {
					if(fpUConf.extraPowerLoss.containsKey("whenDeathByMob")) {
						if(fpUConf.extraPowerLoss.get("whenDeathByMob") > 0) {
							Utilities.removePower(p, fpUConf.extraPowerLoss.get("whenDeathByMob"));
						}
					}
					
					return;
					
				}
				
				if((causeOfDeath == DamageCause.CONTACT)) {
					if(fpUConf.extraPowerLoss.containsKey("whenDeathByCactus")) {
						if(fpUConf.extraPowerLoss.get("whenDeathByCactus") > 0) {
							Utilities.removePower(p, fpUConf.extraPowerLoss.get("whenDeathByCactus"));
						}
					}
					
					return;
				}

				if((causeOfDeath == DamageCause.BLOCK_EXPLOSION)) {
					if(fpUConf.extraPowerLoss.containsKey("whenDeathByTNT")) {
						if(fpUConf.extraPowerLoss.get("whenDeathByTNT") > 0) {
							Utilities.removePower(p, fpUConf.extraPowerLoss.get("whenDeathByTNT"));
						}
					}
					
					return;
				}

				if(causeOfDeath == DamageCause.FIRE || causeOfDeath == DamageCause.FIRE_TICK) {
					if(fpUConf.extraPowerLoss.containsKey("whenDeathByFire")) {
						if(fpUConf.extraPowerLoss.get("whenDeathByFire") > 0) {
							Utilities.removePower(p, fpUConf.extraPowerLoss.get("whenDeathByFire"));
						}
					}
					
					return;
				}

				if(causeOfDeath == DamageCause.MAGIC) {
					if(fpUConf.extraPowerLoss.containsKey("whenDeathByPotion")) {
						if(fpUConf.extraPowerLoss.get("whenDeathByPotion") > 0) {
							Utilities.removePower(p, fpUConf.extraPowerLoss.get("whenDeathByPotion"));
						}
					}
					
					return;
				}
				
				if (causeOfDeath == DamageCause.SUICIDE){
					if(fpUConf.extraPowerLoss.containsKey("whenDeathBySuicide")) {
						if(fpUConf.extraPowerLoss.get("whenDeathBySuicide") > 0) {
							Utilities.removePower(p, fpUConf.extraPowerLoss.get("whenDeathBySuicide"));
						}
					}

					return;
				}
				
				if(fpUConf.extraPowerLoss.containsKey("whenDeathByOther")) {
					if(fpUConf.extraPowerLoss.get("whenDeathByOther") > 0) {
						Utilities.removePower(p, fpUConf.extraPowerLoss.get("whenDeathByOther"));
					}
				}
			} else {
				if ( ( causeOfDeath == DamageCause.ENTITY_ATTACK ) || ( causeOfDeath == DamageCause.PROJECTILE ) ) {
					if(fpUConf.extraPowerLoss.containsKey("whenDeathByPVP")) {
						if(fpUConf.extraPowerLoss.get("whenDeathByPVP") > 0) {
							Utilities.removePower(p, fpUConf.extraPowerLoss.get("whenDeathByPVP"));
						}
					}
					Player k = p.getKiller();
					
					UPlayer uP = UPlayer.get(p);
					UPlayer uK = UPlayer.get(k);
					
					if(fpUConf.extraPowerBoosts.containsKey("whenKillEnemyPlayer") && uP.getRelationTo(uK).equals(Rel.ENEMY)) {
						if(fpUConf.extraPowerLoss.get("whenKillEnemyPlayer") > 0) {
							Utilities.addPower(k, fpUConf.extraPowerLoss.get("whenKillEnemyPlayer"));
						}
					}
					if(fpUConf.extraPowerBoosts.containsKey("whenKillAllyPlayer") && uP.getRelationTo(uK).equals(Rel.ALLY)) {
						if(fpUConf.extraPowerLoss.get("whenKillAllyPlayer") > 0) {
							Utilities.addPower(k, fpUConf.extraPowerLoss.get("whenKillAllyPlayer"));
						}
					}
					if(fpUConf.extraPowerBoosts.containsKey("whenKillTrucePlayer") && uP.getRelationTo(uK).equals(Rel.TRUCE)) {
						if(fpUConf.extraPowerLoss.get("whenKillTrucePlayer") > 0) {
							Utilities.addPower(k, fpUConf.extraPowerLoss.get("whenKillTrucePlayer"));
						}
					}
					
					if(fpUConf.extraPowerBoosts.containsKey("whenKillNeutralPlayer") && uP.getRelationTo(uK).equals(Rel.NEUTRAL)) {
						if(fpUConf.extraPowerLoss.get("whenKillNeutralPlayer") > 0) {
							Utilities.addPower(k, fpUConf.extraPowerLoss.get("whenKillNeutralPlayer"));
						}
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
						
			if(fpUConf.extraPowerBoosts.containsKey("whenKillAnotherMonster")) {
				if(fpUConf.extraPowerLoss.get("whenKillAnotherMonster") > 0 && canLosePowerWherePlayerIsAt(k)) {
					Utilities.addPower(k, fpUConf.extraPowerLoss.get("whenKillAnotherMonster"));
				}
			}
		}
	}

	
	public static final boolean canLosePowerWherePlayerIsAt( Player player ) {
		Faction factionAtFeet = BoardColls.get().getFactionAt( PS.valueOf( player.getLocation() ) );
		return canLosePowerInThisFaction(factionAtFeet, player.getWorld());
	}
	
	public static final boolean canLosePowerInThisFaction(Faction faction, World worldName) {
		if (!faction.getFlag(FFlag.POWERLOSS)) { //this handles safezone too
			return false;
		} else {
			// safezone check is not needed here because both 1.6 bridge and 1.7 powerloss are false for safezone,
			// except that 1.7 can set it to true if wanted but it's false by default in factions.json
			
			// warzone will always lose power regardless of worldsNoPowerLoss setting, Factions plugin does this too.
			if (!FType.valueOf(faction).equals(FType.WARZONE) && null != worldName && MConf.get().worldsNoPowerLoss.contains( worldName.getName() ) ) {
				return false;
			}
		}
		//
		return true;
	}


}
