package markehme.factionsplus.Cmds;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusTemplates;
import markehme.factionsplus.Utilities;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;

public class CmdAddWarp extends FCommand {
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

	public void perform() {
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

		Boolean authallow = false;

		if(FactionsPlus.config.getBoolean("membersCanSetWarps")) {
			authallow = true;
		} else {
			if(Utilities.isOfficer(fplayer) && FactionsPlus.config.getBoolean("officersCanSetWarps")) {
				authallow = true;
			} else if(Utilities.isLeader(fplayer) && FactionsPlus.config.getBoolean("leadersCanSetWarps")) {
				authallow = true;
			}
		}

		if(!authallow) {
			sender.sendMessage(ChatColor.RED + "Sorry, your ranking is not high enough to do that!");
			return;
		}

		if(!fplayer.isInOwnTerritory()) {
			if(FactionsPlus.config.getBoolean("mustBeInOwnTerritoryToCreate")) {
				sender.sendMessage(ChatColor.RED + "You must be in your own territory to create a warp!");
				return;
			}
		}

		if(FactionsPlus.config.getInt("economy_costToCreateWarp") > 0 && !FactionsPlus.config.getBoolean("economy_enable")) {
			if (!payForCommand(FactionsPlus.config.getInt("economy_costToCreateWarp"), "to create this warp", "for creating this warp")) {
				return;
			}
		}

		if(FactionsPlus.config.getInt("maxWarps") != 0) {
			if(Utilities.getCountOfWarps(currentFaction) >= FactionsPlus.config.getInt("maxWarps")) {
				sender.sendMessage(ChatColor.RED + "You have reached the max amount of warps.");
				return;
			}
		}

		File currentWarpFile = new File(FactionsPlus.BASE_FOLDER + "warps" + File.separator + currentFaction.getId());

		if (!currentWarpFile.exists()) {
			try {
				currentWarpFile.createNewFile();
			} catch (Exception e) {
				System.out.println("[FactionPlus] Cannot create file " + currentWarpFile.getName() + " - " + e.getMessage());
				sender.sendMessage(ChatColor.RED + "An internal error occured (04)");
				return;
			}
		} else {
			try {
				FileInputStream fstream = new FileInputStream(currentWarpFile);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;

				while ((strLine = br.readLine()) != null) {
					String[] warp_data =  strLine.split(":");

					if(warp_data[0].equalsIgnoreCase(warpname)) {
						in.close();
						sender.sendMessage(ChatColor.RED + "A warp already exists with that name.");
						return;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
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
			System.out.println("[FactionPlus] Unexpected error " + e.getMessage());
			sender.sendMessage(ChatColor.RED + "An internal error occured (05)");
			return;
		}

		player.sendMessage(ChatColor.GREEN + "Warp " + ChatColor.WHITE + warpname + ChatColor.GREEN + " set for your Faction!");

		String[] args;

		args = new String[3];
		args[1] = sender.getName();
		args[2] = warpname;

		String announcemsg = FactionsPlusTemplates.Go("notify_warp_created", args);
		// notify all the players in the faction 
		for (FPlayer fplayerlisting : currentFaction.getFPlayersWhereOnline(true)){
			fplayerlisting.msg(announcemsg);
		}

	}
}
