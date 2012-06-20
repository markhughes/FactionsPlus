package markehme.factionsplus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import me.desmin88.mobdisguise.api.MobDisguiseAPI;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import pgDev.bukkit.DisguiseCraft.DisguiseCraft;
import pgDev.bukkit.DisguiseCraft.api.DisguiseCraftAPI;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.event.FPlayerJoinEvent;
import com.massivecraft.factions.event.FPlayerLeaveEvent;

public class FactionsPlusListener implements Listener {
	Factions factions;
	FPlayers fplayers;
	Faction faction;
	Server server;

	@EventHandler
	public void onFPlayerJoinEvent(FPlayerJoinEvent event) {
		if(event.isCancelled()) {
			return;
		}

		File banFile = new File("plugins" + File.separator + "FactionsPlus" + File.separator + "fbans" + File.separator + event.getFaction().getId() + "." + event.getFPlayer().getName().toLowerCase());

		if(banFile.exists()) {
			event.getFPlayer().msg("You can't join this Faction as you have been banned!");
			event.getFPlayer().leave(true);
			event.setCancelled(true);
			return;
		}

		if(FactionsPlus.config.getInt("powerBoostIfPeaceful") > 0) {
			if(event.getFaction().isPeaceful()) { // TODO: Prepare for 1.7.x and the removal of isPeaceful()
				Utilities.addPower(event.getFPlayer(), FactionsPlus.config.getInt("powerBoostIfPeaceful"));
			}
		}
	}

	@EventHandler
	public void onFPlayerLeaveEvent(FPlayerLeaveEvent event) {
		if(event.isCancelled()) {
			return;
		}

		if(FactionsPlus.config.getInt("powerBoostIfPeaceful") > 0) {
			if(event.getFaction().isPeaceful()) { // TODO: Prepare for 1.7.x and the removal of isPeaceful()
				Utilities.removePower(event.getFPlayer(), FactionsPlus.config.getInt("powerBoostIfPeaceful"));
			}
			return;
		}
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event)
	{
		if ((event.getEntity() instanceof Player)) {
			Player p = (Player)event.getEntity();

			String causeOfDeath = event.getEntity().getLastDamageCause().getCause().toString();
			if (p.getKiller() instanceof Player) {
				if ((p.getKiller().getName() == p.getName()) && 
						(FactionsPlus.config.getDouble("extraPowerLossIfDeathBySuicide") > 0.0D)) {
					Utilities.removePower(p, FactionsPlus.config.getDouble("extraPowerLossIfDeathBySuicide"));
					return;
				}
				if ((causeOfDeath == "ENTITY_ATTACK") || (causeOfDeath == "PROJECTILE")) {
					if (FactionsPlus.config.getDouble("extraPowerLossIfDeathByPVP") > 0.0D) {
						Utilities.removePower(p, FactionsPlus.config.getDouble("extraPowerLossIfDeathByPVP"));
					}

					if (FactionsPlus.config.getDouble("extraPowerLossIfDeathByPVP") > 0.0D) {
						Utilities.removePower(p, FactionsPlus.config.getDouble("extraPowerLossIfDeathByPVP"));
					}

					if (FactionsPlus.config.getDouble("extraPowerWhenKillPlayer") > 0.0D) {
						Player k = p.getKiller();
						Utilities.addPower(k, FactionsPlus.config.getDouble("extraPowerWhenKillPlayer"));
					}
					return;
				}
			}
			if (p.getKiller() == null) {
				if ((causeOfDeath == "ENTITY_ATTACK"||causeOfDeath == "PROJECTILE"||causeOfDeath == "ENTITY_EXPLOSION") &&
						(FactionsPlus.config.getDouble("extraPowerLossIfDeathByMob") > 0.0D)) {
					Utilities.removePower(p, FactionsPlus.config.getDouble("extraPowerLossIfDeathByMob"));
					return;
				}
				if ((causeOfDeath == "CONTACT") && 
						(FactionsPlus.config.getDouble("extraPowerLossIfDeathByCactus") > 0.0D)) {
					Utilities.removePower(p, FactionsPlus.config.getDouble("extraPowerLossIfDeathByCactus"));
					return;
				}

				if ((causeOfDeath == "BLOCK_EXPLOSION") && 
						(FactionsPlus.config.getDouble("extraPowerLossIfDeathByTNT") > 0.0D)) {
					Utilities.removePower(p, FactionsPlus.config.getDouble("extraPowerLossIfDeathByTNT"));
					return;
				}

				if (((causeOfDeath == "FIRE") || (causeOfDeath == "FIRE_TICK")) && 
						(FactionsPlus.config.getDouble("extraPowerLossIfDeathByFire") > 0.0D)) {
					Utilities.removePower(p, FactionsPlus.config.getDouble("extraPowerLossIfDeathByFire"));
					return;
				}

				if ((causeOfDeath == "MAGIC") && 
						(FactionsPlus.config.getDouble("extraPowerLossIfDeathByPotion") > 0.0D)) {
					Utilities.removePower(p, FactionsPlus.config.getDouble("extraPowerLossIfDeathByPotion"));
					return;
				}
				if(FactionsPlus.config.getDouble("extraPowerLossIfDeathByOther") > 0) {
					Utilities.removePower(p, FactionsPlus.config.getDouble("extraPowerLossIfDeathByOther"));
					return;
				}
			}
		}
	}



	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();

		if (Utilities.isJailed(player)) {
			event.setRespawnLocation(FactionsPlusJail.getJailLocation(player));
			player.sendMessage("You're currently in Jail!");
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		FPlayer me = FPlayers.i.get(player);

		if(FactionsPlus.config.getBoolean("showLastAnnounceOnLogin")) {
			if(new File("plugins" + File.separator + "FactionsPlus" + File.separator + "announcements" + File.separator + me.getFactionId()).exists()) {
				try {
					event.getPlayer().sendMessage(Utilities.readFileAsString("plugins" + File.separator + "FactionsPlus" + File.separator + "announcements" + File.separator + me.getFactionId()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		if (Utilities.isJailed(player)) {
			player.teleport(FactionsPlusJail.getJailLocation(player));
			//event.setRespawnLocation(FactionsPlusJail.getJailLocation(player));
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if(event.isCancelled()) {
			return;
		}

		Player player = event.getPlayer();

		if(Utilities.isJailed(player)) {
			event.setCancelled(true);
			player.sendMessage("You're currently in Jail! You can't do that!");
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if(event.isCancelled()) {
			return;
		}

		Player player = event.getPlayer();

		if(Utilities.isJailed(player)) {
			event.setCancelled(true);
			player.sendMessage("You're currently in Jail! You can't do that!");
		}

	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(PlayerChatEvent event) {
		if(event.isCancelled()) {
			return;
		}

		Player player = event.getPlayer();

		if(Utilities.isJailed(player)) {
			event.setCancelled(true);
			player.sendMessage("You're currently in Jail! You can't do that!");
		}

	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if(event.isCancelled()) {
			return;
		}

		if(Utilities.isJailed(event.getPlayer())) {
			//event.getPlayer().teleport(FactionsPlusJail.getJailLocation(event.getPlayer()));
			event.setCancelled(true);
			return;
		}


		if(FactionsPlus.isMobDisguiseEnabled) {
			FPlayer fplayer = FPlayers.i.get(event.getPlayer());

			if(MobDisguiseAPI.isDisguised(event.getPlayer())) {
				if(FactionsPlus.config.getBoolean("unDisguiseIfInEnemyTerritory")) {
					if(fplayer.isInEnemyTerritory()) {
						MobDisguiseAPI.undisguisePlayer(event.getPlayer());
						event.getPlayer().sendMessage("You have been un-disguised!");
					}
				}

				if(FactionsPlus.config.getBoolean("unDisguiseIfInOwnTerritory")) {
					if(fplayer.isInOwnTerritory()) {
						MobDisguiseAPI.undisguisePlayer(event.getPlayer());
						event.getPlayer().sendMessage("You have been un-disguised!");
					}
				}
			}
		}

		if(FactionsPlus.isDisguiseCraftEnabled) {
			DisguiseCraftAPI dcAPI = DisguiseCraft.getAPI();
			FPlayer fplayer = FPlayers.i.get(event.getPlayer());

			if(dcAPI.isDisguised(event.getPlayer())) {
				if(FactionsPlus.config.getBoolean("unDisguiseIfInEnemyTerritory")) {
					if(fplayer.isInEnemyTerritory()) {
						dcAPI.undisguisePlayer(event.getPlayer());
						event.getPlayer().sendMessage("You have been un-disguised!");
					}
				}

				if(FactionsPlus.config.getBoolean("unDisguiseIfInOwnTerritory")) {
					if(fplayer.isInOwnTerritory()) {
						dcAPI.undisguisePlayer(event.getPlayer());
						event.getPlayer().sendMessage("You have been un-disguised!");
					}
				}
			}
		}

		if(FactionsPlus.config.getBoolean("showLastAnnounceOnLandEnter")) {
			if (event.getFrom().equals(event.getTo())) return;

			Player player = event.getPlayer();
			FPlayer me = FPlayers.i.get(player);
			Faction factionHere = Board.getFactionAt(new FLocation(event.getTo()));

			if (Board.getFactionAt(new FLocation(event.getFrom())) != Board.getFactionAt(new FLocation(event.getTo()))) {
				if(factionHere.getId().equals(me.getFactionId())) {
					if(new File("plugins" + File.separator + "FactionsPlus" + File.separator + "announcements" + File.separator + me.getFactionId()).exists()) {
						try {
							event.getPlayer().sendMessage(Utilities.readFileAsString("plugins" + File.separator + "FactionsPlus" + File.separator + "announcements" + File.separator + me.getFactionId()));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if(event.isCancelled()) {
			return;
		}

		Player player = event.getPlayer();
		String filterRow = null;

		//FPlayer fplayer = FPlayers.i.get(player.getName());

		if ((event.getMessage().equalsIgnoreCase("/f reload")) || (event.getMessage().toLowerCase().startsWith("/f reload"))) {
			event.getPlayer().sendMessage("Yo yo, lets reload FactionsPlus? ;)");
		}
		Faction factionHere = Board.getFactionAt(new FLocation(player.getLocation()));

		if(factionHere.getTag().trim().equalsIgnoreCase("WarZone")) {

			if (!player.isOp()) {
				try {
					BufferedReader buff = new BufferedReader(new FileReader("plugins" + File.separator + "FactionsPlus" + File.separator + "disabled_in_warzone.txt"));

					while ((filterRow = buff.readLine()) != null) {
						if ((event.getMessage().equalsIgnoreCase(filterRow)) || (event.getMessage().toLowerCase().startsWith(filterRow + " "))) {
							event.setCancelled(true);
							player.sendMessage("You can't use that command in a WarZone!");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
