package markehme.factionsplus.sublisteners;

import markehme.factionsplus.Utilities;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.extras.FType;

import org.bukkit.World;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.massivecraft.factions.FFlag;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.ps.PS;

public class PowerBoostSubListener {
	public EntityDeathEvent entityDeathEvent(EntityDeathEvent event) {
		
		Player pPlayer = null;;
		
		if(event.getEntity() instanceof Player) {
			pPlayer = (Player) event.getEntity();
		} else {
			if(event.getEntity() instanceof Monster) {
				if(event.getEntity().getKiller() instanceof Player) {
					pPlayer = event.getEntity().getKiller();
				}
			}
			
			if(pPlayer == null) return event;
			
		}
		
		FPUConf fpUConf = FPUConf.get(UPlayer.get(pPlayer));
				
		if((event.getEntity() instanceof Player)) {
						
			if(!canLosePowerWherePlayerIsAt(pPlayer)) {
				
				return event;
				
			}
			
			EntityDamageEvent ldc = event.getEntity().getLastDamageCause();
			
			if(null == ldc) {
				if(fpUConf.extraPowerLoss.containsKey("whenDeathByOther")) {
					if(fpUConf.extraPowerLoss.get("whenDeathByOther") > 0) {
						Utilities.removePower( pPlayer, fpUConf.extraPowerLoss.get("whenDeathByOther"));

					}
				}
				
				return event;
				
			}
			
			DamageCause causeOfDeath = ldc.getCause();
			
			if(pPlayer.getKiller() == null ) {
				if(causeOfDeath == DamageCause.ENTITY_ATTACK || causeOfDeath == DamageCause.PROJECTILE || causeOfDeath == DamageCause.ENTITY_EXPLOSION) {
					if(fpUConf.extraPowerLoss.containsKey("whenDeathByMob")) {
						if(fpUConf.extraPowerLoss.get("whenDeathByMob") > 0) {
							Utilities.removePower(pPlayer, fpUConf.extraPowerLoss.get("whenDeathByMob"));
						}
					}
					
					return event;
					
				}
				
				if(causeOfDeath == DamageCause.CONTACT) {
					if(fpUConf.extraPowerLoss.containsKey("whenDeathByCactus")) {
						if(fpUConf.extraPowerLoss.get("whenDeathByCactus") > 0) {
							Utilities.removePower(pPlayer, fpUConf.extraPowerLoss.get("whenDeathByCactus"));
						}
					}
					
					return event;
				}

				if(causeOfDeath == DamageCause.BLOCK_EXPLOSION) {
					if(fpUConf.extraPowerLoss.containsKey("whenDeathByTNT")) {
						if(fpUConf.extraPowerLoss.get("whenDeathByTNT") > 0) {
							Utilities.removePower(pPlayer, fpUConf.extraPowerLoss.get("whenDeathByTNT"));
						}
					}
					
					return event;
				}

				if(causeOfDeath == DamageCause.FIRE || causeOfDeath == DamageCause.FIRE_TICK) {
					if(fpUConf.extraPowerLoss.containsKey("whenDeathByFire")) {
						if(fpUConf.extraPowerLoss.get("whenDeathByFire") > 0) {
							Utilities.removePower(pPlayer, fpUConf.extraPowerLoss.get("whenDeathByFire"));
						}
					}
					
					return event;
				}

				if(causeOfDeath == DamageCause.MAGIC) {
					if(fpUConf.extraPowerLoss.containsKey("whenDeathByPotion")) {
						if(fpUConf.extraPowerLoss.get("whenDeathByPotion") > 0) {
							Utilities.removePower(pPlayer, fpUConf.extraPowerLoss.get("whenDeathByPotion"));
						}
					}
					
					return event;
				}
				
				if(causeOfDeath == DamageCause.SUICIDE) {
					if(fpUConf.extraPowerLoss.containsKey("whenDeathBySuicide")) {
						if(fpUConf.extraPowerLoss.get("whenDeathBySuicide") > 0) {
							Utilities.removePower(pPlayer, fpUConf.extraPowerLoss.get("whenDeathBySuicide"));
						}
					}

					return event;
				}
				
				if(fpUConf.extraPowerLoss.containsKey("whenDeathByOther")) {
					if(fpUConf.extraPowerLoss.get("whenDeathByOther") > 0) {
						Utilities.removePower(pPlayer, fpUConf.extraPowerLoss.get("whenDeathByOther"));
					}
				}
			} else {
				if ((causeOfDeath == DamageCause.ENTITY_ATTACK) || (causeOfDeath == DamageCause.PROJECTILE)) {
					if(fpUConf.extraPowerLoss.containsKey("whenDeathByPVP")) {
						if(fpUConf.extraPowerLoss.get("whenDeathByPVP") > 0) {
							Utilities.removePower(pPlayer, fpUConf.extraPowerLoss.get("whenDeathByPVP"));
						}
					}
					Player k = pPlayer.getKiller();
					
					UPlayer uP = UPlayer.get(pPlayer);
					UPlayer uK = UPlayer.get(k);
					
					if(fpUConf.extraPowerBoosts.containsKey("whenKillEnemyPlayer") && uP.getRelationTo(uK).equals(Rel.ENEMY)) {
						if(fpUConf.extraPowerBoosts.get("whenKillEnemyPlayer") > 0) {
							Utilities.addPower(k, fpUConf.extraPowerBoosts.get("whenKillEnemyPlayer"));
						}
					}
					if(fpUConf.extraPowerBoosts.containsKey("whenKillAllyPlayer") && uP.getRelationTo(uK).equals(Rel.ALLY)) {
						if(fpUConf.extraPowerBoosts.get("whenKillAllyPlayer") > 0) {
							Utilities.addPower(k, fpUConf.extraPowerBoosts.get("whenKillAllyPlayer"));
						}
					}
					
					if(fpUConf.extraPowerBoosts.containsKey("whenKillTrucePlayer") && uP.getRelationTo(uK).equals(Rel.TRUCE)) {
						if(fpUConf.extraPowerBoosts.get("whenKillTrucePlayer") > 0) {
							Utilities.addPower(k, fpUConf.extraPowerBoosts.get("whenKillTrucePlayer"));
						}
					}
					
					if(fpUConf.extraPowerBoosts.containsKey("whenKillNeutralPlayer") && uP.getRelationTo(uK).equals(Rel.NEUTRAL)) {
						if(fpUConf.extraPowerBoosts.get("whenKillNeutralPlayer") > 0) {
							Utilities.addPower(k, fpUConf.extraPowerBoosts.get("whenKillNeutralPlayer"));
						}
					}

					return event;
				}
			}
		}
		
		//2of2
		if(event.getEntity() instanceof Monster) {
			if(event.getEntity().getKiller() instanceof Player) {
				if(fpUConf.extraPowerBoosts.containsKey("whenKillAnotherMonster")) {
					if(fpUConf.extraPowerBoosts.get("whenKillAnotherMonster") > 0 && canLosePowerWherePlayerIsAt(pPlayer)) {
						Utilities.addPower(pPlayer, fpUConf.extraPowerBoosts.get("whenKillAnotherMonster"));
					}
				}
			}
		}
		
		return event;
	}
	
	public static final boolean canLosePowerWherePlayerIsAt(Player player) {
		Faction factionAtFeet = BoardColls.get().getFactionAt(PS.valueOf(player.getLocation()));
		return canLosePowerInThisFaction(factionAtFeet, player.getWorld());
	}
	
	public static final boolean canLosePowerInThisFaction(Faction faction, World worldName) {
		if (!faction.getFlag(FFlag.POWERLOSS)) { //this handles safezone too
			return false;
		} else {
			if (!FType.valueOf(faction).equals(FType.WARZONE) && null != worldName && MConf.get().worldsNoPowerLoss.contains(worldName.getName())) {
				return false;
			}
		}
		
		return true;
	}
}
