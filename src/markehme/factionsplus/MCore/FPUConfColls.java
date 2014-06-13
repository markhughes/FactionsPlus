package markehme.factionsplus.MCore;

import markehme.factionsplus.FactionsPlus;

import com.massivecraft.mcore.Aspect;
import com.massivecraft.mcore.MCore;

public class FPUConfColls extends XColls<FPUConfColl, FPUConf> {
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static FPUConfColls i = new FPUConfColls();
	public static FPUConfColls get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE: COLLS
	// -------------------------------------------- //

	@Override
	public FPUConfColl createColl(String collName) {
		return new FPUConfColl(collName);
	}

	@Override
	public Aspect getAspect() {
		return FactionsPlus.instance.getAspect();
	}

	@Override
	public String getBasename() {
		return Const.COLLECTION_UCONF;
	}

	@Override
	public FPUConf get2(Object worldNameExtractable) {
		FPUConfColl coll = this.get(worldNameExtractable);
		if (coll == null) return null;
		return coll.get(MCore.INSTANCE);
	}


}
