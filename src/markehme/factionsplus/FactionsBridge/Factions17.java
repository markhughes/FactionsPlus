package markehme.factionsplus.FactionsBridge;

import java.lang.reflect.*;
import markehme.factionsplus.*;
import markehme.factionsplus.extras.*;

import com.massivecraft.factions.*;



public class Factions17 extends FactionsBase implements FactionsAny {
	
	private Method			mSetFlag		= null;// Faction.setFlag(FFlag);
	private Class			classFFlag		= null;// FFlag.class
	
	private TwoWayMapOfNonNulls<Object, FactionsAny.FFlag>	mapFFlag		= new TwoWayMapOfNonNulls<Object, FactionsAny.FFlag>();
	
	protected Factions17( ) {
		super();
		
		boolean failed = false;
		
		try {
			classFFlag = Class.forName( "com.massivecraft.factions.struct.FFlag" );
			
			mSetFlag = Faction.class.getMethod( "setFlag", classFFlag, boolean.class );
			
			Reflective.mapEnums( mapFFlag, "com.massivecraft.factions.struct.FFlag", FactionsAny.FFlag.class);
			
		} catch ( NoSuchMethodException e ) {// avoided multi catch so we can compile this with jdk6 too
			e.printStackTrace();
			failed = true;
		} catch ( SecurityException e ) {
			e.printStackTrace();
			failed = true;
		} catch ( ClassNotFoundException e ) {
			e.printStackTrace();
			failed = true;
		} catch ( IllegalArgumentException e ) {
			e.printStackTrace();
			failed = true;
		} finally {
			if ( failed ) {
				throw FactionsPlusPlugin.bailOut( "failed to hook into Factions 1.7.x" );
			}
		}
	}
	
	
	@Override
	public void setFlag( Faction forFaction, FactionsAny.FFlag whichFlag, Boolean whatState ) {
		boolean failed = false;
		try {
			Object flag = mapFFlag.getLeftSide( whichFlag );
			if (null == flag) {
				failed=true;
				throw FactionsPlusPlugin.bailOut( "failed to proplerly map in .init()" );
			}else {
				// factiont.setFlag(com.massivecraft.factions.struct.FFlag.PEACEFUL, true);
				mSetFlag.invoke( forFaction, flag, whatState );
			}
		} catch ( IllegalAccessException e ) {
			e.printStackTrace();
			failed = true;
		} catch ( IllegalArgumentException e ) {
			e.printStackTrace();
			failed = true;
		} catch ( InvocationTargetException e ) {
			e.printStackTrace();
			failed = true;
		} finally {
			if ( failed ) {
				throw FactionsPlusPlugin.bailOut( "failed to invoke " + mSetFlag );
			}
		}
	}
	
	
	@Override
	public final void finalizeHelp() {
		// not required for 1.7
		return;
	}


	@Override
	@Deprecated
	public ChatMode setChatMode(FPlayer forWhatPlayer, ChatMode chatMode ) {
		return null;//telling caller it didn't happen
	}


	@Override
	@Deprecated
	public ChatMode getChatMode(FPlayer forWhatPlayer) {//not supported in 1.7
		//basically if this call is reached, then the caller/coder didn't make the required checks to see if it's non 1.7 Factions version
		//and by not doing those, you could end up expecting certain results from this method which will not happen ie. causing bugs later
		throw new RuntimeException("not supposed to be called in 1.7");
//		return null;
	}
}
