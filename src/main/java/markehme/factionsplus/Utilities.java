package markehme.factionsplus;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.UConf;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.integration.Econ;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public abstract class Utilities {
	public final static Player getPlayer(String name) {
		for(Player p : Bukkit.getServer().getOnlinePlayers())
			if(p.getName().equalsIgnoreCase(name) )
				return p;
		
		return Bukkit.getPlayer(name);
	}
	
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
	
	
	
	/**
	 * checks if player has permission<br />
	 * if he's OP then it auto has perm
	 * @param p
	 * @param perm
	 * @return
	 */
	
	private static final int margin = 10;
	

	public static boolean isJustLookingAround(Location from, Location to) {
	       if (from.getWorld() != to.getWorld() && (!from.getWorld().equals(to.getWorld()))) {
	            return false;
	        }
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
		int dotAt = asString.indexOf( "." );
		if (dotAt<0) {//assumed it's fully integer
			dotAt=asString.length(); 
		}
//		multipliedByThis=Math.min( Math.getExponent( multipliedByThis )+dotAt, dotAt);
		return Integer.parseInt( asString.substring( 0, dotAt) );
	}
	
	/**
	 * Changes location except for their eye location
	 * @param from
	 * @param to
	 * @return from  (unneeded but hey)
	 */
	public static final Location setLocationExceptEye(Location from, Location to) {
		from.setWorld(to.getWorld());
		
		from.setX(to.getX());
		from.setY(to.getY());
		from.setZ(to.getZ());
		
		return from;
	}
	
	
	/**
	 * Charge a player (or their faction - if as per configuration) 
	 * @param cost
	 * @param player
	 * @return
	 */
	public static boolean doCharge(double cost, UPlayer player) {
		// Is this a worthless transaction, the console, or is economy disabled? 
		if(!UConf.get(player).econEnabled || cost == 0.0 || player.isConsole()) {
			return true;
		}
		
		// If bank is enabled, charge the Faction not the player! 
		if(UConf.get(player).bankEnabled && UConf.get(player).bankFactionPaysCosts && player.hasFaction()) {
			return Econ.modifyMoney(player.getFaction(), -cost, null);
		} else {
			return Econ.modifyMoney(player, -cost, null);
		}
	}
	
	/**
	 * Determine if there is a region for a player in a chunk.
	 * @param currentLocation
	 * @param player
	 * @return
	 */
	public static boolean checkForRegionsInChunk(Location currentLocation, Player player) {		
		World world = currentLocation.getWorld();
		Chunk chunk = world.getChunkAt(currentLocation);
		
		int minChunkX = chunk.getX() << 4;
		int minChunkZ = chunk.getZ() << 4;
		int maxChunkX = minChunkX + 15;
		int maxChunkZ = minChunkZ + 15;

		int worldHeight = world.getMaxHeight();

		BlockVector minChunk = new BlockVector(minChunkX, 0, minChunkZ);
		BlockVector maxChunk = new BlockVector(maxChunkX, worldHeight, maxChunkZ);

		RegionManager regionManager = FactionsPlus.worldGuardPlugin.getRegionManager(world);
		
		ProtectedCuboidRegion region = new ProtectedCuboidRegion("FactionsPlus_TempRegion", minChunk, maxChunk);
		
		Map<String, ProtectedRegion> allregions = regionManager.getRegions(); 
		
		List<ProtectedRegion> allregionslist = new ArrayList<ProtectedRegion>( allregions.values() );
		
		List<ProtectedRegion> overlaps;
		
		boolean foundregions = false;

		try {
			overlaps = region.getIntersectingRegions( allregionslist );
			if(overlaps == null || overlaps.isEmpty()) {
				// No regions
				foundregions = false;
			} else {
				TreeSet<ProtectedRegion> appRegions = new TreeSet<ProtectedRegion>();
				for(ProtectedRegion currentRegion : overlaps) {
					appRegions.add(currentRegion);
				}
				
				ApplicableRegionSet set = new ApplicableRegionSet(appRegions, regionManager.getRegion("__global__"));
				
				if(!set.canBuild(FactionsPlus.worldGuardPlugin.wrapPlayer(player))) {
					foundregions = true;
				}
			}
			
			overlaps.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}

		allregionslist.clear();
		
		return(foundregions);
		
	}
}
