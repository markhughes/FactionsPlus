package markehme.factionsplus.MCore;

import markehme.factionsplus.FactionsPlus;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.MStore;

public class FactionDataColl extends Coll<Faction> {
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public FactionDataColl(String name) {
		super(name, Faction.class, MStore.getDb(), FactionsPlus.instance);
	}

	// -------------------------------------------- //
	// OVERRIDE: COLL
	// -------------------------------------------- //

	@Override
	public void init() {
		super.init();
	}

	@Override
	public Faction get(Object oid) {
		Faction ret = super.get(oid);

		return ret;
	}


}
