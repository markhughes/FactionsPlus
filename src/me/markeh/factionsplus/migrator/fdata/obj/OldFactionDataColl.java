package me.markeh.factionsplus.migrator.fdata.obj;

import me.markeh.factionsplus.FactionsPlus;

import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.MStore;

public class OldFactionDataColl extends Coll<OldFactionData> {
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public OldFactionDataColl(String name) {
		super(name, OldFactionData.class, MStore.getDb(), FactionsPlus.get());
	}

	// -------------------------------------------- //
	// OVERRIDE: COLL
	// -------------------------------------------- //

	@Override
	public void init() {
		super.init();
	}

	@Override
	public OldFactionData get(Object oid) {
		OldFactionData ret = super.get(oid);

		return ret;
	}
}
