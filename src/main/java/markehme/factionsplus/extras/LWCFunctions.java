package markehme.factionsplus.extras;

import java.util.ArrayList;
import java.util.List;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.griefcraft.model.Protection;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.ps.PS;

/**
 * call these methods only when LWC plugin is loaded in bukkit, else NoClassDefFoundError<br>
 * to avoid that, call LWCBase.isLWCPluginPresent() first
 */
public abstract class LWCFunctions extends LWCBase {
	
	private static final LWCModule lwcModule	=	new LWCModule();
	
	private static boolean hooked				=	false;
	
	
	/**
	 * you may call this repeatedly while running<br>
	 * assumes LWC is loaded by bukkit already else NoClassDefFoundError when calling this
	 */
	public static void hookLWCIfNeeded() {
		//beware here NoClassDefFoundError if LWC isn't loaded
		assert LWCBase.isLWCPluginPresent();
		
			// register after we integrate
			if ( !alreadyRegistered ) {
				try {
					Bukkit.getPluginManager().registerEvents( lwcListener, FactionsPlus.instance );
					FactionsPlus.info( "Started LWC listener" );
				} finally {
					alreadyRegistered = true;
				}
			}
	
		
		if (!hooked) {
			//we always need this in order to prevent people from locking ie. chests in enemy faction
			getLWC().getModuleLoader().registerModule( FactionsPlus.instance, lwcModule );
			hooked=true;
			FactionsPlusPlugin.info("Successfuly hooked into LWC!");
		}//else , still hooked
	}
	
	public static void unhookLWC() {
		//beware here NoClassDefFoundError if LWC isn't loaded
		try {
			deregListenerIfNeeded();
			if ( hooked ) {
				getLWC().getModuleLoader().removeModules( FactionsPlus.instance );
			}
		} finally {
			hooked = false;
		}

	}
	
	private final static List<Material> protectionsTypesToRemove = new ArrayList<Material>() {{
		add(Material.CHEST);
		add(Material.FURNACE);
		add(Material.BURNING_FURNACE); 
		add(Material.WALL_SIGN);
		add(Material.SIGN_POST); 
		add(Material.DISPENSER); 
		add(Material.HOPPER);
		
		add(Material.WOODEN_DOOR);
		add(Material.IRON_DOOR_BLOCK);
		add(Material.TRAP_DOOR);
	}};
	
	
	/**
	 * Clears all LWC locks for the chunk at passed location
	 * except the locks that are of players in the same faction as passed fPlayer
	 * 
	 * @param facLocation
	 *            the FLocation of the faction (chunk-coords basically)
	 * @param fPlayer
	 * @return number of protections removed,<br />
	 *         0 or less means none were found/removed
	 * @throws Exception
	 *             if something failed (typically this won't happen)
	 */
	public static int clearLocks( PS facLocation, UPlayer fPlayer ) throws Exception {
		
		World world = facLocation.asBukkitWorld();
		
		if ( null == world ) {
			throw new Exception( "World is null (Not loaded, or not found)" );
		}
		
		Chunk chunk = world.getChunkAt( facLocation.getBlock().asBukkitBlock().getX(), facLocation.getBlock().asBukkitBlock().getZ() );
		
		// Ensure the chunk is loaded
		if ( !world.isChunkLoaded( chunk ) ) {
			
			world.loadChunk( chunk ); // attempt to load the chunk 
			
			// Check again
			if ( !chunk.isLoaded() ) {
				throw new Exception( "Could not force load chunk." );
			}
			
		}
		
		int numberOfRemovedProtections = 0;
		
		// parse each block (of the chunk) and if it's one of protectionsTypesToRemove then remove the protection from it
		
		for ( int x = 0; x < 16; x++ ) {
			for ( int z = 0; z < 16; z++ ) {
				for ( int y = 0; y < 256; y++ ) {
					
					Block block = chunk.getBlock( x, y, z );
					
					Material type = block.getType();
					
					if ( type == Material.AIR ) {
						continue; // ignore all air blocks
					}
					
					if ( protectionsTypesToRemove.contains( type ) ) {
						// If it is possibly protected ... 
						
						Protection protectedBlock = getLWC().findProtection( block );
						if ( null != protectedBlock ) {
							// Protected block, so continue.
							
							UPlayer fpOwner = UPlayer.get( protectedBlock.getOwner() );
							if ( !fPlayer.getFaction().getUPlayers().contains( fpOwner ) ) {
								// Not in Faction, so remove protection. 
								
								protectedBlock.remove();
								numberOfRemovedProtections++;
							}
						}
					}
				}
			}
		}
		
		return numberOfRemovedProtections;
	}
	

	public static boolean checkInTerritory(Player p, Block b) {
		
		UPlayer fp = UPlayer.get(p);
		PS floc = PS.valueOf(b.getLocation());
		Faction owner = BoardColls.get().getFactionAt(floc);
		
		if( !FType.valueOf(owner).equals(FType.WILDERNESS) && owner != fp.getFaction() ){
			fp.sendMessage("You can only create locks in your own territory!");
			return false;
		}
		return true;
	}


	public static int clearLocksCommand(Player name, Location loc) {
		if (!LWCBase.isLWCPluginPresent()) {
			name.sendMessage( ChatColor.RED+"LWC plugin is not active." );
			return -1;
		}
		
		UPlayer fp = UPlayer.get(name);
		
		Chunk chunk = loc.getChunk();
		
		PS floc = PS.valueOf(loc);
		
		Faction owner = BoardColls.get().getFactionAt(floc);

		if( owner != fp.getFaction() ) {
			fp.sendMessage("You can only clear locks in your own territory!");
			return -1;
		}
		
		int numberOfRemovedProtections = 0;
		for ( int x = 0; x < 16; x++ ) {
			for ( int z = 0; z < 16; z++ ) {
				for ( int y = 0; y < 256; y++ ) {
					
					Block block = chunk.getBlock( x, y, z );
					Material type = block.getType();
					
					if ( type == Material.AIR ) {
						continue;
					}
					
					if (protectionsTypesToRemove.contains( type ) ) {

						Protection protectedBlock = getLWC().findProtection( block );
						
						if ( null != protectedBlock ) {
							UPlayer fpOwner = UPlayer.get( protectedBlock.getOwner() );
							
							if ( !fp.getFaction().getUPlayers().contains( fpOwner ) ) {
								protectedBlock.remove();
								numberOfRemovedProtections ++;
							}
						}
					}
				}
			}
		}
		
		return numberOfRemovedProtections;
	}


}
