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

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;

public class CmdAddWarp extends FPCommand {
	public CmdAddWarp() {
		this.aliases.add( "createwarp" );
		this.aliases.add( "addwarp" );
		this.aliases.add( "setwarp" );
		
		this.requiredArgs.add( "name" );
		this.optionalArgs.put( "password", "string" );
		
		this.errorOnToManyArgs = false;
		
		this.addRequirements( ReqFactionsEnabled.get() );
		this.addRequirements( ReqIsPlayer.get() );
		this.addRequirements( ReqHasFaction.get() );
		
		this.setHelp( "create a faction warp, can be specified with a password" );
		this.setDesc( "create a faction warp, can be specified with a password" );

	}

	@Override
	public void performfp() {
		String warpname = this.arg(0);

		String warpPassword = null;

		if(this.arg(1) != null) {
			warpPassword = this.arg(1);
			
			if(warpPassword.length() < 1) {
				msg("Your warp password must be at least 2 characters or more.");
				
				return;
			}
		}

		if(!FactionsPlus.permission.has(sender, "factionsplus.createwarp")) {
			sender.sendMessage(ChatColor.RED + "No permission!");
			return;
		}

		UPlayer uPlayer = UPlayer.get(sender.getName());
		Faction currentFaction = uPlayer.getFaction();


		if(!Config._warps.canSetOrRemoveWarps(uPlayer)) {
			sender.sendMessage(FactionsPlusTemplates.Go("create_warp_denied_badrank", null));
			return;
		}

		if(!uPlayer.isInOwnTerritory()) {
			if(Config._warps.mustBeInOwnTerritoryToCreate._) {
				sender.sendMessage(FactionsPlusTemplates.Go("create_warp_denied_badterritory", null));
				return;
			}
		}
		if(Config._economy.costToCreateWarp._ > 0.0d && Config._economy.isHooked()) {
			if (!Utilities.doFinanceCrap(Config._economy.costToCreateWarp._, "create a warp", usender)) {
				return;
			}
		}
		
		if(Config._warps.maxWarps._ != 0) {
			if(Utilities.getCountOfWarps(currentFaction) >= Config._warps.maxWarps._) {
				usender.msg( FactionsPlusTemplates.Go( "warps_reached_max", null ) );
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
			DataInputStream in 			= null;
			BufferedReader br			= null;
			FileInputStream fstream		= null;
			try {
				fstream 					= new FileInputStream(currentWarpFile);
				in							= new DataInputStream(fstream);
				br 							= new BufferedReader(new InputStreamReader(in));
				String strLine;

				while ((strLine = br.readLine()) != null) {
					String[] warp_data =  strLine.split(":");

					if(warp_data[0].equalsIgnoreCase(warpname)) {
						sender.sendMessage(FactionsPlusTemplates.Go("warps_already_exists", null));
						return;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
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

		Player player = (Player) sender;

		Location loc = player.getLocation();
		try {
			FileWriter filewrite = new FileWriter(currentWarpFile, true);
			String dataAddition;

			if(warpPassword != null) {
				dataAddition = ":" + warpPassword;
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
			FactionsPlus.warn("Unexpected error:");
			e.printStackTrace();
			sender.sendMessage(ChatColor.RED + "An internal error occured (05)");
			return;
		}
		
		String[] argsb = { warpname };
		
		msg(FactionsPlusTemplates.Go("warp_created", argsb));

		String[] argsa = { sender.getName(), warpname };
		
		msg( FactionsPlusTemplates.Go("notify_warp_created", argsa) );
		
	}
}