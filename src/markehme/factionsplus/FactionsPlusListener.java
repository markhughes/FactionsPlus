package markehme.factionsplus;

import java.io.*;

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
import com.massivecraft.factions.event.FactionDisbandEvent;

public class FactionsPlusListener implements Listener {
	Factions factions;
	FPlayers fplayers;
	Server server;

	@EventHandler
	public void onFactionDisband(FactionDisbandEvent event){
		// Clean up old files used by faction
		// Announcements, bans, rules, jails, warps
		Faction faction = event.getFaction();
		
		// Annoucements
		File tempFile = new File(FactionsPlus.folderAnnouncements, faction.getId());
		if(tempFile.exists()){
			tempFile.delete();
		}
		tempFile = null;
		
		// Bans
		File tempDir =FactionsPlus.folderFBans;
		if(tempDir.isDirectory()){
			for(File file : tempDir.listFiles()){
				if(file.getName().startsWith(faction.getId() + ".")){
					file.delete();
				}
			}
		}
		tempDir = null;
		
		// Rules
		tempFile = new File(FactionsPlus.folderFRules, faction.getId());
		if(tempFile.exists()){
			tempFile.delete();
		}
		tempFile = null;
		
		// Jailed Players and Jail locations
		tempDir =FactionsPlus.folderJails;
		if(tempDir.isDirectory()){
			for(File file : tempDir.listFiles()){
				if(file.getName().startsWith("jaildata." + faction.getId() + ".")){
					file.delete();
				} else if (file.getName().equals("loc." + faction.getId())){
					file.delete();
				}
			}
		}
		
		// Warps
		tempFile = new File(FactionsPlus.folderWarps,  faction.getId());
		if(tempFile.exists()){
			tempFile.delete();
		}
		tempFile = null;
	}
	
	@EventHandler
	public void onFPlayerJoinEvent(FPlayerJoinEvent event) {
		if(event.isCancelled()) {
			return;
		}

		File banFile = new File(FactionsPlus.folderFBans, event.getFaction().getId() + "." + event.getFPlayer().getName().toLowerCase());

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
		
		// If player is still jailed, SEND THEM TO THE BRIG!
		if(Utilities.isJailed(event.getFPlayer().getPlayer())){
			event.getFPlayer().getPlayer().teleport(FactionsPlusJail.getJailLocation(event.getFPlayer().getPlayer()));
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
				
				if ((causeOfDeath.equals("SUICIDE"))
						&& ( FactionsPlus.config.getDouble( "extraPowerLossIfDeathBySuicide" ) > 0.0D ) )
				{
					Utilities.removePower( p, FactionsPlus.config.getDouble( "extraPowerLossIfDeathBySuicide" ) );
					return;
				}
				
				if(FactionsPlus.config.getDouble("extraPowerLossIfDeathByOther") > 0) {
					Utilities.removePower(p, FactionsPlus.config.getDouble("extraPowerLossIfDeathByOther"));
					return;
				}
			} else {//non-null killer
//				if ( p.getKiller() instanceof Player ) {// The expression of type Player is already an instance of type Player
					if ( ( causeOfDeath == "ENTITY_ATTACK" ) || ( causeOfDeath == "PROJECTILE" ) ) {
						if ( FactionsPlus.config.getDouble( "extraPowerLossIfDeathByPVP" ) > 0.0D ) {
							Utilities.removePower( p, FactionsPlus.config.getDouble( "extraPowerLossIfDeathByPVP" ) );
						}
						
						if ( FactionsPlus.config.getDouble( "extraPowerWhenKillPlayer" ) > 0.0D ) {
							Player k = p.getKiller();
							Utilities.addPower( k, FactionsPlus.config.getDouble( "extraPowerWhenKillPlayer" ) );
						}
						return;
					}
//				}
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
			File fAF = new File(FactionsPlus.folderAnnouncements, me.getFactionId());
			if(fAF.exists()) {
				try {
					event.getPlayer().sendMessage(Utilities.readFileAsString(fAF));
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
					File fAF=new File(FactionsPlus.folderAnnouncements, me.getFactionId());
					if(fAF.exists()) {
						try {
							event.getPlayer().sendMessage(Utilities.readFileAsString(fAF));
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
				BufferedReader buff=null;
				try {
					buff = new BufferedReader(new FileReader(FactionsPlus.fileDisableInWarzone));

					while ((filterRow = buff.readLine()) != null) {
						if ((event.getMessage().equalsIgnoreCase(filterRow)) || (event.getMessage().toLowerCase().startsWith(filterRow + " "))) {
							event.setCancelled(true);
							player.sendMessage("You can't use that command in a WarZone!");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if (null != buff) {
						try {
							buff.close();
						} catch ( IOException e ) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
