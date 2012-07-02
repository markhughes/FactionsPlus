package markehme.factionsplus.FactionsBridge;

import java.lang.reflect.*;
import java.util.*;

import markehme.factionsplus.*;
import markehme.factionsplus.extras.*;

import com.massivecraft.factions.*;
import com.massivecraft.factions.cmd.*;
import com.massivecraft.factions.iface.*;
import com.massivecraft.factions.util.*;



public abstract class FactionsBase implements FactionsAny {
	
	private Class											classRP			= null; // the class/interface RelationParticipator
	private Method											mGetRelationTo	= null; // the method
																					// getRelationTo(RelationParticipator rp)
	private Field	fSenderMustBe_FactionAdminLeader;
	
											
	// this will hold a mapping between our Factions.version-independent FactionsAny.Relation and the specific Factions-version
	// Relation
	// except it's mapped in reversed order
	private Map<Object, FactionsAny.Relation>	mapRelation		= new HashMap<Object, FactionsAny.Relation>();
	
	
	protected FactionsBase( ) {
		boolean failed = false;
		
		try {
			classRP = Class.forName( "com.massivecraft.factions.iface.RelationParticipator" );
			
			mGetRelationTo = RelationUtil.class.getMethod( 
				( Factions16.class.equals( this.getClass() ) ? /*1.6*/"getRelationTo" : /*1.7*/"getRelationOfThatToMe"), 
				classRP,classRP );
			
			
			String sourceEnum="com.massivecraft.factions.struct."
					+ ( Factions16.class.equals( this.getClass() ) ? /*1.6*/"Relation" : /*1.7*/"Rel" );

			Reflective.mapEnums( mapRelation, sourceEnum, FactionsAny.Relation.class);
			
			Class clsFCommand = Class.forName("com.massivecraft.factions.cmd.FCommand");
			fSenderMustBe_FactionAdminLeader=clsFCommand.getField(Factions16.class.equals(this.getClass())?"senderMustBeAdmin": 
				/*1.7*/"senderMustBeLeader");
			
			
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
		} catch ( NoSuchFieldException e ) {
			e.printStackTrace();
			failed = true;
		} finally {
			if ( failed ) {
				throw FactionsPlusPlugin.bailOut( "failed to hook into Factions 1.6.x" );
			}
		}
		
		
	}

	
	

	@Override
	public FactionsAny.Relation getRelationBetween( RelationParticipator one, RelationParticipator two ) {
		boolean failed = false;
		FactionsAny.Relation ret = null;
		try {
			if ( ( null == one ) || ( null == two ) ) {
				failed = true;
				return null;// not gonna happen really
			}
			
			Object isReturn = mGetRelationTo.invoke(RelationUtil.class/*or null cause it's static class*/, one, two );
			ret = mapRelation.get( isReturn );
			if ( null == ret ) {
				FactionsPlusPlugin.severe( "impossible to be null here, because it would've errored on .init()," +
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
				throw FactionsPlusPlugin.bailOut( "failed to invoke " + mGetRelationTo );
			}
		}
		return ret;// actually reached
	}
	
	
	@Override
	public void addSubCommand( FCommand subCommand ) {
		P.p.cmdBase.addSubCommand( subCommand );// practically all that we need to do for 1.7 to have the commands in help
	}
	

	@SuppressWarnings( "boxing" )
	@Override
	public void setSenderMustBeFactionAdmin( FCommand fCommandInstance, boolean flagState ) {
		boolean failed = false;
		try {
			fSenderMustBe_FactionAdminLeader.set( fCommandInstance, flagState );
		} catch ( IllegalArgumentException e ) {
			e.printStackTrace();
			failed = true;
		} catch ( IllegalAccessException e ) {
			e.printStackTrace();
			failed = true;
		} catch ( ClassCastException e ) {
			e.printStackTrace();
			failed = true;
		} finally {
			if ( failed ) {
				throw FactionsPlusPlugin.bailOut( "failed to set field " + fSenderMustBe_FactionAdminLeader );
			}
		}
	}
}
