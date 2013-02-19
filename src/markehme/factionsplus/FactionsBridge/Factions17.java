package markehme.factionsplus.FactionsBridge;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import markehme.factionsplus.FactionsPlusPlugin;
import markehme.factionsplus.util.TwoWayMapOfNonNulls;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;



public class Factions17 extends FactionsBase implements FactionsAny {
	
	private Method			mSetFlag		= null;// Faction.setFlag(FFlag);
	private Method			mGetFlag		= null;// FFlag Faction.getFlag();
	private Class			classFFlag		= null;// FFlag.class
	
	private TwoWayMapOfNonNulls<Object, FactionsAny.FFlag>	mapFFlag		= new TwoWayMapOfNonNulls<Object, FactionsAny.FFlag>();
	
	protected Factions17( ) {
		super();
		
		boolean failed = false;
		
		try {
			classFFlag = Class.forName( "com.massivecraft.factions.struct.FFlag" );
			
			mSetFlag = Faction.class.getMethod( "setFlag", classFFlag, boolean.class );
			mGetFlag = Faction.class.getMethod( "getFlag", classFFlag);
			
			Reflective.mapEnums( mapFFlag, "com.massivecraft.factions.struct.FFlag", FactionsAny.FFlag.class, null);
			
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
		Throwable failed = null;
		try {
			Object flag = mapFFlag.getLeftSide( whichFlag );
			if (null == flag) {
				failed=new NullPointerException("flag returned null which means: failed to proplerly map in .init()");
				throw null;//FactionsPlusPlugin.bailOut( "failed to proplerly map in .init()" );
			}else {
				// factiont.setFlag(com.massivecraft.factions.struct.FFlag.PEACEFUL, true);
				mSetFlag.invoke( forFaction, flag, whatState );
			}
		} catch ( IllegalAccessException e ) {
			failed = e;
		} catch ( IllegalArgumentException e ) {
			failed = e;
		} catch ( InvocationTargetException e ) {
			failed = e;
		} finally {
			if ( null != failed ) {
				throw FactionsPlusPlugin.bailOut(failed, "failed to invoke " + mSetFlag );
			}
		}
	}
	
	@Override
	public boolean getFlag( Faction forFaction, FactionsAny.FFlag whichFlag ) {
		Throwable failed = null;
		try {
			Object flag = mapFFlag.getLeftSide( whichFlag );
			if ( null == flag ) {
				failed = new NullPointerException( "flag returned null which means: failed to proplerly map in .init()" );
				throw null;// FactionsPlusPlugin.bailOut( "failed to proplerly map in .init()" );
			} else {
				// boolean ret=forFaction.getFlag(flag);
				Object ret = mGetFlag.invoke( forFaction, flag );
				assert null != ret;
				assert ret instanceof Boolean;
				return ((Boolean)ret).booleanValue();
			}
		} catch ( IllegalArgumentException e ) {
			failed = e;
		} catch ( IllegalAccessException e ) {
			failed = e;
		} catch ( InvocationTargetException e ) {
			failed = e;
		} finally {
			if ( null != failed ) {
				throw FactionsPlusPlugin.bailOut( failed, "failed to invoke " + mGetFlag );
			}
		}
		throw null;//unreachable
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


	@Override
	public final boolean isFactions17() {
		return true;
	}


	
}
