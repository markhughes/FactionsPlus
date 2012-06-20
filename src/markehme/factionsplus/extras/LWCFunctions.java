package markehme.factionsplus.extras;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;

public class LWCFunctions {
	private static LWC lwc;

	public static void integrateLWC(LWCPlugin plugin) {
		lwc = plugin.getLWC();
	}
	public static void clearLocks(Location location, FPlayer fPlayer)
	{
		Chunk chunk = location.getChunk();
		BlockState[] blocks = chunk.getTileEntities();
		List<Block> chests = new LinkedList<Block>();
		List<Block> furnaces = new LinkedList<Block>();
		List<Block> wooddoor = new LinkedList<Block>();
		List<Block> irondoor = new LinkedList<Block>();

		
		for(int x = 0; x < blocks.length; x++)
		{
			if(blocks[x].getType() == Material.CHEST)
			{
				chests.add(blocks[x].getBlock());
			}
			if(blocks[x].getType() == Material.FURNACE)
			{
				furnaces.add(blocks[x].getBlock());
			}
			if(blocks[x].getType() == Material.WOOD_DOOR)
			{
				wooddoor.add(blocks[x].getBlock());
			}
			if(blocks[x].getType() == Material.IRON_DOOR)
			{
				irondoor.add(blocks[x].getBlock());
			}
				
		}
		for(int x = 0; x < chests.size(); x++)
		{
			if(lwc.findProtection(chests.get(x)) != null)
			{
				if(!fPlayer.getFaction().getFPlayers().contains(FPlayers.i.get(lwc.findProtection(chests.get(x)).getBukkitOwner())))
					lwc.findProtection(chests.get(x)).remove();
			}
		}
		for(int x = 0; x < furnaces.size(); x++)
		{
			if(lwc.findProtection(furnaces.get(x)) != null)
			{
				if(!fPlayer.getFaction().getFPlayers().contains(FPlayers.i.get(lwc.findProtection(furnaces.get(x)).getBukkitOwner())))
					lwc.findProtection(furnaces.get(x)).remove();
			}
		}
		for(int x = 0; x < wooddoor.size(); x++)
		{
			if(lwc.findProtection(wooddoor.get(x)) != null)
			{
				if(!fPlayer.getFaction().getFPlayers().contains(FPlayers.i.get(lwc.findProtection(wooddoor.get(x)).getBukkitOwner())))
					lwc.findProtection(wooddoor.get(x)).remove();
			}
		}
		for(int x = 0; x < irondoor.size(); x++)
		{
			if(lwc.findProtection(irondoor.get(x)) != null)
			{
				if(!fPlayer.getFaction().getFPlayers().contains(FPlayers.i.get(lwc.findProtection(irondoor.get(x)).getBukkitOwner())))
					lwc.findProtection(irondoor.get(x)).remove();
			}
		}
	}

}
