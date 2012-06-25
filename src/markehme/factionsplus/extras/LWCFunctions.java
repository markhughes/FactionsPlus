package markehme.factionsplus.extras;

import java.util.*;

import markehme.factionsplus.*;

import org.apache.commons.lang.*;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;
import com.griefcraft.model.*;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;

public class LWCFunctions {
	private static LWC lwc;
 
	public static void integrateLWC(LWCPlugin plugin) {
		lwc = plugin.getLWC();
	}
	
	private final static Material[] protectionsTypesToRemove={
		 Material.CHEST
		,Material.FURNACE
		,Material.WOODEN_DOOR
		,Material.IRON_DOOR_BLOCK
		,Material.TRAP_DOOR
		,Material.SIGN
		,Material.WALL_SIGN
	};
	
	/**
	 * Clears all LWC locks for the chunk at passed location
	 * except the locks that are of players in the same faction as passed fPlayer
	 * @param location
	 * @param fPlayer
	 */
	public static void clearLocks(Location location, FPlayer fPlayer)
	{
		Chunk chunk = location.getChunk();
		BlockState[] blocks = chunk.getTileEntities();

		//parse each block(in the chunk) and if it's of protectionsTypesToRemove then remove the protection from it
		for(int x = 0; x < blocks.length; x++)
		{
			Material type = blocks[x].getType();
			if (isProtectionTypeToRemove(type)) {
				Block block = blocks[x].getBlock();
				//if the chunk contents never get lost somehow then we don't need to cache(in a list) all protected blocks
				//so we can thus remove the protection here while parsing every block of the chunk (within this 'for')
				Protection protectedBlock = lwc.findProtection(block);
				if (null != protectedBlock) {
					//there is a lock for that block ie. it's a chest
					FPlayer fpOwner = FPlayers.i.get(protectedBlock.getOwner());
					if (!fPlayer.getFaction().getFPlayers().contains(fpOwner)) {
						//protection owner is not in the faction? then clear the lock 
						//only if the owner of the protected block is not in the same faction as fPlayer
						//only then remove the lwc protection from that block (ie. chest)
						protectedBlock.remove();
					}
				}
			}
		}
		
	}

	private static boolean isProtectionTypeToRemove( Material type ) {
		return Utilities.isReferenceInArray(type, protectionsTypesToRemove);
	}


}
