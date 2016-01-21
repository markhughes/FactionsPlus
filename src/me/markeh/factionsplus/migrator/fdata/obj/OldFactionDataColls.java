package me.markeh.factionsplus.migrator.fdata.obj;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.Colls;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;

public class OldFactionDataColls extends Colls<OldFactionDataColl, OldFactionData> {
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static OldFactionDataColls i = new OldFactionDataColls();
	public static OldFactionDataColls get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE: COLLS
	// -------------------------------------------- //

	@Override
	public OldFactionDataColl createColl(String collName) {
		return new OldFactionDataColl(collName);
	}

	@Override
	public Aspect getAspect() {
		return FactionsPlusAspect.get().getAspect();
	}

	@Override
	public String getBasename() {
		return Const.COLLECTION_FACTION;
	}

	@Override
	public void init() {
		super.init();

		this.migrate();
	}
	
	public void migrate() {
		// Create file objects
	}
	
	@Override
	public OldFactionDataColl get(Object o)
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