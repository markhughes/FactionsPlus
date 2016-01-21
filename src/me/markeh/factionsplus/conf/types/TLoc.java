package me.markeh.factionsplus.conf.types;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

import me.markeh.factionsplus.FactionsPlus;
import me.markeh.factionsplus.conf.obj.TType;

public class TLoc extends TType<TLoc>  {
	
	public static TLoc valueOf(Location location) {
		return new TLoc(location);
	}
	
	public static TLoc fromRaw(String raw) {
		if (raw.trim().isEmpty()) return null;
		
		FactionsPlus.get().debug("'" + raw + "'");
		
		String[] data = raw.split(" / ");
		
		FactionsPlus.get().debug("0 = " + data[0]);
		FactionsPlus.get().debug("1 = " + data[1]);
		FactionsPlus.get().debug("2 = " + data[2]);
		FactionsPlus.get().debug("3 = " + data[3]);
		FactionsPlus.get().debug("4 = " + data[4]);
		FactionsPlus.get().debug("5 = " + data[5]);

		Location location = new Location(
				Bukkit.getWorld(UUID.fromString(data[5])),
				Double.valueOf(data[0]),
				Double.valueOf(data[1]),
				Double.valueOf(data[2]));
		
		location.setPitch(Float.valueOf(data[3]));
		location.setYaw(Float.valueOf(data[4]));
		
		return new TLoc(location);
	}
	public TLoc(Location location) {
		this.bukkitLocation = location;
	}
	
	private Location bukkitLocation;
	
	@Override
	public String asRawString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(this.bukkitLocation.getX() + " / ");
		sb.append(this.bukkitLocation.getY() + " / ");
		sb.append(this.bukkitLocation.getZ() + " / ");
		sb.append(this.bukkitLocation.getPitch() + " / ");
		sb.append(this.bukkitLocation.getYaw() + " / ");
		sb.append(this.bukkitLocation.getWorld().getUID().toString());
		
		return sb.toString();
	}

	@Override
	public TLoc valueOf(String raw) {
		return fromRaw(raw);
	}
	
	public Location getBukkitLocation() {
		return this.bukkitLocation;
	}
	
	public Block getBlock() {
		return this.bukkitLocation.getBlock();
	}

}
