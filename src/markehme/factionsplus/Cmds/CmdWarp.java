package markehme.factionsplus.Cmds;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import markehme.factionsplus.FactionsPlus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.integration.EssentialsFeatures;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Relation;
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

		File currentWarpFile = new File("plugins" + File.separator + "FactionsPlus" + File.separator + "warps" + File.separator + currentFaction.getId());

		World world;

		// Check if player can teleport from enemy territory
		if(!Conf.homesTeleportAllowedFromEnemyTerritory && fplayer.isInEnemyTerritory() ){
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
				Conf.homesTeleportAllowedEnemyDistance > 0 && ! Board.getFactionAt(new FLocation(loc)).isSafeZone() 
				&& ( ! fplayer.isInOwnTerritory()
						|| ( fplayer.isInOwnTerritory() && ! Conf.homesTeleportIgnoreEnemiesIfInOwnTerritory))){
			World w = loc.getWorld();
			double x = loc.getX();
			double y = loc.getY();
			double z = loc.getZ();

			for (Player p : me.getServer().getOnlinePlayers())
			{
				if (p == null || !p.isOnline() || p.isDead() || p == fme || p.getWorld() != w)
					continue;

				FPlayer fp = FPlayers.i.get(p);
				if (fplayer.getRelationTo(fp) != Relation.ENEMY)
					continue;

				Location l = p.getLocation();
				double dx = Math.abs(x - l.getX());
				double dy = Math.abs(y - l.getY());
				double dz = Math.abs(z - l.getZ());
				double max = Conf.homesTeleportAllowedEnemyDistance;

				// box-shaped distance check
				if (dx > max || dy > max || dz > max)
					continue;

				fplayer.msg("<b>You cannot teleport to your faction warp while an enemy is within " + Conf.homesTeleportAllowedEnemyDistance + " blocks of you.");
				return;
			}
		}

		if (!currentWarpFile.exists()) {
			sender.sendMessage(ChatColor.RED + "Your faction has no warps!");

			return;
		}
		try {
			FileInputStream fstream = new FileInputStream(currentWarpFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
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
					float p = Float.parseFloat(warp_data[5]);

					world = (World) Bukkit.getServer().getWorld(warp_data[6]);

					if(warp_data.length == 8) {
						if(warp_data[7] != "nullvalue") {
							if(!setPassword.trim().equals(warp_data[7].trim())) {
								sender.sendMessage("Incorrect password, please use /f warp [warp] <password>");
								return;
							}
						}
					}

					if(FactionsPlus.config.getInt("economy_costToWarp") > 0) {
						if (!payForCommand(FactionsPlus.config.getInt("economy_costToWarp"), "to teleport to this warp", "for teleporting to your faction home")) {
							return;
						}
					}

					player.sendMessage(ChatColor.RED + "Warped to " + ChatColor.WHITE + warpname);

					Location newTel = new Location(world, x, y, z, Y, p);

					if (EssentialsFeatures.handleTeleport(player, newTel)) return;

					// Create a smoke effect
					if (FactionsPlus.config.getBoolean("smokeEffectOnWarp")) {
						List<Location> smokeLocations = new ArrayList<Location>();
						smokeLocations.add(player.getLocation());
						smokeLocations.add(player.getLocation().add(0, 1, 0));
						smokeLocations.add(newTel);
						smokeLocations.add(newTel.clone().add(0, 1, 0));
						SmokeUtil.spawnCloudRandom(smokeLocations, 3f);
					}

					player.teleport(new Location(world, x, y, z, Y, p));

					in.close();

					return;
				}	
			}

			player.sendMessage("Could not find the warp " + warpname);

			in.close();

		} catch (Exception e) {
			e.printStackTrace();

			sender.sendMessage(ChatColor.RED + "An internal error occured (02)");
		}	    
	}
}
