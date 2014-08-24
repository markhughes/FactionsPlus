package markehme.factionsplus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import markehme.factionsplus.config.OldConfig;
import markehme.factionsplus.util.CacheMap;
import markehme.factionsplus.util.Q;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;

/**
 * TODO: move away from this 
 *
 */
@Deprecated
public class FactionsPlusJail {
	/**
	 * caches mappings between faction id (as String) and its jail Location 
	 */
	private static CacheMap<String, Location>	cachedJailLocations = new CacheMap<String, Location>(30);
	
	/**
	 * Move this into CmdUnJail
	 * @param nameOfPlayerToBeUnjailed
	 * @param unjailer
	 * @param DontSayAnything
	 * @return
	 */
	@Deprecated 
	public static boolean removeFromJail(String nameOfPlayerToBeUnjailed, UPlayer unjailer, boolean DontSayAnything) {

		if ( UPlayer.get( Bukkit.getPlayer( nameOfPlayerToBeUnjailed ) ) == null ) {
			unjailer.sendMessage( ChatColor.RED + "That player does not exist on this server" );
			return false;
		}
		
		UPlayer fpToBeUnjailed = UPlayer.get( Bukkit.getPlayer( nameOfPlayerToBeUnjailed ) );
		
		String factionId = fpToBeUnjailed.getFactionId();
		
		if ( !unjailer.getFactionId().equals( factionId ) ) {
			if(!DontSayAnything) {
				unjailer.sendMessage( ChatColor.RED + "That player is not in your faction" );
			}
			return false;
		}
		
		
		File jailingFile = new File(OldConfig.folderJails, "jaildata." + factionId + "." + nameOfPlayerToBeUnjailed);
		if(jailingFile.exists()){
			
			//done: teleport player to original before-jailed location, works only when player is online
			FileInputStream fos = null;
			InputStreamReader osw = null;
			BufferedReader bw = null;
			Player onlinejplayer = fpToBeUnjailed.getPlayer();
			Location originalLocation=null;
			if ( null != onlinejplayer ) {
				try {
					fos = new FileInputStream( jailingFile );
					osw = new InputStreamReader( fos, Q.UTF8 );
					bw = new BufferedReader( osw );
					
					bw.readLine();// ignore the `time` line
										
					String worldName = bw.readLine();
					if ( null != worldName ) {
						World world = FactionsPlus.server.getWorld( worldName );
						double x = Double.parseDouble( bw.readLine() );
						double y = Double.parseDouble( bw.readLine() );
						double z = Double.parseDouble( bw.readLine() );
						float yo = Float.parseFloat( bw.readLine() );
						float peach = Float.parseFloat( bw.readLine() );
						
						originalLocation = new Location( world, x, y, z, yo, peach );
					}
					
					
				} catch ( Throwable e ) {
					// we're just ignoring old formats for this file which may contain only the time argument => useless
//					e.printStackTrace();//: remove this, temporary
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
			
			
			
			
			
			boolean tpSuccess = false;
			if (null != onlinejplayer) {
				if ( null == originalLocation ) {
					// however we should teleport the player somewhere other than remain inside jail
					Faction f = fpToBeUnjailed.getFaction();
					if ( null != f ) {
						originalLocation = f.getHome().asBukkitLocation();
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
				}
			}
			
			if(tpSuccess) {
				// Online, so remove all data.
				jailingFile.delete();
			} else {
				// Offline, so write 'unjail'. On login this should be checked. 
				
				/*
				 * try {
				    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter( Config.folderJails + "jaildata." + fpToBeUnjailed.getFactionId() + "." + fpToBeUnjailed.getName(), true)));
				    out.println();
				    out.println("unjail");
				    out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				*/
				
				jailingFile.delete();
				
			}
			
//			cachedJailLocations.remove(id);could or could not have existed, hmm maybe not remove this due to possibility that
			//jailLocation can be used again, yep makes sense
			
			String unjailMsg = ChatColor.WHITE+nameOfPlayerToBeUnjailed + ChatColor.GREEN+" has been removed from jail by "+
					ChatColor.WHITE+unjailer.getName()+ChatColor.GREEN+"."+
			(!tpSuccess?ChatColor.RED+" But was not teleported to original location.":"");
			if (!unjailer.getFactionId().equals(fpToBeUnjailed.getFactionId()) && !DontSayAnything) {
				unjailer.sendMessage( unjailMsg);
			}
			
			if(!DontSayAnything) {
				fpToBeUnjailed.getFaction().sendMessage( unjailMsg );
			}
			return true;
		} else {
			unjailer.sendMessage( nameOfPlayerToBeUnjailed + " is not jailed." );
			return false;
		}
	}
	
	
	
	/**
	 * Remove and use FactionData.jailLocation
	 * @param player
	 * @return
	 */
	@Deprecated 
	public static Location getJailLocation(Player player) {
		UPlayer fplayer = UPlayer.get( player ); 
		assert (null != fplayer); 
		
		String fid = fplayer.getFactionId().trim(); 
		
		Location jailLocation = cachedJailLocations.get(fid);
		
		if (null != jailLocation) {
			return jailLocation;
		}
		
		Faction CWFaction = Faction.get(fid);
		
		assert null != CWFaction:"player wasn't in a faction? like not even wilderness? this should basically not be null";
		assert fid.equals(CWFaction.getId());
		
		World world;
		
		File currentJailFile = new File(OldConfig.folderJails, "loc." + CWFaction.getId());
				
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
			        	
			    world = FactionsPlus.server.getWorld(jail_data[5]);
			    
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
	
	/**
	 * Remove and move into CmdJail
	 * @param nameOfPlayerToBeJailed
	 * @param sender
	 * @param argTime
	 * @return
	 */
	@Deprecated 
	public static boolean sendToJail(String nameOfPlayerToBeJailed, CommandSender sender, int argTime) {
		Player player = (Player)sender;
		
		UPlayer fplayer = UPlayer.get( Bukkit.getPlayer( sender.getName() ) );
		Faction currentFaction = fplayer.getFaction();
				
		OfflinePlayer playerToBeJailed = FactionsPlus.server.getOfflinePlayer( nameOfPlayerToBeJailed);
		
		
		if( UPlayer.get( Bukkit.getPlayer( nameOfPlayerToBeJailed ) ) == null ) {
			fplayer.msg("Cannot jail non-existant player '"+nameOfPlayerToBeJailed+"'");
			return false;
		}
		
		UPlayer fjplayer = UPlayer.get( Bukkit.getPlayer( nameOfPlayerToBeJailed ) );
		
		if( !fjplayer.getFactionId().equals( fplayer.getFactionId() ) ) { 
			fplayer.msg("You can only Jail players that are in your Faction!");
			return false;
		}
		
		if (fplayer.equals( fjplayer ) && (!fplayer.getPlayer().isOp())) {//allow ops to can jail themselves (for some reason)
			fplayer.sendMessage(ChatColor.RED + "You cannot jail yourself.");
			return false;
		}
		
		Location jailLoc = getJailLocation( player );
		
		if ( null != jailLoc ) {
			
			File jailingFile =
				new File( OldConfig.folderJails, "jaildata." + currentFaction.getId() + "." + playerToBeJailed.getName() );
			
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
					
					final Player onlinejplayer = playerToBeJailed.getPlayer();
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
						bw.write(  "injail" );
						bw.newLine();
						
						if( OldConfig._jails.delayBeforeSentToJail._ == 0 ) {
							
							tpSuccess=onlinejplayer.teleport( jailLoc );
							
							Faction fjpfaction = fjplayer.getFaction();
							
							String jailedMsg = ChatColor.WHITE + fjplayer.getName()+ChatColor.GREEN + " has been jailed by "+
									ChatColor.WHITE+fplayer.getName()+ ChatColor.GREEN+"!"+(null == onlinejplayer? ChatColor.WHITE+" We'll tp them to jail when they login.":
										(tpSuccess?"":ChatColor.RED+" But we couldn't teleport them to jail!") );
							
							if (fjpfaction != fplayer.getFaction()) {
								fplayer.sendMessage( jailedMsg);
							}
							
							fjplayer.getFaction().sendMessage( jailedMsg );
							
						} else {
							
							onlinejplayer.sendMessage( "You are going to be sent to jail in " + OldConfig._jails.delayBeforeSentToJail._ + " seconds." );
							
							final Location thGLC = jailLoc;
							final UPlayer a_fjplayer = fjplayer;
							final UPlayer B_fplayer = fplayer;
							
							new java.util.Timer().schedule( 
							        new java.util.TimerTask() {
							            @Override
							            public void run() {
							            	boolean _tpSuccess = false;
							            	
							            	_tpSuccess = onlinejplayer.teleport( thGLC );
							            
											Faction fjpfaction = a_fjplayer.getFaction();
											
											String jailedMsg = ChatColor.WHITE + a_fjplayer.getName()+ChatColor.GREEN + " has been jailed by "+
													ChatColor.WHITE+B_fplayer.getName()+ ChatColor.GREEN+"!"+(null == onlinejplayer? ChatColor.WHITE+" We'll tp them to jail when they login.":
														(_tpSuccess?"":ChatColor.RED+" But we couldn't teleport them to jail!") );
											
											if (fjpfaction != B_fplayer.getFaction()) {
												B_fplayer.sendMessage( jailedMsg);
											}
											
											B_fplayer.getFaction().sendMessage( jailedMsg );
											
											this.cancel();
							            }
							        }, 
							        (OldConfig._jails.delayBeforeSentToJail._ * 1000)
							);

							fjplayer.msg( onlinejplayer.getName() + " will be sent to jail in " + OldConfig._jails.delayBeforeSentToJail._ + " seconds." );
						}
					}
					
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
			
		} else {
			sender.sendMessage( ChatColor.RED + "There is no jail currently set." );
		}
		
		return false;
	}
}
