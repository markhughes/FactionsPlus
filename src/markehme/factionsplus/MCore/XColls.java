package markehme.factionsplus.MCore;

import org.bukkit.Bukkit;

import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.Colls;
import com.massivecraft.mcore.store.Entity;
import com.massivecraft.mcore.util.MUtil;
import com.massivecraft.mcore.util.SenderUtil;

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

		if (SenderUtil.isNonplayer(o))
		{
			return this.getForWorld(Bukkit.getWorlds().get(0).getName());
		}

		String worldName = MUtil.extract(String.class, "worldName", o);
		if (worldName == null) return null;
		return this.getForWorld(worldName);
	}

}
