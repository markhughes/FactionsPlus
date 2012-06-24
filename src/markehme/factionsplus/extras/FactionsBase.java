package markehme.factionsplus.extras;

import java.lang.reflect.*;
import java.util.*;

import markehme.factionsplus.*;

import com.massivecraft.factions.*;
import com.massivecraft.factions.cmd.*;



public abstract class FactionsBase implements FactionsAny {
	
	private Class											classRP			= null; // the class/interface RelationParticipator
	private Method											mGetRelationTo	= null; // the method
																					// getRelationTo(RelationParticipator rp)
																					
	// this will hold a mapping between our Factions.version independent FactionsAny.Relation and the specific Factions version
	// Relation
	// except it's mapped in reversed order
	private Map<Object, FactionsAny.Relation>	mapRelation		= new HashMap<Object, FactionsAny.Relation>();
	
	
	protected FactionsBase( ) {
		boolean failed = false;
		
		try {
			classRP = Class.forName( "com.massivecraft.factions.iface.RelationParticipator" );
			
			mGetRelationTo = FPlayer.class.getMethod( "getRelationTo", classRP );
			
			
			String sourceEnum="com.massivecraft.factions.struct."
					+ ( Factions16.class.equals( this.getClass() ) ? /*1.6*/"Relation" : /*1.7*/"Rel" );

			Reflective.mapEnums( mapRelation, sourceEnum, FactionsAny.Relation.class);
			
			
		} catch ( ClassNotFoundException e ) {
			e.printStackTrace();
			failed = true;
		} catch ( IllegalArgumentException e ) {
			e.printStackTrace();
			failed = true;
		} catch ( SecurityException e ) {
			e.printStackTrace();
			failed = true;
		} catch ( NoSuchMethodException e ) {
			e.printStackTrace();
			failed = true;
		} finally {
			if ( failed ) {
				throw FactionsPlus.bailOut( "failed to hook into Factions 1.6.x" );
			}
		}
		
		
	}

	
	

	@Override
	public FactionsAny.Relation getRelationTo( FPlayer one, FPlayer two ) {
		boolean failed = false;
		FactionsAny.Relation ret = null;
		try {
			if ( ( null == one ) || ( null == two ) ) {
				failed = true;
				return null;// not gonna happen really
			}
			
			Object isReturn = mGetRelationTo.invoke( one, two );
			ret = mapRelation.get( isReturn );
			if ( null == ret ) {
				FactionsPlus.severe( "impossible to be null here, because it would've errored on .init()," +
						"assuming the mapping was done right" );
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
			if ( ( failed ) || ( null == ret ) ) {
				throw FactionsPlus.bailOut( "failed to invoke " + mGetRelationTo );
			}
		}
		return ret;// actually reached
	}
	
	
	@Override
	public void addSubCommand( FCommand subCommand ) {
		P.p.cmdBase.addSubCommand( subCommand );// practically all that we need to do for 1.7 to have the commands in help
	}
}
