package markehme.factionsplus.Cmds;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsBridge.*;
import markehme.factionsplus.extras.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.integration.EssentialsFeatures;
import com.massivecraft.factions.struct.*;
import com.massivecraft.factions.zcore.util.SmokeUtil;

public class CmdWarp extends FCommand {
	public CmdWarp() {
		this.aliases.add("warp");

		this.requiredArgs.add("name");
		this.optionalArgs.put("password", "string");
		this.optionalArgs.put("faction", "string");

		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		this.errorOnToManyArgs = false;

		senderMustBePlayer = true;
		senderMustBeMember = false;

		this.setHelpShort("warps to a specific warp");

	}

	@Override
	public void perform() {
		String warpname = this.argAsString(0);
		String setPassword = null;

		if(this.argAsString(1) != null) {
			setPassword = this.argAsString(1);
		} else {
			setPassword = "nullvalue";
		}

		if(!FactionsPlus.permission.has(sender, "factionsplus.warp")) {
			sender.sendMessage(ChatColor.RED + "No permission!");
			return;
		}


		Player player = (Player)sender;

		FPlayer fplayer = FPlayers.i.get(sender.getName());

		Faction currentFaction = fplayer.getFaction();

		File currentWarpFile = new File(FactionsPlus.folderWarps,  currentFaction.getId());

		World world;

		// Check if player can teleport from enemy territory
		if(!FactionsPlus.config.getBoolean(FactionsPlus.confStr_warpTeleportAllowedFromEnemyTerritory) && fplayer.isInEnemyTerritory() ){
			fplayer.msg("<b>You cannot teleport to your faction warp while in the territory of an enemy faction.");
			return;
		}

		// Check if player can teleport from different world
		/*
		 * Move inside the try catch
		 * 
		 * if(!Conf.homesTeleportAllowedFromDifferentWorld && player.getWorld().getUID() != world){
		 * 		fme.msg("<b>You cannot teleport to your faction home while in a different world.");
		 * 		return;
		 * }
		 */

		// Check for enemies nearby
		// if player is not in a safe zone or their own faction territory, only allow teleport if no enemies are nearby
		Location loc = player.getLocation().clone();
		if
		(
				FactionsPlus.config.getInt(FactionsPlus.confStr_warpTeleportAllowedEnemyDistance) > 0 && ! Board.getFactionAt(new FLocation(loc)).isSafeZone() 
				&& ( ! fplayer.isInOwnTerritory()
						|| ( fplayer.isInOwnTerritory() && ! FactionsPlus.config.getBoolean(FactionsPlus.confStr_warpTeleportIgnoreEnemiesIfInOwnTerritory)))){
			World w = loc.getWorld();
			double x = loc.getX();
			double y = loc.getY();
			double z = loc.getZ();

			for (Player playa : me.getServer().getOnlinePlayers())
			{
				if (playa == null || !playa.isOnline() || playa.isDead() || playa == fme || playa.getWorld() != w)
					continue;

				FPlayer fp = FPlayers.i.get(playa);
				if ( ! FactionsAny.Relation.ENEMY.equals( 
					Bridge.factions.getRelationBetween( fplayer, fp ) 
					)) {
					continue;//if not enemies, continue
				}

				Location l = playa.getLocation();
				double dx = Math.abs(x - l.getX());
				double dy = Math.abs(y - l.getY());
				double dz = Math.abs(z - l.getZ());
				double max = FactionsPlus.config.getInt(FactionsPlus.confStr_warpTeleportAllowedEnemyDistance);

				// box-shaped distance check
				if (dx > max || dy > max || dz > max)
					continue;

				fplayer.msg("<b>You cannot teleport to your faction warp while an enemy is within " + max+ " blocks of you.");
				return;
			}
		}

		if (!currentWarpFile.exists()) {
			sender.sendMessage(ChatColor.RED + "Your faction has no warps!");

			return;
		}
		
		FileInputStream fstream=null;
		DataInputStream in=null;
		BufferedReader br=null;
		try {
			fstream = new FileInputStream(currentWarpFile);
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null) {
				//sender.sendMessage(strLine);

				String[] warp_data =  strLine.split(":");

				if(warp_data[0].equalsIgnoreCase(warpname)) {
					//sender.sendMessage("warp found: " + warp_data[0]);
					double x = Double.parseDouble(warp_data[1]);
					double y = Double.parseDouble(warp_data[2]); // y axis
					double z = Double.parseDouble(warp_data[3]);

					float Y = Float.parseFloat(warp_data[4]); // yaw
					float playa = Float.parseFloat(warp_data[5]);

					world = Bukkit.getServer().getWorld(warp_data[6]);

					if(warp_data.length == 8) {
						if(warp_data[7] != "nullvalue") {
							if(!setPassword.trim().equals(warp_data[7].trim())) {
								sender.sendMessage("Incorrect password, please use /f warp [warp] <password>");
								return;
							}
						}
					}

					if(FactionsPlus.config.getInt(FactionsPlus.confStr_economyCostToWarp) > 0) {
						if (!payForCommand(FactionsPlus.config.getInt(FactionsPlus.confStr_economyCostToWarp), "to teleport to this warp", "for teleporting to your faction home")) {
							return;
						}
					}

					player.sendMessage(ChatColor.RED + "Warped to " + ChatColor.WHITE + warpname);

					Location newTel = new Location(world, x, y, z, Y, playa);

					if (EssentialsFeatures.handleTeleport(player, newTel)) return;

					// Create a smoke effect
					if (FactionsPlus.config.getBoolean(FactionsPlus.confStr_smokeEffectOnWarp)) {
						List<Location> smokeLocations = new ArrayList<Location>();
						smokeLocations.add(player.getLocation());
						smokeLocations.add(player.getLocation().add(0, 1, 0));
						smokeLocations.add(newTel);
						smokeLocations.add(newTel.clone().add(0, 1, 0));
						SmokeUtil.spawnCloudRandom(smokeLocations, 3f);
					}

					player.teleport(new Location(world, x, y, z, Y, playa));

//					in.close();

					return;
				}	
			}

			player.sendMessage("Could not find the warp " + warpname);

//			in.close();

		} catch (Exception e) {
			e.printStackTrace();

			sender.sendMessage(ChatColor.RED + "An internal error occured (02)");
		}finally{
			if (null != br) {
				try {
					br.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
			if (null != in) {
				try {
					in.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
			if (null != fstream) {
				try {
					fstream.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
		}
	}
}
