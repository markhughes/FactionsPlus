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
		List<Block> trapdoor = new LinkedList<Block>();
		List<Block> sign = new LinkedList<Block>();
		List<Block> wallsign = new LinkedList<Block>();




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
			if(blocks[x].getType() == Material.WOODEN_DOOR)
			{
				wooddoor.add(blocks[x].getBlock());
			}
			if(blocks[x].getType() == Material.IRON_DOOR_BLOCK)
			{
				irondoor.add(blocks[x].getBlock());
			}
			if(blocks[x].getType() == Material.TRAP_DOOR)
			{
				trapdoor.add(blocks[x].getBlock());
			}
			if(blocks[x].getType() == Material.SIGN || blocks[x].getType() == Material.WALL_SIGN)
			{
				sign.add(blocks[x].getBlock());
			}
			if(blocks[x].getType() == Material.WALL_SIGN)
			{
				wallsign.add(blocks[x].getBlock());
			}

		}
		for(int x = 0; x < chests.size(); x++)
		{
			if(lwc.findProtection(chests.get(x)) != null)
			{
				if(!fPlayer.getFaction().getFPlayers().contains(FPlayers.i.get(lwc.findProtection(chests.get(x)).getOwner())))
					lwc.findProtection(chests.get(x)).remove();
			}
		}
		for(int x = 0; x < furnaces.size(); x++)
		{
			if(lwc.findProtection(furnaces.get(x)) != null)
			{
				if(!fPlayer.getFaction().getFPlayers().contains(FPlayers.i.get(lwc.findProtection(furnaces.get(x)).getOwner())))
					lwc.findProtection(furnaces.get(x)).remove();
			}
		}
		for(int x = 0; x < wooddoor.size(); x++)
		{
			if(lwc.findProtection(wooddoor.get(x)) != null)
			{
				if(!fPlayer.getFaction().getFPlayers().contains(FPlayers.i.get(lwc.findProtection(wooddoor.get(x)).getOwner())))
					lwc.findProtection(wooddoor.get(x)).remove();
			}
		}
		for(int x = 0; x < irondoor.size(); x++)
		{
			if(lwc.findProtection(irondoor.get(x)) != null)
			{
				if(!fPlayer.getFaction().getFPlayers().contains(FPlayers.i.get(lwc.findProtection(irondoor.get(x)).getOwner())))
					lwc.findProtection(irondoor.get(x)).remove();
			}
		}
		for(int x = 0; x < trapdoor.size(); x++)
		{
			if(lwc.findProtection(trapdoor.get(x)) != null)
			{
				if(!fPlayer.getFaction().getFPlayers().contains(FPlayers.i.get(lwc.findProtection(trapdoor.get(x)).getOwner())))
					lwc.findProtection(trapdoor.get(x)).remove();
			}
		}
		for(int x = 0; x < sign.size(); x++)
		{
			if(lwc.findProtection(sign.get(x)) != null)
			{
				if(!fPlayer.getFaction().getFPlayers().contains(FPlayers.i.get(lwc.findProtection(sign.get(x)).getOwner())))
					lwc.findProtection(sign.get(x)).remove();
					
			}
		}
		for(int x = 0; x < wallsign.size(); x++)
		{
			if(lwc.findProtection(wallsign.get(x)) != null)
			{
				if(!fPlayer.getFaction().getFPlayers().contains(FPlayers.i.get(lwc.findProtection(wallsign.get(x)).getOwner())))
					lwc.findProtection(wallsign.get(x)).remove();
					
			}
		}
		
	}


}
