package markehme.factionsplus.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.massivecore.ps.PS;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.MCore.FactionData;
import markehme.factionsplus.MCore.FactionDataColls;

/**
 * This entire class is related to migrating data from the old systems (0.6) to the new
 * systems used by FactionsPlus. This is the only class that ignores deprecation notices 
 * as it is the only class that should be accessing OldConfig. 
 * @author markhughes
 *
 */
@SuppressWarnings("deprecation")
public class OldMigrate {
	
	/**
	 * At the end of migration the config file moves to config.yml.migrated, so
	 * if the old config exists then it hasn't been migrated.
	 * @return
	 */
	public boolean shouldMigrate() {
		return(OldConfig.fileConfig.exists());
	}
	
	private static void msg(String m)  {
		FactionsPlus.tellConsole("[Migration] " + m);
	}
	
	/**
	 * Migrates all the fData
	 */
	public void migrateDatabase() {
		
		// Load the old configuration 
		OldConfig.init();
		OldConfig.reload();
		
		for(FactionColl fColl : FactionColls.get().getColls()) {
			for(String id : fColl.getIds()) {
				Faction faction = fColl.get(id);
				
				msg("");
				msg("[Faction] " + faction.getName() + " (" + faction.getId() + ")");
				msg("[Faction] Migration starting for this faction");
				
				// ----- Create FactionData file ----- //
				FactionData fData = FactionDataColls.get().getForUniverse(faction.getUniverse()).get(faction.getId());
				
				if(fData == null) {
					FactionDataColls.get().getForUniverse(faction.getUniverse()).create();
					msg("[Faction] [FData] FactionData File created.");
				} else {
					msg("[Faction] [FData] FactionData File already exists, moving on.");
				}
				
				// ----- Migrate Announcement ----- //
				
				File fileAnnouncementFile = new File(OldConfig.folderAnnouncements, faction.getId());
				
				if(fileAnnouncementFile.exists()) {
					try {
						fData.announcement = (Utilities.readFileAsString(fileAnnouncementFile)).trim();
						
						msg("[Faction] [Announcement] Announcement Migrated: " + fData.announcement);
					} catch (Exception e) {
						msg("[Faction] [Announcement] " + ChatColor.RED + "[ERROR]" + ChatColor.RESET + " Could not read the data for this announcement in this Faction" );

						e.printStackTrace();
					}
				} else {
					msg("[Faction] [Announcement] No announcement data to migrate, moving on.");
				}
				
				// ----- Migrate Jails ----- //
				File jailLocationFile = new File(OldConfig.folderJails, "loc." + faction.getId());
				
				// Check for a jail location 
				if(!jailLocationFile.exists()) {
					// If theres no jail location, then theres also no jailed players
					msg("[Faction] [Jail] No jail location set, moving on.");
				} else {
					
					// Read the old database information 
					String locationData[] = Utilities.readFileAsString(jailLocationFile).split(":");
					
					Location jailLocation = new Location(Bukkit.getWorld(locationData[4]), Double.valueOf(locationData[0]), Double.valueOf(locationData[1]), Double.valueOf(locationData[2]));
					
					// Create the location and store it in our fData object 
					fData.jailLocation = PS.valueOf(jailLocation);
					
					msg("[Faction] [Jail] Jail location found and moved to x: " + locationData[0]+", y: " + locationData[1] + ", z: " + locationData[2] + ", in world: " + locationData[4]);
					
					// TODO: read jailed players (since a jail has been set there could potentially be jailed players) 
				}
				
				
				// ----- Migrate Warps ----- //
				File warpsFile = new File(OldConfig.folderWarps, faction.getId());
				
				if(!warpsFile.exists()) {
					msg("[Faction] [Warps] No warps have been found for this faction, moving on.");
				} else {
					FileInputStream fis = null;
					
					try {
						fis = new FileInputStream(new File(OldConfig.folderWarps, faction.getId()));
						int b = fis.read();
						
						if(b == -1) {
							msg("[Faction] [Warps] No warps have been found for this faction, moving on.");
							
							fis.close();					
						}
						
					} catch(Exception e) {
						msg("[Faction] [Warps] " + ChatColor.RED + "[ERROR]" + ChatColor.RESET + " Could not read the data for the warps in this Faction");
						
						fis = null;
						
						e.printStackTrace();
						
					} finally {
						if(null != fis) {
							try {
								fis.close();
							} catch(Exception e) {
								e.printStackTrace();
							}
						}
					}
					
					Scanner scanner = null;
					FileReader fr = null;
					
					String warpData = "";
					
					try {
						fr = new FileReader(warpsFile);
						scanner = new Scanner(fr);
						
						while(scanner.hasNextLine()) {
							warpData = scanner.nextLine();
							
							if(!warpData.trim().isEmpty()) {
								String[] items = warpData.split(":");
								
								if (items.length > 0) {
									// is warp data
									// TODO: convert
								}
							} else {
								msg("[Faction] [Warps] No warps have been found for this faction, moving on.");
							}
						}
					} catch(Exception e) {
						msg("[Faction] [Warps] " + ChatColor.RED + "[ERROR]" + ChatColor.RESET + " Error reading warp, last data: " + warpData);
						
						e.printStackTrace();
					} finally {
						if(null != scanner) {
							scanner.close();
						}
						
						if(null != fr) {
							try {
								fr.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		
		// Migration finished - so we no longer need to use the database 
		OldConfig.fileConfig.renameTo(new File( OldConfig.folderBase, "config_disabled.yml" ));
		OldConfig.deInit();
	}
	
	public void deIntOld() {
		OldConfig.deInit();
	}
}