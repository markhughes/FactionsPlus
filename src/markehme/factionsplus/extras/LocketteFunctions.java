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
		
		Chunk chunk = world.getChunkAt( facLocation.getBlockX().intValue(), facLocation.getBlockZ().intValue() );
		
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
					if(Lockette.isProtected(block)) {
						
						String ownerN = Lockette.getProtectedOwner(block);
						UPlayer fOwner = UPlayer.get(ownerN);
						
						// Validate they're not in the same Faction. 
						if(fOwner.getFactionId() != uPlayer.getFactionId()) {
							
							Block attachBlock = Lockette.getSignAttachedBlock(block);
							
							Sign aSign		= (Sign) attachBlock.getState();
							
							aSign.setLine(1, "[Not Private]");
							aSign.setLine(2, "Removed Private");
							aSign.setLine(3, "on claim.");
							aSign.setLine(4, "");
							
							// Update the sign
							aSign.update(true);
							
							// Notify the user
							// TODO: 	This would get annoying if multiple protections have been removed in the one chunk
							//			so this needs to be made so that only one message is sent to the player, and change
							//			item to items. 
							
							fOwner.msg(ChatColor.RED + "You had a protected item at x: " + aSign.getX() +", y:" + aSign.getY() + " that has been claimed over.");
							
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
