package me.markeh.factionsplus.migrator.fdata.obj;

import me.markeh.factionsplus.FactionsPlus;

import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.MStore;

public class FactionDataColl extends Coll<FactionData> {
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public FactionDataColl(String name) {
		super(name, FactionData.class, MStore.getDb(), FactionsPlus.get());
	}

	// -------------------------------------------- //
	// OVERRIDE: COLL
	// -------------------------------------------- //

	@Override
	public void init() {
		super.init();
	}

	@Override
	public FactionData get(Object oid) {
		FactionData ret = super.get(oid);

		return ret;
	}
}
