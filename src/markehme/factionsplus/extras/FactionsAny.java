package markehme.factionsplus.extras;

import java.lang.reflect.*;

import com.massivecraft.factions.*;
import com.massivecraft.factions.cmd.*;


public interface FactionsAny {
	public static enum FFlag {
		PEACEFUL
		//adding new flags here requires adding switch-cases in two locations, do Ctrl+Alt+H on one of the flags to track where 
	}
	
	public static enum Relation {
		ENEMY
		//same should apply here, Ctrl+Alt+H  (in eclipse) to see where to add code if you add more flags
	}
	
	
//	/**
//	 * @param faction which faction to have Peaceful set to
//	 * @param state the state of the peaceful flag for that faction
//	 */
//	public void setPeaceful(Faction faction, Boolean state);
	
	public void setFlag(Faction forFaction, FactionsAny.FFlag whichFlag, Boolean whatState);
	
	public FactionsAny.Relation getRelationTo(FPlayer one, FPlayer two);

	public void addSubCommand( FCommand subCommand );

	/**
	 * this should only ever be called after {@link #addSubCommand(FCommand)} as the last statement<br>
	 * this will update the help just in case last commands didn't fit the page entirely<br>
	 */
	public void finalizeHelp();
}
