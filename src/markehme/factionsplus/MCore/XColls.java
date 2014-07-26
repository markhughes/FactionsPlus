package markehme.factionsplus.MCore;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.Colls;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;

/**
 * Taken from: https://github.com/MassiveCraft/Factions/blob/master/src/com/massivecraft/factions/entity/XColls.java
 *
 * @param <C>
 * @param <E>
 */
public abstract class XColls<C extends Coll<E>, E> extends Colls<C, E> {
	
	@Override
	public C get(Object o)
	{
		if (o == null) return null;

		if (o instanceof Entity)
		{
			String universe = ((Entity<?>)o).getUniverse();
			if (universe == null) return null;
			return this.getForUniverse(universe);
		}

		if (o instanceof Coll)
		{
			String universe = ((Coll<?>)o).getUniverse();
			if (universe == null) return null;
			return this.getForUniverse(universe);
		}

		if ((o instanceof CommandSender) && !(o instanceof Player))
		{
			return this.getForWorld(Bukkit.getWorlds().get(0).getName());
		}

		String worldName = MUtil.extract(String.class, "worldName", o);
		if (worldName == null) return null;
		return this.getForWorld(worldName);
	}

}
