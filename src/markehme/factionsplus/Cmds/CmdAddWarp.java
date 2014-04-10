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
import markehme.factionsplus.Cmds.req.ReqFactionsPlusEnabled;
import markehme.factionsplus.Cmds.req.ReqWarpsEnabled;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.MCore.UConf;
import markehme.factionsplus.extras.FType;
import markehme.factionsplus.util.FPPerm;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;
import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.util.Txt;

public class CmdAddWarp extends FPCommand {
	public CmdAddWarp() {
		this.aliases.add("createwarp");
		this.aliases.add("addwarp");
		this.aliases.add("setwarp");
		
		// Unique identifier for this command 
		this.fpidentifier = "addwarp";
		
		this.requiredArgs.add("name" );
		this.optionalArgs.put("password", "");
		
		this.addRequirements(ReqFactionsEnabled.get());
		
		// Ensure FactionsPlus is enabled
		this.addRequirements(ReqFactionsPlusEnabled.get());
		
		// Ensure Warps are enabled
		this.addRequirements(ReqWarpsEnabled.get());
		
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
		
		this.addRequirements(ReqHasPerm.get(FPPerm.CREATEWARP.node));
		
		this.setHelp( LConf.get().cmdDescAddWarp );
		this.setDesc( LConf.get().cmdDescAddWarp );

	}

	@Override
	public void performfp() {
				
		String warpName 	= this.arg(0);
		String warpPass		= null;

		if(this.arg(1) != null) {
			warpPass = this.arg(1);
			
			if(warpPass.length() < 1) {
				msg(LConf.get().warpsPasswordTooSmall);
				return;
			}
		}
		
		UPlayer uPlayer = UPlayer.get(sender.getName());
		
		Faction currentFaction = uPlayer.getFaction();
		
		// Ensure that this role can set warps 
		if(!UConf.get(uPlayer).whoCanSetWarps.get(uPlayer.getRole())) {
			sender.sendMessage(Txt.parse(LConf.get().warpsNotHighEnoughRankingToSet));
			return;
		}
		
		// Ensure they can create warps here 
		// TODO: Less-code way to do this? 
		Faction landIN = BoardColls.get().getFactionAt(PS.valueOf(uPlayer.getPlayer().getLocation()));
		Boolean canCreateHere = false;
		
		if(!uPlayer.isInOwnTerritory()) {
			if(landIN.getRelationTo(uPlayer) == Rel.ENEMY && !UConf.get(uPlayer).allowWarpsIn.get("enemy") ) {
				canCreateHere = false;
			}
			
			if(landIN.getRelationTo(uPlayer) == Rel.ALLY && !UConf.get(uPlayer).allowWarpsIn.get("ally") ) {
				canCreateHere = false;
			}
			
			if(landIN.getRelationTo(uPlayer) == Rel.NEUTRAL && !UConf.get(uPlayer).allowWarpsIn.get("neutral") ) {
				canCreateHere = false;
			}
			
			if(landIN.getRelationTo(uPlayer) == Rel.TRUCE && !UConf.get(uPlayer).allowWarpsIn.get("true") ) {
				canCreateHere = false;
			}
			
			if((
				landIN.getRelationTo(uPlayer) == Rel.MEMBER ||
				landIN.getRelationTo(uPlayer) == Rel.LEADER ||
				landIN.getRelationTo(uPlayer) == Rel.OFFICER ||
				landIN.getRelationTo(uPlayer) == Rel.RECRUIT
			) && !UConf.get(uPlayer).allowWarpsIn.get("owned") ) {
				canCreateHere = false;
			}
		}
		
		if(FType.valueOf(landIN) == FType.WILDERNESS && !UConf.get(uPlayer).allowWarpsIn.get("wilderness")) {
			canCreateHere = false;
		}
		
		if(FType.valueOf(landIN) == FType.WARZONE && !UConf.get(uPlayer).allowWarpsIn.get("warzone")) {
			canCreateHere = false;
		}
		
		if(FType.valueOf(landIN) == FType.SAFEZONE && !UConf.get(uPlayer).allowWarpsIn.get("safezone")) {
			canCreateHere = false;
		}
		
		if(!canCreateHere) {
			msg(Txt.parse(LConf.get().warpsNotInCorrectTerritory));
			return;
		}
		
		if(UConf.get(uPlayer).economyCost.get("createwarp") > 0.0d) {
			// TODO: Remake economy related features 	
			
			/*
			if (!Utilities.doFinanceCrap(OldConfig._economy.costToCreateWarp._, "create a warp", usender)) {
				return;
			}
			*/
		}
		
		if(UConf.get(uPlayer).maxWarps != 0) {
			if(Utilities.getCountOfWarps(currentFaction) >= UConf.get(uPlayer).maxWarps) {
				msg(Txt.parse(LConf.get().warpsReachedMax));
				return;
			}
		}
		
		File currentWarpFile = new File(OldConfig.folderWarps, currentFaction.getId());
		
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

					if(warp_data[0].equalsIgnoreCase(warpName)) {
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

			if(warpPass != null) {
				dataAddition = ":" + warpPass;
			} else {
				dataAddition = ":nullvalue";
			}

			filewrite.write(warpName + ":" +
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
		
		String[] argsb = { warpName };
		
		msg(FactionsPlusTemplates.Go("warp_created", argsb));

		String[] argsa = { sender.getName(), warpName };
		
		msg( FactionsPlusTemplates.Go("notify_warp_created", argsa) );
		
	}
}