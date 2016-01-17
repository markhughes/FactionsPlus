package me.markeh.factionsplus.migrator.fdata.obj;

import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.AspectColl;
import com.massivecraft.massivecore.Multiverse;

public class FactionsPlusAspect {
	private static FactionsPlusAspect instance = null;
	public static FactionsPlusAspect get() {
		if (instance == null) instance = new FactionsPlusAspect();
		
		return instance;
	}
	
	// Aspect Stuff
	private Aspect aspect;
	public Aspect getAspect() { return this.aspect; }
	public Multiverse getMultiverse() { return this.getAspect().getMultiverse(); }
	
	public FactionsPlusAspect() {
		this.aspect = AspectColl.get().get(Const.ASPECT, true);
		this.aspect.register();
		this.aspect.setDesc(
			"<i>If the FactionsPlus system even is enabled and how it's configured.",
			"<i>What Factions exists and what players belong to them."
		);
	}
}
