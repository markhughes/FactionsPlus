package markehme.factionsplus;

import java.io.*;
import java.nio.charset.*;
import java.util.Scanner;

import markehme.factionsplus.Cmds.CmdSetJail;
import markehme.factionsplus.FactionsBridge.*;
import markehme.factionsplus.config.*;
import markehme.factionsplus.util.*;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.zcore.persist.*;

public class FactionsPlusJail {
	public static Server server;
	/**
	 * caches mappings between faction id (as String) and its jail Location 
	 */
	private static CacheMap<String, Location>	cachedJailLocations=new CacheMap<String, Location>(30);
	
	public static boolean removeFromJail(String nameOfPlayerToBeUnjailed, FPlayer unjailer) {

		if ( !FPlayers.i.exists( nameOfPlayerToBeUnjailed ) ) {
			unjailer.sendMessage( ChatColor.RED + "That player does not exist on this server" );
			return false;
		}
		
		FPlayer fpToBeUnjailed = FPlayers.i.get( nameOfPlayerToBeUnjailed );// never null
//		unjailer.sendMessage( "mapped "+nameOfPlayerToBeUnjailed+" to "+fpToBeUnjailed.getName() );
//		unjailer.sendMessage( "mapped "+Bukkit.getPlayer( nameOfPlayerToBeUnjailed )+" to "+fpToBeUnjailed.getId() );

		String factionId=fpToBeUnjailed.getFactionId();
		
		if ( !unjailer.getFactionId().equals( factionId ) ) {
			unjailer.sendMessage( ChatColor.RED + "That player is not in your faction" );
			return false;
		}
		
		
		File jailingFile = new File(Config.folderJails, "jaildata." + factionId + "." + nameOfPlayerToBeUnjailed);
		if(jailingFile.exists()){
			
			//done: teleport player to original before-jailed location, works only when player is online
			FileInputStream fos = null;
			InputStreamReader osw = null;
			BufferedReader bw = null;
			Player onlinejplayer = Utilities.getOnlinePlayerExact( fpToBeUnjailed);
			Location originalLocation=null;
			if ( null != onlinejplayer ) {
				try {
					fos = new FileInputStream( jailingFile );
					osw = new InputStreamReader( fos, Q.UTF8 );
					bw = new BufferedReader( osw );
					
					bw.readLine();// ignore the `time` line
					
					// XXX: if player isn't online when unjailed, it won't be sent back to original location
					// I don't feel like keeping track of this in the file, it would require changing the isJailed to check
					// inside the file if a flag is set (jailed=YES/NO; teleportedBack=YES/NO) and when both are YES then delete 
					//the file, else keep it until both are YES, ie. when player comes online 
					
					
					String worldName = bw.readLine();
					if ( null != worldName ) {
						World world = server.getWorld( worldName );
						double x = Double.parseDouble( bw.readLine() );
						double y = Double.parseDouble( bw.readLine() );
						double z = Double.parseDouble( bw.readLine() );
						float yo = Float.parseFloat( bw.readLine() );
						float peach = Float.parseFloat( bw.readLine() );
						
						originalLocation = new Location( world, x, y, z, yo, peach );
					}
					
					
				} catch ( Throwable e ) {
					// we're just ignoring old formats for this file which may contain only the time argument => useless
					e.printStackTrace();//FIXME: remove this, temporary
				} finally {
					if ( null != bw ) {
						try {
							bw.close();
						} catch ( IOException e ) {
							e.printStackTrace();
						}
					}
					if ( null != osw ) {
						try {
							osw.close();
						} catch ( IOException e ) {
							e.printStackTrace();
						}
					}
					if ( null != fos ) {
						try {
							fos.close();
						} catch ( IOException e ) {
							e.printStackTrace();
						}
					}
				}
			}
			
			jailingFile.delete();
			
			
			
			boolean tpSuccess = false;
			if (null != onlinejplayer) {
				if ( null == originalLocation ) {
					// however we should teleport the player somewhere other than remain inside jail
					Faction f = fpToBeUnjailed.getFaction();
					if ( null != f ) {
						originalLocation = f.getHome();
						if ( null == originalLocation ) {
							originalLocation = onlinejplayer.getBedSpawnLocation();
							if ( null == originalLocation ) {
								originalLocation = onlinejplayer.getWorld().getSpawnLocation();
							}
						}
					}
				}
				
				if ( null != originalLocation ) {
					tpSuccess = onlinejplayer.teleport( originalLocation );
//					unjailer.sendMessage( "teleported "+onlinejplayer.getName()+" to orig location" );wow if jailer is 's2' and jailed one is 's' then the former gets teleported
				}
			}
			
//			cachedJailLocations.remove(id);could or could not have existed, hmm maybe not remove this due to possibility that
			//jailLocation can be used again, yep makes sense
			
			unjailer.sendMessage( nameOfPlayerToBeUnjailed + " has been removed from jail."+
				(!tpSuccess?ChatColor.RED+" But was not teleported to original location.":""));
			return true;
		} else {
			unjailer.sendMessage( nameOfPlayerToBeUnjailed + " is not jailed." );
			return false;
		}
	}
	
	
	
	
	public static Location getJailLocation(Player player) {
		FPlayer fplayer = FPlayers.i.get( player );//considering Factions' implementation of this, this is never null
		assert (null != fplayer)&&(FPlayers.i.isCreative());//if is creative, even if player didn't exist it will be instance-created
		//thing is, it's always creative, on both 1.6 and 1.7 (for players, not for factions)
		String fid = fplayer.getFactionId().trim();//just in case
		
		Location jailLocation=cachedJailLocations.get(fid);
		if (null != jailLocation) {
//			System.out.println("found in cache: "+fid+"->"+jailLocation);
			return jailLocation;
		}
//		System.out.println("not in cache: "+fid+"->"+jailLocation);
		
		Faction CWFaction = Factions.i.get(fid);
		assert null != CWFaction:"player wasn't in a faction ? like not even wilderness? this should basically not be null";
		assert fid.equals(CWFaction.getId());
		
		World world;
		
		File currentJailFile = new File(Config.folderJails, "loc." + CWFaction.getId());
				
		if(currentJailFile.exists()) {
			Scanner scanner=null;
			try {
				scanner = new Scanner(currentJailFile);
				String JailData =scanner.useDelimiter("\\A").next();
					
				String[] jail_data =  JailData.split(":");
					
			    double x = Double.parseDouble(jail_data[0]);
			    double y = Double.parseDouble(jail_data[1]); // Y-Axis
			    double z = Double.parseDouble(jail_data[2]);
			    
			    float Y = Float.parseFloat(jail_data[3]); // Yaw
			    float p = Float.parseFloat(jail_data[4]);
			        	
			    world = server.getWorld(jail_data[5]);
			    
			    jailLocation=new Location(world, x, y, z, Y, p);
			    Location existed = cachedJailLocations.put( fid, jailLocation );
//			    System.out.println("added to cache: "+fid+"->"+jailLocation);
			    assert null == existed:"bad code logic, should not have existed, unless it skipped the above get at beginning of method";
			    return jailLocation;
			    
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if (null != scanner) {
					scanner.close();
				}
			}
		}
		return null;
	}
	
//	@Deprecated
//	public static void OrganiseJail(Player player) {
//		// creates jail file for a certain player TODO: Implant timed jails 
//		// 0 	=	Not jailed, so remove the file
//		// -1	=	Permentaly Jailed
//		// 1	=	Any number larger than 1 stands for minutes 
//		FPlayer fplayer = FPlayers.i.get(player.getName());
//		
//		File jailingFile = new File(Config.folderJails,"jaildata." + fplayer.getFactionId() + "." + player.getName());
//		
//		if(!jailingFile.exists()) {
//			try {
//				jailingFile.createNewFile();
//				
//				FileWriter filewrite = new FileWriter(jailingFile, true);
//				filewrite.write("0");
//				
//				filewrite.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	
//	@Deprecated
//	public static boolean doJailPlayer(Player player, String name, int time) {
//		if(!FactionsPlus.permission.has(player, "factionsplus.jail")) {
//			player.sendMessage(ChatColor.RED + "No permission!");
//			return false;
//		}
//		
//		String args[] = null;
//		
//		Player jplayer = server.getPlayer(name);
//		
//		FPlayer fjplayer = FPlayers.i.get(jplayer.getName());
//		String jcurrentID = fjplayer.getFaction().getId();
//		
//		FPlayer fplayer = FPlayers.i.get(player.getName());
//		String PcurrentID = fplayer.getFaction().getId();
//		
//		if(jcurrentID != PcurrentID) {
//			player.sendMessage("You can only jail players in your Faction!");
//			return false;
//		}
//		
//		OrganiseJail(jplayer);
//		
//		name = jplayer == null ? name.toLowerCase() : jplayer.getName().toLowerCase();
//		
//		File jailingFile = new File(Config.folderJails, "jaildata." + PcurrentID + "." + player.getName());
//		
//		if(!jailingFile.exists()) {
//			try {
//				jailingFile.createNewFile();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//		if (name != null) {
//			jplayer.sendMessage(FactionsPlusTemplates.Go("jailed_message", args));
//			return sendToJail(name, player, time);
//		}
//		
//		return false;
//
//	}
	
	public static boolean sendToJail(String nameOfPlayerToBeJailed, CommandSender sender, int argTime) {
		Player player = (Player)sender;
		
		FPlayer fplayer = FPlayers.i.get(sender.getName());
		Faction currentFaction = fplayer.getFaction();
		
		World world;
		//online or offline at the time of the jailing
		//TODO: investigate if getOfflinePlayer acts as  getPlayerExact or as getPlayer    for offline players. It'd better act as Exact ffs
		OfflinePlayer playerToBeJailed = server.getOfflinePlayer( nameOfPlayerToBeJailed);
		
		
		if (!FPlayers.i.exists( nameOfPlayerToBeJailed )) {
			fplayer.msg("Cannot jail inexisting player '"+nameOfPlayerToBeJailed+"'");
			return false;
		}
		
		FPlayer fjplayer = FPlayers.i.get(nameOfPlayerToBeJailed);//this is never null, an instance is always created if didn't previously exist
//		fplayer.msg(jailingplayer+" "+fjplayer.getFactionId()+" "+fplayer.getFactionId());
		if(!fjplayer.getFactionId().equals(fplayer.getFactionId())) {//they are numbers in String
			fplayer.msg("You can only Jail players that are in your Faction!");
			return false;
		}
		
		Location jailLoc = getJailLocation( player );
		
//		File currentJailFile = new File(Config.folderJails, "loc." + currentFaction.getId());
		
//		if(currentJailFile.exists()) {
		if ( null != jailLoc ) {
			// Scanner scanner=null;
			// try {
			// scanner=new Scanner(currentJailFile);
			// String JailData = scanner.useDelimiter("\\A").next();//'teh ?
			//
			// String[] jail_data = JailData.split(":");
			//
			// double x = Double.parseDouble(jail_data[0]);
			// double y = Double.parseDouble(jail_data[1]); // y axis
			// double z = Double.parseDouble(jail_data[2]);
			//
			// float yo = Float.parseFloat(jail_data[3]); // yaw
			// float peach = Float.parseFloat(jail_data[4]);
			//
			// world = server.getWorld(jail_data[5]);
			
			File jailingFile =
				new File( Config.folderJails, "jaildata." + currentFaction.getId() + "." + playerToBeJailed.getName() );
			
			if ( !jailingFile.exists() ) {
				
				FileOutputStream fos = null;
				OutputStreamWriter osw = null;
				BufferedWriter bw = null;
				try {
					fos = new FileOutputStream( jailingFile);
					osw = new OutputStreamWriter( fos, Q.UTF8 );
					bw = new BufferedWriter( osw );
					
					bw.write( Integer.toString( argTime ));
					bw.newLine();
					
					Player onlinejplayer = playerToBeJailed.getPlayer();
					boolean tpSuccess=false;
					if ( null != onlinejplayer ) {
						// done inform: what if it returns false aka teleport was not successful?

						Location originalLocation = onlinejplayer.getLocation();
						bw.write( originalLocation.getWorld().getName() );
						bw.newLine();
						bw.write( Double.toString( originalLocation.getX() ));
						bw.newLine();
						bw.write( Double.toString( originalLocation.getY() ));
						bw.newLine();
						bw.write( Double.toString( originalLocation.getZ() ));
						bw.newLine();
						bw.write( Float.toString( originalLocation.getYaw() ));
						bw.newLine();
						bw.write( Float.toString( originalLocation.getPitch() ));
						bw.newLine();
						
						tpSuccess=onlinejplayer.teleport( jailLoc );
					}
					
					sender.sendMessage( ChatColor.GREEN + fjplayer.getName() + " has been jailed!"
						+ (null == onlinejplayer? ChatColor.WHITE+" We'll tp them to jail when they login.":
							(tpSuccess?"":ChatColor.RED+" But we couldn't teleport them to jail!")
						  ) 
						              );
					
				} catch ( IOException e ) {
					e.printStackTrace();
					sender.sendMessage( ChatColor.RED+"internal error happened");
				} finally {
					if ( null != bw ) {
						try {
							bw.close();
						} catch ( IOException e ) {
							e.printStackTrace();
						}
					}
					if ( null != osw ) {
						try {
							osw.close();
						} catch ( IOException e ) {
							e.printStackTrace();
						}
					}
					if ( null != fos ) {
						try {
							fos.close();
						} catch ( IOException e ) {
							e.printStackTrace();
						}
					}
				}
					
			} else {
				sender.sendMessage( ChatColor.RED + fjplayer.getName() + " is already jailed!" );
			}
			
			// } catch (Exception e) {
			// e.printStackTrace();
			// sender.sendMessage(ChatColor.RED + "Can not read the jail data, is a jail set?");
			// }finally {
			// if (null != scanner) {
			// scanner.close();
			// }
			// }
			
		} else {
			sender.sendMessage( ChatColor.RED + "There is no jail currently set." );
		}
		
		return false;
	}
	
	public static boolean setJail(CommandSender sender) {
		if(!FactionsPlus.permission.has(sender, "factionsplus.setjail")) {
			sender.sendMessage(ChatColor.RED + "No permission!");
			return false;
		}
		
		FPlayer fplayer = FPlayers.i.get(sender.getName());
		Faction currentFaction = fplayer.getFaction();
		
		boolean authallow = ((Config._jails.leadersCanSetJails._) && (Utilities.isLeader( fplayer ))) 
		|| ((Config._jails.officersCanSetJails._) && (Utilities.isOfficer( fplayer )))
		|| (Config._jails.membersCanSetJails._);
		
		
		if(!authallow) {
			sender.sendMessage(ChatColor.RED + "Sorry, your faction rank is not allowed to do that!");
			//ie. leader maybe can't but officer can, depending on the options set in config (while clearly that's crazy to set,
			//it's possible and up to server admin)
			return false;
		}
		
		if(!fplayer.isInOwnTerritory()) {
			sender.sendMessage(ChatColor.RED + "You must be in your own territory to set the jail location!");
			return false;
		}
		
		if(Config._economy.isHooked()) {
			if(Config._economy.costToSetJail._ > 0.0d) {//TODO: fill those empty strings
				if(!CmdSetJail.doFinanceCrap(Config._economy.costToSetJail._, "", "", FPlayers.i.get(Bukkit.getPlayer(sender.getName())))) {
					return false;
				}
			}
		}
		
		String cfid = currentFaction.getId();
		File currentJailFile = new File(Config.folderJails,"loc." + cfid);
		
		Player player = (Player)sender;
		
		Location loc = player.getLocation();
		
		String jailData = loc.getX() + ":" + 
        loc.getY() + ":" + 
        loc.getZ() + ":" + 
        loc.getYaw() + ":" + 
        loc.getPitch() + ":" + loc.getWorld().getName();
		
		DataOutputStream jailWrite=null;
		FileOutputStream fos = null;
		try {
			fos=new FileOutputStream(currentJailFile, false);
			jailWrite = new DataOutputStream(fos);
			jailWrite.write(jailData.getBytes());
//			jailWrite.close();
			
			cachedJailLocations.put( cfid, loc );
			sender.sendMessage("Jail set!");
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			
			sender.sendMessage("Failed to set jail (Internal error -2)");
			cachedJailLocations.remove( cfid );//just in case
			return false;
		}finally{
			if (null != jailWrite) {
				try {
					jailWrite.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
			if (null != fos) {
				try {
					fos.close();//likely already closed by jailWrite
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
		}
		
	}

//	@Deprecated
//	public static void unjailPlayer(String name, int id) {
//		new File(Config.folderJails, "jaildata." + id + "." + name).delete();
//	}
//
//	@Deprecated
//	public static double getTempJailTime(Player p) {
//		// TODO: getTempJailTime Function
//		return 0;
//	}
}
