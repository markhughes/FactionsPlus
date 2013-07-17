package markehme.factionsplus.extras;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.config.Config;
import markehme.factionsplus.listeners.LocketteListener;
import markehme.factionsplus.listeners.MVPListener;
import markehme.factionsplus.references.FPP;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.yi.acru.bukkit.Lockette.Lockette;

import com.griefcraft.model.Protection;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.ps.PS;

public class LocketteFunctions {
	static LocketteListener lockettelisten = null;
	
	public static int removeLocketteLocks( PS facLocation, UPlayer uPlayer ) throws Exception {
		
		World world = facLocation.getChunk().asBukkitWorld();
		
		if ( null == world ) {
			throw new Exception( "World is undenified." );
		}
		
		Chunk chunk = world.getChunkAt( 
				(uPlayer.getPlayer().getLocation().getBlockX()), 
				(uPlayer.getPlayer().getLocation().getBlockZ())
			);

		if ( !world.isChunkLoaded( chunk ) ) {
			world.loadChunk( chunk );
			
			if ( !chunk.isLoaded() ) {
				throw new Exception( "Failed to force load chunk at x: " + chunk.getX() + ", z:" + chunk.getZ() );
			}
		}
		
		int numberOfRemovedProtections = 0;
		
		// parse each block(in the chunk) and if it's a sign 
		for ( int x = 0; x < 16; x++ ) {
			for ( int z = 0; z < 16; z++ ) {
				for ( int y = 0; y < 256; y++ ) {
					
					Block block = chunk.getBlock( x, y, z );
					Material type = block.getType();
					
					// Is the block protected?
					// isProtected() checks the block type, we don't need to check the block type
					if( Lockette.isProtected( block ) ) {
						
						// Lockettes build-in function saves us time
						String ownerN = Lockette.getProtectedOwner( block );
						UPlayer blockOwner = UPlayer.get( ownerN );
						
						// Validate they're not in the same Faction. 
						if( blockOwner.getFactionId() != uPlayer.getFactionId() ) {
							
							// No Lockette internal functions where helpful, this works though.
							
							Block northBlock 	= block.getRelative( BlockFace.NORTH );
							Block southBlock 	= block.getRelative( BlockFace.SOUTH );
							Block eastBlock 	= block.getRelative( BlockFace.EAST );
							Block westBlock 	= block.getRelative( BlockFace.WEST );
							
							if( northBlock.getType().equals( Material.WALL_SIGN ) ) {
								
						    	northBlock.breakNaturally();
						    	
						    } else if( southBlock.getType().equals( Material.WALL_SIGN ) ) {
						    	
								southBlock.breakNaturally();
								
						    } else if( eastBlock.getType().equals( Material.WALL_SIGN ) ) {
						    	
								eastBlock.breakNaturally();
								
						    } else if( westBlock.getType().equals( Material.WALL_SIGN ) ) {
						    	
								westBlock.breakNaturally();
								
						    } else {
						    	
						    	uPlayer.msg( "Error: Couldn't remove lockette protection." );
						    	
						    }
						    
							
							blockOwner.msg( ChatColor.RED + "You had a protected item at x: " + block.getX() +", y:" + block.getY() + " that has been claimed over." );
							
							numberOfRemovedProtections++;
						}
						
					}

				}
			}
		}
		return numberOfRemovedProtections;
	}
	
	public static final void enableOrDisable(FactionsPlus instance){
 		PluginManager pm = Bukkit.getServer().getPluginManager();
 		
 		if(Config._extras._protection.removeSignProtectionOnClaim._) {
 			if ( pm.isPluginEnabled("Lockette") ) {
				assert ( null == lockettelisten );
				
				lockettelisten = new LocketteListener();
				pm.registerEvents( lockettelisten, instance );
				
				if (null == lockettelisten) {
					lockettelisten = new LocketteListener();
					Bukkit.getServer().getPluginManager().registerEvents(lockettelisten, instance);
				}
				
				FPP.info( "Hooked into Lockette." );
				
				return;
 			} else {
 				FPP.warn("extras.protection.removeSignProtectionOnClaim is enabled but Lockette was not found.");
 			}
		}
 		
 		// TODO: unhook events if they're registered 
	}
}
