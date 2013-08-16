package markehme.factionsplus;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import markehme.factionsplus.config.Config;
import markehme.factionsplus.extras.FType;
import markehme.factionsplus.references.FP;
import markehme.factionsplus.util.Q;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import com.massivecraft.factions.FFlag;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UConf;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.integration.Econ;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;


public abstract class Utilities {
	/* ********** FILE RELATED ********** */
	
	public static Random randomG = new Random();
	
	public static String readFileAsString(File filePath) {
		FileInputStream fstream=null;
		DataInputStream in=null;
		BufferedReader br=null;
		InputStreamReader isr = null;
		try {
			fstream = new FileInputStream(filePath);
			in = new DataInputStream(fstream);
			isr=new InputStreamReader(in);
			br = new BufferedReader(isr);
			String strLine;
			String fullThing = "";

			while ((strLine = br.readLine()) != null)   {
				fullThing = fullThing + strLine + "\r\n";
			}

//			in.close();

			return fullThing;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (null != br) {
				try {
					br.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
			
			if (null != isr) {
				try {
					isr.close();
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

		return null;
	}


	public static void writeToFile(String fileN, String T) {
		BufferedWriter bw =null;
		try {
			bw = new BufferedWriter(new FileWriter(new File(fileN), true));
			bw.write(T);
			bw.newLine();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (null != bw) {
				try {
					bw.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static int countLines(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}
	/* ********** JAIL RELATED ********** */

	public static boolean isJailed(Player thePlayer) {
		UPlayer fplayer = UPlayer.get(thePlayer.getName());
		
		if(fplayer == null) return false;
		
		File jailDataFile = new File(Config.folderJails,"jaildata." + fplayer.getFactionId() + "." + thePlayer.getName());

		if(!jailDataFile.exists()) {
			return false;
		}

		String JailData = Utilities.readFileAsString(jailDataFile);
		
		// TODO: if last parm of file is "unjail" then run the unjail, and then remove the file
		
		if(JailData == "0") {
			return false;
		} else {
			return true;
		}
	}
	
	/* ********** FACTIONS RELATED ********** */

	public static boolean isOfficer(UPlayer uPlayer) {
		return( uPlayer.getRole().equals( Rel.OFFICER ) );
	}

	public static boolean isLeader(UPlayer uPlayer) {
		return( uPlayer.getRole().equals( Rel.LEADER ) );
	}

	
	/**
	 * @param fplayer
	 * @return null if offline
	 */
	public final static Player getOnlinePlayerExact(UPlayer uPlayer) {
		if (null == uPlayer) {
			return null;
		}
		return Utilities.getOnlinePlayerExact( uPlayer.getId() );
	}
	
	/**
	 * @param playerName
	 * @return null if offline
	 */
	public final static Player getOnlinePlayerExact(String playerName) {
		if ((null == playerName) || (playerName.isEmpty())) {
			return null;
		}
		return Bukkit.getPlayerExact( playerName );
	}
	
//	public static boolean checkGroupPerm(World world, String group, String permission) {
//		if(Config.config.getBoolean("enablePermissionGroups")) {
//			return(FactionsPlus.permission.groupHas(world, group, permission));
//		} else {
//			return true;
//		}
//	}

	
	public static void addPower(Player player, double amount) {
		UPlayer uPlayer = UPlayer.get(player);
		uPlayer.setPower(Double.valueOf(uPlayer.getPower() + amount));
	}

	public static void addPower(UPlayer uPlayer, double amount) {
		uPlayer.setPower(Double.valueOf(uPlayer.getPower() + amount));
	}

	public static void addPower(String player, double amount) {
		UPlayer uPlayer = UPlayer.get(player);
		uPlayer.setPower(uPlayer.getPower() + amount);
	}

	public static void removePower(Player player, double amount) {
		UPlayer uPlayer = UPlayer.get(player);
		uPlayer.setPower(uPlayer.getPower() - amount);
	}

	public static void removePower(UPlayer uPlayer, double amount) {
		uPlayer.setPower(uPlayer.getPower() - amount);
	}

	public static void removePower(String player, double amount) {
		UPlayer uPlayer = UPlayer.get(player);
		uPlayer.setPower(uPlayer.getPower() - amount);
	}

	public static int getCountOfWarps(Faction faction) {
		File currentWarpFile = new File(Config.folderWarps, faction.getId());

		int c = 0;
		if (currentWarpFile.exists()) {	
			FileInputStream fstream=null;
			DataInputStream in=null;
			BufferedReader br =null;
			try {
				fstream = new FileInputStream(currentWarpFile);
				in = new DataInputStream(fstream);
				br= new BufferedReader(new InputStreamReader(in));
				String strLine;

				while ((strLine = br.readLine()) != null) {
					if(strLine.contains(":")) {
						c++;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();			
			} finally {
				if ( null != br ) {
					try {
						br.close();
					} catch ( IOException e ) {
						e.printStackTrace();
					}
				}
				if ( null != in ) {
					try {
						in.close();
					} catch ( IOException e ) {
						e.printStackTrace();
					}
				}
				if ( null != fstream ) {
					try {
						fstream.close();
					} catch ( IOException e ) {
						e.printStackTrace();
					}
				}
			}
		}
		return c;
	}
	
	
	// ---------------- other simple utils
	
	public static File getCurrentFolder() {
		return new File(".");
	}
	
	/**
	 * the object is checked by reference (ie. == as opposed to .equals ) to see if it's contained in the array
	 * @param objRef
	 * @param array
	 * @return
	 */
	public final static boolean isReferenceInArray( Object objRef, Object[] array ) {
		for ( int i = 0; i < array.length; i++ ) {
			if (objRef == array[i]) { //not .equals(), we just want to know if that reference is in the array
				return true;
			}
		}
		return false;
	}
	
	/**
	 * uses .equals()
	 * @param objRef
	 * @param array
	 * @return
	 */
	public static int getIndexOfObjectInArray( Object objRef, Object[] array ) {
		for ( int i = 0; i < array.length; i++ ) {
			if (objRef.equals(array[i])) { //not .equals(), we just want to know if that reference is in the array
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * checks if player has permission<br />
	 * if he's OP then it auto has perm
	 * @param p
	 * @param perm
	 * @return
	 */
	public static boolean hasPermissionOrIsOp(Player p, Permission perm) {
		if (null != p) {
			return p.isOp() || p.hasPermission( perm );
		}
		return false;
	}
	
	public static boolean isOp(UPlayer uPlayer) {
		return Utilities.getOnlinePlayerExact( uPlayer ).isOp();
	}
	
	
	@Deprecated
	// Use FType from now on
	public static boolean isWarZone( Faction faction ) {
		
		if( FType.valueOf( faction ) == FType.WARZONE ) {
			
			return true;
			
		}
		
		return false;
		
	}

	@Deprecated
	// Use FType from now on
	public static boolean isSafeZone( Faction faction ) {
		
		if( FType.valueOf( faction ) == FType.SAFEZONE ) {
			
			return true;
			
		}
		
		return false;	}

	@Deprecated
	// Use FType from now on
	public static boolean isWilderness( Faction faction ) {
		
		if( FType.valueOf( faction ) == FType.WILDERNESS ) {
			
			return true;
			
		}
		
		return false;		
	}
	
	@Deprecated
	// Use FType from now on
	public static final boolean isPlayerFaction( Faction faction ) {
		
		if( FType.valueOf( faction ) == FType.FACTION ) {
			
			return true;
			
		}
		
		return false;			
	}

	/**
	 * aka non-safezone and non-warzone and not wilderness
	 * @param faction
	 * @return true is so
	 */
	@Deprecated
	// Use FType from now on
	public static boolean isNormalFaction(Faction faction) {
		return !isWarZone( faction ) && !isSafeZone(faction) && !isWilderness( faction );
	}
	
	private static final int margin = 10;
	

	public static boolean isJustLookingAround(Location from, Location to) {
		assert Q.nn( from );
		assert Q.nn( to );

	        if (from.getWorld() != to.getWorld() && (!from.getWorld().equals(to.getWorld()))) {
	            return false;
	        }
//	        System.out.println(getIntegerPartMultipzliedBy(from.getX(), 10)+" vs "+from.getX());
	        if (getIntegerPartMultipliedBy(from.getX(), margin) != getIntegerPartMultipliedBy(to.getX(), margin)) {
	            return false;
	        }
	        if (getIntegerPartMultipliedBy(from.getY(), margin) != getIntegerPartMultipliedBy(to.getY(), margin)) {
	            return false;
	        }
	        if (getIntegerPartMultipliedBy(from.getZ(), margin) != getIntegerPartMultipliedBy(to.getZ(), margin)) {
	            return false;
	        }
	        return true;
	}
	
	public static final int getIntegerPartMultipliedBy(double d, int multipliedByThis) {
		assert multipliedByThis>0;
		String asString = Double.toString( d*multipliedByThis );
		int dotAt = asString.indexOf( Config.DOT );
		if (dotAt<0) {//assumed it's fully integer
			dotAt=asString.length(); 
		}
//		multipliedByThis=Math.min( Math.getExponent( multipliedByThis )+dotAt, dotAt);
		return Integer.parseInt( asString.substring( 0, dotAt) );
	}
	
	/**
	 * just like: from=to; except the pitch/yal is not reset; ie. keep the pitch/yaw of "from"
	 * @param from
	 * @param to
	 * @return from  (unneeded but hey)
	 */
	public static final Location setLocationExceptEye(Location from, Location to) {
		assert Q.nn( from );
		assert Q.nn(to);
		from.setWorld( to.getWorld() );
		from.setX( to.getX() );
		from.setY( to.getY() );
		from.setZ( to.getZ() );
		return from;
	}
	//***********************************World Checking/Common Condition Checking for Listeners to also respect factions settings***********************//
	/**
	 * Checks if world is an ignored PvP world in faction config
	 * @param world
	 * @return boolean (true if it is an ignored world)
	 */
	public static final boolean noMonitorPvPWorld(World world) {
		String name = world.getName();
		if(FactionsPlus.ignoredPvPWorlds.contains(name)) {
			return true;
		} else {
			return false;
		}
		
	}
	/**
	 * Checks if claiming in world is disabled in config
	 * @param world
	 * @return boolean (true if it is a disabled claiming world)
	 */
	public static final boolean noClaimWorld(World world) {
		String name = world.getName();
		if(FactionsPlus.noClaimingWorlds.contains(name)) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * Checks if powerloss in world is disabled in config
	 * @param world
	 * @return boolean (true if it is a powerloss disabled world)
	 */
	public static final boolean noPowerLossWorld(World world) {
		String name = world.getName();
		if(FactionsPlus.noPowerLossWorlds.contains(name)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static final void setPeaceful(Faction faction) {
		setPeaceful(faction, true);
	}

	public static final void setPeaceful(Faction faction, boolean state) {
		assert Q.nn( faction );
		faction.setFlag(FFlag.PEACEFUL, state);
	}
	
	public static final boolean isPeaceful(Faction faction) {
		return( faction.getFlag( FFlag.PEACEFUL ) );
	}
	
	
	/**
	 * use this method instead of Conf.wildernessPowerLoss
	 * @return
	 */
	public static final boolean confIs_wildernessPowerLoss() {
		return( Faction.get("0").getFlag(FFlag.POWERLOSS) );
	}
	
	/**
	 * use this method instead of Conf.warZonePowerLoss
	 * @return
	 */
	public static final boolean confIs_warzonePowerLoss() {
		// TODO: Re-instate this
		return false;
		//technically, you don't need the faction for 1.6.x, but you do for 1.7.x version of Factions
		//return( Faction.get( "WarZone" ).getFlag(FFlag.POWERLOSS) );
		
	}
	
	/**
	 * 
	 * @param Cost of action
	 * @param because Mark did x
	 * @param UPlayer
	 * @return boolean
	 */
	public static boolean doFinanceCrap(double cost, String sinceDidX, UPlayer player) {
		if ( !Config._economy.isHooked() || ! UConf.get(player).econEnabled || Utilities.getOnlinePlayerExact(player) == null || cost == 0.0) {
			return true;
		}
		
		if( UConf.get(player).bankEnabled && UConf.get(player).bankFactionPaysCosts && player.hasFaction() ) {
			return Econ.modifyMoney(player.getFaction(), -cost, sinceDidX);
		} else {
			return Econ.modifyMoney(player, -cost, sinceDidX);
		}
	}
	
	/**
	 * Returns true or false. Based on the previous Factions source.
	 * @param loc
	 * @return
	 */
	public static boolean checkForRegionsInChunk(Location currentLocation) {
		
		// This is pretty much the exact same as Factions 1.6.x source's method
		// except it's inside FactionsPlus!
		
		World world = currentLocation.getWorld();
		Chunk chunk = world.getChunkAt( currentLocation );
		
		int minChunkX = chunk.getX() << 4;
		int minChunkZ = chunk.getZ() << 4;
		int maxChunkX = minChunkX + 15;
		int maxChunkZ = minChunkZ + 15;

		int worldHeight = world.getMaxHeight();

		BlockVector minChunk = new BlockVector( minChunkX, 0, minChunkZ );
		BlockVector maxChunk = new BlockVector( maxChunkX, worldHeight, maxChunkZ );

		RegionManager regionManager = FP.worldGuardPlugin.getRegionManager( world );
		
		ProtectedCuboidRegion region = new ProtectedCuboidRegion( "factionsplus_tempoverlapcheck", minChunk, maxChunk );
		
		Map<String, ProtectedRegion> allregions = regionManager.getRegions(); 
		
		List<ProtectedRegion> allregionslist = new ArrayList<ProtectedRegion>( allregions.values() );
		
		List<ProtectedRegion> overlaps;
		
		boolean foundregions = false;

		try {
			overlaps = region.getIntersectingRegions( allregionslist );
			if( overlaps == null || overlaps.isEmpty() ) {
				foundregions = false;
			} else {
				foundregions = true;
			}
		} catch ( Exception e ) {
			
			e.printStackTrace();
			
		}

		region = null;
		allregionslist = null;
		overlaps = null;

		return( foundregions );
		
	}

	
}



