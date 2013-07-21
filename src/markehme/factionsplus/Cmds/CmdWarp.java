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
import markehme.factionsplus.FactionsPlusTemplates;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;
import markehme.factionsplus.references.FP;
import markehme.factionsplus.references.FPP;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.earth2me.essentials.EssentialsPlayerListener;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;
import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.util.SmokeUtil;

public class CmdWarp extends FPCommand {
	public CmdWarp() {
		this.aliases.add("warp");

		this.requiredArgs.add("name");
		this.optionalArgs.put("password", "string");
		this.optionalArgs.put("faction", "string");
		this.errorOnToManyArgs = false;

		this.addRequirements( ReqFactionsEnabled.get() );
		this.addRequirements( ReqIsPlayer.get() );
		this.addRequirements( ReqHasFaction.get() );
		
		this.setHelp("warps to a specific warp");
		this.setDesc("warps to a specific warp");

	}

	@Override
	public void performfp() {
		String warpname = this.arg(0);
		String setPassword = "nullvalue";

		if(this.arg(1) != null) {
			if(this.arg(1) != "-") {
				setPassword = this.arg(1);
			}
		}

		if(!FP.permission.has(sender, "factionsplus.warp")) {
			msg(ChatColor.RED + "No permission!");
			return;
		}
		
		Faction currentFaction;
				
		if(this.arg(2) != null) {
			currentFaction = Faction.get(this.arg(2));
			
			if(currentFaction == null ) {
				msg( "The faction " + this.arg(2) + " could not be found." );
				return; 
			}
			
			if( currentFaction.getId() != usender.getFactionId() && !usender.isUsingAdminMode() ) {
				if(!FP.permission.has(sender, "factionsplus.warpotherfactions" ) ) {
					msg( "You do not have permission to use other Factions warps. (factionsplus.warpotherfactions)" );
					return;
				}
			}
		} else {
			currentFaction = usender.getFaction();
		}
		
		if( (! usender.hasFaction() && currentFaction != usender.getFaction() ) || currentFaction.isNone() ) {
			
			msg( ChatColor.WHITE + "You are currently not in a Faction." );
			
		}

		
		
		File currentWarpFile = new File(Config.folderWarps,  currentFaction.getId());

		World world;

		// Check if player can teleport from enemy territory
		if(!Config._warps.warpTeleportAllowedFromEnemyTerritory._ && usender.isInEnemyTerritory() ){
			msg("<b>You cannot teleport to your faction warp while in the territory of an enemy faction.");
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
		Location loc = me.getLocation().clone();
		
		if(
				Config._warps.warpTeleportAllowedEnemyDistance._ > 0 && ! Utilities.isSafeZone(BoardColls.get().getFactionAt(PS.valueOf(loc))) 
				&& ( ! usender.isInOwnTerritory()
						|| ( usender.isInOwnTerritory() && ! Config._warps.warpTeleportIgnoreEnemiesIfInOwnTerritory._))){
			World w = loc.getWorld();
			double x = loc.getX();
			double y = loc.getY();
			double z = loc.getZ();

			for (Player playa : me.getServer().getOnlinePlayers()) {
				if (playa == null || !playa.isOnline() || playa.isDead() || playa.getWorld() != w)
					continue;

				UPlayer fp = UPlayer.get(playa);
				
				if (fp.equals(usender)) {
					continue;
				}
				
				
				if ( ! fp.getRelationTo(usender).equals (Rel.ENEMY ) ) {
					continue;
				}

				Location l = playa.getLocation();
				double dx = Math.abs(x - l.getX());
				double dy = Math.abs(y - l.getY());
				double dz = Math.abs(z - l.getZ());
				double max = Config._warps.warpTeleportAllowedEnemyDistance._;

				// box-shaped distance check
				if (dx > max || dy > max || dz > max)
					continue;

				msg("<b>You cannot teleport to your faction warp while an enemy is within " + max+ " blocks of you.");
				return;
			}
		}

		if (!currentWarpFile.exists()) {
			if(currentFaction != usender.getFaction() ) {
				
				msg( ChatColor.RED + currentFaction.getName() + " has no warps!" );
				
			} else {
				
				msg( ChatColor.RED + "Your faction has no warps!" );
				
			}
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
								msg(FactionsPlusTemplates.Go("warp_incorrect_password", null ));
								
								return;
							}
						}
					}

					Location newTel = new Location(world, x, y, z, Y, playa);
					
					String ownfid = usender.getFactionId();
					
					int count = 0;
					
					do {
						PS warpFLocation = PS.valueOf( newTel );
						String warpatFID = BoardColls.get().getFactionAt( warpFLocation ).getId();
						if ( !ownfid.equalsIgnoreCase( warpatFID ) ) {
							if ( Config._warps.mustBeInOwnTerritoryToCreate._ ) {
								// the the destination warp should be in player's own faction's territory, else deny tp-ing to it
								// XXX: this is a workaround for 1. not removing warps that violate this constraint(assuming it changed)...
								// 2. disbanding faction or unclaiming land won't remove the warp
								usender.msg( "<b>You cannot teleport to warp " + ChatColor.WHITE + warpname + " <b>because it "
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
										String[] aargsa = { warpname };
											
										msg(FactionsPlusTemplates.Go("warped_removed", aargsa ));
										
										 //fplayer.msg( "The warp " + ChatColor.WHITE + warpname + " was removed." );
									}
										
								return;
							} else {// you can land anywhere if the 3 config options allows it below:
								Rel rel = usender.getFaction().getRelationTo(BoardColls.get().getFactionAt( warpFLocation ) );
								if (
										((Config._warps.denyWarpToEnemyLand._) && (Rel.ENEMY.equals( rel )))
										||((Config._warps.denyWarpToAllyLand._) && (Rel.ALLY.equals( rel )))
										||((Config._warps.denyWarpToNeutralOrTruceLand._) && 
												(Rel.NEUTRAL.equals( rel )
												|| Rel.NEUTRAL.equals( rel )) )
									) {
									msg( "<b>You cannot teleport to warp " + ChatColor.WHITE + warpname + " <b>because it "
											+ ( 0 < count ? "will make you land inside of" : "is in" )
											+ " "+ChatColor.WHITE+rel+"<b> faction territory."+(0<count?" <i>(because it's obstructed)":"") );
										return;
								}
							}
						}
						try {
							
							newTel = EssentialsIntegration.getSafeDestination( newTel );
							
						} catch(NoClassDefFoundError e) {
							
							FP.severe("Essentials is out of date. Can not get safe location.");
							
						}
					} while ( ++count < 2 );// XXX:make this 1 to not check for safedestination, or 2 to do check
					
					if(Config._economy.costToWarp._ > 0.0d) {
						if (!Utilities.doFinanceCrap(Config._economy.costToWarp._, "teleport to warp "+warpname, 
							usender)) {
							return;
						}
					}
					
					String[] aargsa = { warpname };
					
					msg(FactionsPlusTemplates.Go("warped_to", aargsa ));
					//player.sendMessage(ChatColor.RED + "Warped to " + ChatColor.WHITE + warpname);
					
					//XXX: this will fail (in Factions not FP) when Essentials is unloaded then loaded again via plugman, also /f home
					
					try {
						
						if (EssentialsIntegration.handleTeleport(me, newTel)) return;
						
					} catch( Exception e) {
						
						msg(ChatColor.RED + "Error: Error thrown on Essentials handling teleport.");
						FP.severe("Essentials is out of date. Can not handle teleport.");

						
					}
					//we still don't try to tp to the safe location. I better not be sorry for this
					newTel = new Location(world, x, y, z, Y, playa);
										
					// Create a smoke effect
					if ( Config._warps.smokeEffectOnWarp._ ) {
						List<Location> smokeLocations = new ArrayList<Location>();
						smokeLocations.add(me.getLocation());
						smokeLocations.add(me.getLocation().add(0, 1, 0));
						smokeLocations.add(newTel);
						smokeLocations.add(newTel.clone().add(0, 1, 0));
						SmokeUtil.spawnCloudRandom(smokeLocations, 3f);
					}

					me.teleport(newTel);
					
					return;
				}	
			}
			
			msg(FactionsPlusTemplates.Go("warp_non_existant", null));
			

		} catch (Exception e) {
			e.printStackTrace();

			sender.sendMessage(ChatColor.RED + "An internal error occured (02)");
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
}
