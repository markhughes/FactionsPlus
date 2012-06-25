package markehme.factionsplus.extras;

import java.util.*;

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
	
	private final static Material[] protectionsToRemove={
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
		Map<Material, LinkedList<Block> > map=new HashMap<Material, LinkedList<Block> >();
		
		Chunk chunk = location.getChunk();
		BlockState[] blocks = chunk.getTileEntities();

		//allocate a linked list for each protection type
		for ( int i = 0; i < protectionsToRemove.length; i++ ) {
			map.put( protectionsToRemove[i], new LinkedList<Block>() );
		}

		//parse each block(of the claimed chunk) and if it's of protectionsToRemove add it to the list
		for(int x = 0; x < blocks.length; x++)
		{
			Material type = blocks[x].getType();
			LinkedList<Block> list = map.get( type );
			if (null == list) {
				//that block is not one of the supported types to remove protection from ie. it's Stone
				continue;//go next block
			}
			list.add( blocks[x].getBlock() );
		}
		
		//for each protection type, attempt to remove every protection in that (claimed) chunk
		for ( int i = 0; i < protectionsToRemove.length; i++ ) {
			for ( Block block : map.get( protectionsToRemove[i] ) ) {
				Protection protectedBlock = lwc.findProtection(block);
				if (null != protectedBlock) {
					//there is a lock for that block
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


}
