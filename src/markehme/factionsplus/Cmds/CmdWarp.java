package markehme.factionsplus.Cmds;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import markehme.factionsplus.EssentialsIntegration;
import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusPlugin;
import markehme.factionsplus.FactionsPlusTemplates;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.FactionsBridge.Bridge;
import markehme.factionsplus.FactionsBridge.FactionsAny;
import markehme.factionsplus.FactionsBridge.FactionsAny.Relation;
import markehme.factionsplus.config.Config;

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
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.integration.EssentialsFeatures;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.SmokeUtil;

public class CmdWarp extends FPCommand {
	public CmdWarp() {
		this.aliases.add("warp");

		this.requiredArgs.add("name");
		this.optionalArgs.put("password", "string");
		this.optionalArgs.put("faction", "string");

		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		this.errorOnToManyArgs = false;

		senderMustBePlayer = true;
		senderMustBeMember = true;

		this.setHelpShort("warps to a specific warp");

	}

	@Override
	public void performfp() {
		String warpname = this.argAsString(0);
		String setPassword = null;

		if(this.argAsString(1) != null) {
			if(this.argAsString(1) != "-") {
				setPassword = this.argAsString(1);
			}
		} else {
			setPassword = "nullvalue";
		}
		

		if(!FactionsPlus.permission.has(sender, "factionsplus.warp")) {
			sender.sendMessage(ChatColor.RED + "No permission!");
			return;
		}


		Player player = (Player)sender;

		FPlayer fplayer = FPlayers.i.get(player);

		Faction currentFaction;
		
		if(this.argAsString(2) != null) {
			currentFaction = Factions.i.getByTag(this.argAsString(2));
			
			if( currentFaction.getId() != fme.getFactionId() && !fme.hasAdminMode() ) {
				if(!FactionsPlus.permission.has(sender, "factionsplus.warpotherfactions" ) ) {
					msg( "You do not have permission to use other Factions warps. (factionsplus.warpotherfactions)" );
					return;
				}
			}
		} else {
			currentFaction = fme.getFaction();
		}
		
		File currentWarpFile = new File(Config.folderWarps,  currentFaction.getId());

		World world;

		// Check if player can teleport from enemy territory
		if(!Config._warps.warpTeleportAllowedFromEnemyTerritory._ && fplayer.isInEnemyTerritory() ){
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
				Config._warps.warpTeleportAllowedEnemyDistance._ > 0 && ! Utilities.isSafeZone(Board.getFactionAt(new FLocation(loc))) 
				&& ( ! fplayer.isInOwnTerritory()
						|| ( fplayer.isInOwnTerritory() && ! Config._warps.warpTeleportIgnoreEnemiesIfInOwnTerritory._))){
			World w = loc.getWorld();
			double x = loc.getX();
			double y = loc.getY();
			double z = loc.getZ();

			for (Player playa : me.getServer().getOnlinePlayers())
			{
				if (playa == null || !playa.isOnline() || playa.isDead() || playa.getWorld() != w)
					continue;

				FPlayer fp = FPlayers.i.get(playa);
				if (fp.equals(fme)) {
					continue;
				}
				
				if ( ! FactionsAny.Relation.ENEMY.equals( 
					Bridge.factions.getRelationBetween( fplayer, fp ) 
					)) {
					continue;//if not enemies, continue
				}

				Location l = playa.getLocation();
				double dx = Math.abs(x - l.getX());
				double dy = Math.abs(y - l.getY());
				double dz = Math.abs(z - l.getZ());
				double max = Config._warps.warpTeleportAllowedEnemyDistance._;

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
								sender.sendMessage(FactionsPlusTemplates.Go("warp_incorrect_password", null ));
								
								return;
							}
						}
					}

					Location newTel = new Location(world, x, y, z, Y, playa);
					
					String ownfid = fplayer.getFactionId();
					
					int count = 0;
					
					do {
						FLocation warpFLocation = new FLocation( newTel );
						String warpatFID = Board.getIdAt( warpFLocation );
						if ( !ownfid.equalsIgnoreCase( warpatFID ) ) {
							if ( Config._warps.mustBeInOwnTerritoryToCreate._ ) {
								// the the destination warp should be in player's own faction's territory, else deny tp-ing to it
								// XXX: this is a workaround for 1. not removing warps that violate this constraint(assuming it changed)...
								// 2. disbanding faction or unclaiming land won't remove the warp
								fplayer.msg( "<b>You cannot teleport to warp " + ChatColor.WHITE + warpname + " <b>because it "
									+ ( 0 < count ? "will make you land outside of" : "is not in" )
									+ " your faction territory."+(0<count?" <i>(because it's obstructed)":"") );
									if( Config._warps.removeWarpIfDeniedAccess._ ) {
										File currentWarpFileTMP = new File(Config.folderWarps,  currentFaction.getId() + ".tmp");
										PrintWriter wrt=null;
										BufferedReader rdr=null;
										try {
											wrt = new PrintWriter( new FileWriter( currentWarpFileTMP ) );
											rdr = new BufferedReader( new FileReader( currentWarpFile ) );
											
											String line;
											
											while ( ( line = rdr.readLine() ) != null ) {
												String[] warp = line.split( ":" );
												if ( ( warp.length >= 1 ) && ( warp[0].equalsIgnoreCase( warpname ) ) ) {
													continue;
												}
													
												wrt.println( line );
											}
										} finally {
											if ( null != rdr ) {
												try {
													rdr.close();
												} catch ( IOException e ) {
													e.printStackTrace();
												}
											}
											if ( null != wrt ) {
												wrt.close();
											}
										}
										
										if (!currentWarpFile.delete()) {
										   	System.out.println("[FactionsPlus] Cannot delete " + currentWarpFile.getName());
										    return;
										}
										    
										 if (!currentWarpFileTMP.renameTo(currentWarpFile)) {
											 System.out.println("[FactionsPlus] Cannot rename " + currentWarpFileTMP.getName() + " to " + currentWarpFile.getName());
											 return;
										 }
										String[] aargsa = new String[1];
										aargsa[0] = warpname;
											
										player.sendMessage(FactionsPlusTemplates.Go("warped_removed", aargsa ));
										
										 //fplayer.msg( "The warp " + ChatColor.WHITE + warpname + " was removed." );
									}
										
								return;
							} else {// you can land anywhere if the 3 config options allows it below:
								Relation rel = Bridge.factions.getRelationBetween( fplayer.getFaction(), Board.getFactionAt( warpFLocation ) );
								if (
										((Config._warps.denyWarpToEnemyLand._) && (FactionsAny.Relation.ENEMY.equals( rel )))
										||((Config._warps.denyWarpToAllyLand._) && (FactionsAny.Relation.ALLY.equals( rel )))
										||((Config._warps.denyWarpToNeutralOrTruceLand._) && 
												(FactionsAny.Relation.NEUTRAL.equals( rel )
												|| FactionsAny.Relation.NEUTRAL.equals( rel )) )
									){
									fplayer.msg( "<b>You cannot teleport to warp " + ChatColor.WHITE + warpname + " <b>because it "
											+ ( 0 < count ? "will make you land inside of" : "is in" )
											+ " "+ChatColor.WHITE+rel+"<b> faction territory."+(0<count?" <i>(because it's obstructed)":"") );
										return;
								}
							}
						}
						newTel = EssentialsIntegration.getSafeDestination( newTel );
					} while ( ++count < 2 );// XXX:make this 1 to not check for safedestination, or 2 to do check
					
					if(Config._economy.costToWarp._ > 0.0d) {
						if (!payForCommand(Config._economy.costToWarp._, "to teleport to warp "+warpname, 
							"for teleporting to faction warp "+warpname)) {
							return;
						}
					}
					
					String[] aargsa = new String[2];
					aargsa[1] = warpname;
					
					player.sendMessage(FactionsPlusTemplates.Go("warped_to", aargsa ));
					//player.sendMessage(ChatColor.RED + "Warped to " + ChatColor.WHITE + warpname);
					
					//XXX: this will fail (in Factions not FP) when Essentials is unloaded then loaded again via plugman, also /f home
					if (EssentialsFeatures.handleTeleport(player, newTel)) return;

					//we still don't try to tp to the safe location. I better not be sorry for this
					newTel=new Location(world, x, y, z, Y, playa);
										
					// Create a smoke effect
					if (Config._warps.smokeEffectOnWarp._) {
						List<Location> smokeLocations = new ArrayList<Location>();
						smokeLocations.add(player.getLocation());
						smokeLocations.add(player.getLocation().add(0, 1, 0));
						smokeLocations.add(newTel);
						smokeLocations.add(newTel.clone().add(0, 1, 0));
						SmokeUtil.spawnCloudRandom(smokeLocations, 3f);
					}

					player.teleport(newTel);
					//done: investigate if ie. Essentials or something will change the actual tp location if obstructed 
					//and thus it will land you somewhere else possible a chunk that you don't own

//					in.close();

					return;
				}	
			}
			
			player.sendMessage(FactionsPlusTemplates.Go("warp_non_existant", null));
			

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
