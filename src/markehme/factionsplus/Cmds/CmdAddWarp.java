package markehme.factionsplus.Cmds;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusTemplates;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Permission;

public class CmdAddWarp extends FPCommand {
	public CmdAddWarp() {
		this.aliases.add("createwarp");
		this.aliases.add("addwarp");
		this.aliases.add("setwarp");

		this.requiredArgs.add("name");
		//this.optionalArgs.put("on/off", "flip");

		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		this.errorOnToManyArgs = false;

		senderMustBePlayer = true;
		senderMustBeMember = false;

		this.setHelpShort("creates a faction warp, can be specified with a password");

	}

	@Override
	public void performfp() {
		String warpname = this.argAsString(0);

		String pass = null;

		if(this.argAsString(1) != null) {
			pass = this.argAsString(1);
		}

		if(!FactionsPlus.permission.has(sender, "factionsplus.createwarp")) {
			sender.sendMessage(ChatColor.RED + "No permission!");
			return;
		}

		FPlayer fplayer = FPlayers.i.get(sender.getName());
		Faction currentFaction = myFaction;


		if(!Config._warps.canSetOrRemoveWarps(fplayer)) {
			sender.sendMessage(ChatColor.RED + "Sorry, your ranking is not high enough to create warps!");
			return;
		}

		if(!fplayer.isInOwnTerritory()) {
			if(Config._warps.mustBeInOwnTerritoryToCreate._) {
				sender.sendMessage(ChatColor.RED + "You must be in your own territory to create a warp!");
				return;
			}
		}

		if(Config._economy.costToCreateWarp._ > 0.0d && !Config._economy.isHooked()) {
			if (!payForCommand(Config._economy.costToCreateWarp._, "to create this warp", "for creating this warp")) {
				return;
			}
		}

		if(Config._warps.maxWarps._ != 0) {
			if(Utilities.getCountOfWarps(currentFaction) >= Config._warps.maxWarps._) {
				sender.sendMessage(ChatColor.RED + "You have reached the max amount of warps.");
				return;
			}
		}

		File currentWarpFile = new File(Config.folderWarps, currentFaction.getId());

		if (!currentWarpFile.exists()) {
			try {
				currentWarpFile.createNewFile();
			} catch (Exception e) {
				FactionsPlus.warn("Cannot create file " + currentWarpFile.getName() + " - " + e.getMessage());
				sender.sendMessage(ChatColor.RED + "An internal error occured (04)");
				return;
			}
		} else {
			DataInputStream in =null;
			BufferedReader br=null;
			FileInputStream fstream=null;
			try {
				fstream = new FileInputStream(currentWarpFile);
				in = new DataInputStream(fstream);
				br = new BufferedReader(new InputStreamReader(in));
				String strLine;

				while ((strLine = br.readLine()) != null) {
					String[] warp_data =  strLine.split(":");

					if(warp_data[0].equalsIgnoreCase(warpname)) {
						sender.sendMessage(ChatColor.RED + "A warp already exists with that name.");
						return;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
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
			/*
			currentWarpFile.delete();
			try {
				currentWarpFile.createNewFile();
			} catch (Exception e) {
		        System.out.println("[FactionPlus] Cannot create file " + currentWarpFile.getName() + " - " + e.getMessage());
				e.printStackTrace();
			}
			 */
		}

		Player player = (Player)sender;

		Location loc = player.getLocation();
		try {
			FileWriter filewrite = new FileWriter(currentWarpFile, true);
			String dataAddition;

			if(pass != null) {
				dataAddition = ":" + pass;
			} else {
				dataAddition = ":nullvalue";
			}

			filewrite.write(warpname + ":" +
					loc.getX() + ":" +
					loc.getY() + ":" +
					loc.getZ() + ":" +
					loc.getYaw() + ":" +
					loc.getPitch() + ":" +
					player.getWorld().getName() + dataAddition + "\n");

			filewrite.close();
		} catch (Exception e) {
			FactionsPlus.warn("Unexpected error:");// + e.getMessage());
			e.printStackTrace();
			sender.sendMessage(ChatColor.RED + "An internal error occured (05)");
			return;
		}

		player.sendMessage(ChatColor.GREEN + "Warp " + ChatColor.WHITE + warpname + ChatColor.GREEN + " set for your Faction!");

		String[] argsa;

		argsa = new String[3];
		argsa[1] = sender.getName();
		argsa[2] = warpname;

		String announcemsg = FactionsPlusTemplates.Go("notify_warp_created", argsa);
		// notify all the players in the faction
//		currentFaction.sendMessage( announcemsg ); //this would work too, same thing
		for (FPlayer fplayerlisting : currentFaction.getFPlayersWhereOnline(true)){
			fplayerlisting.msg(announcemsg);
		}

	}
}