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
	
	private Method	mGetRole=null;
	private Map<String, FactionsAny.Relation>	renameSourceRoles=new HashMap<String, FactionsAny.Relation>();
	
	
	protected FactionsBase( ) {
		boolean failed = false;
		
		try {
			classRP = Class.forName( "com.massivecraft.factions.iface.RelationParticipator" );
			
			mGetRelationTo = RelationUtil.class.getMethod( 
				( Factions16.class.equals( this.getClass() ) ? /*1.6*/"getRelationTo" : /*1.7*/"getRelationOfThatToMe"), 
				classRP,classRP );
			//F3 easy {@link com.massivecraft.factions.iface.RelationParticipator )} hmm nvm it works on String too
			
			mGetRole=FPlayer.class.getMethod( "getRole");
			// {@link com.massivecraft.factions.FPlayer }
			
			String sourceEnum="com.massivecraft.factions.struct."
					+ ( Factions16.class.equals( this.getClass() ) ? /*1.6*/"Relation" : /*1.7*/"Rel" );

			Reflective.mapEnums( mapRelation, sourceEnum, FactionsAny.Relation.class);
			
			//1.6 Role names
			renameSourceRoles.put( "ADMIN", FactionsAny.Relation.LEADER);
			renameSourceRoles.put( "MODERATOR", FactionsAny.Relation.OFFICER);
			renameSourceRoles.put( "NORMAL", FactionsAny.Relation.MEMBER);
			//1.7 Rel names are same as those in mapRelation above which we don't add, cause mapEnums will do it auto
			
			String enumRole = (Factions16.class.equals(this.getClass())?
				/*1.6*/"com.massivecraft.factions.struct.Role": /*1.7*/"com.massivecraft.factions.struct.Rel");
			Reflective.mapEnums( mapRelation, enumRole, FactionsAny.Relation.class, renameSourceRoles,true/*skip source fields that don't exist in destination*/);
			renameSourceRoles.clear();
			
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
			assert null != isReturn:"what trickery is this?!";
			ret = mapRelation.get( isReturn );
			if ( null == ret ) {
				FactionsPlusPlugin.severe( "impossible to be null here, because it would've errored on .init()," +
						"assuming the mapping was done right, which probably means you forgot to add Role to mapping" );
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
	public Relation getRole( RelationParticipator one ) {
		Throwable error= null;
		FactionsAny.Relation ret = null;
		try {
			if ( null == one ) {
				error = new NullPointerException("parameter was null");
				return null;// not gonna happen really
			}
			
			Object isReturn = mGetRole.invoke( one );
			ret = mapRelation.get( isReturn );
//			FactionsPlus.warn("+"+ret+" "+FactionsAny.Relation.OFFICER.equals( ret )+" "+FactionsAny.Relation.LEADER.equals( ret ));
			if ( null == ret ) {
				FactionsPlusPlugin.severe( "there was no mapping for `"+isReturn+"` => bridging failed" );
			}
		} catch ( IllegalAccessException e ) {
			error = e;
		} catch ( IllegalArgumentException e ) {
			error = e;
		} catch ( InvocationTargetException e ) {
			error = e;
		} finally {
			if ( ( null != error ) || ( null == ret ) ) {
				throw FactionsPlusPlugin.bailOut( error, "failed to invoke " + mGetRole );
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
