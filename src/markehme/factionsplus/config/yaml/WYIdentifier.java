package markehme.factionsplus.config.yaml;

import markehme.factionsplus.util.*;



/**
 * two of these are .equals() if their ids .equals(), regardless of their value; used for HashMap<br>
 */
public class WYIdentifier<METADATA_TYPE> extends WY_IDBased<METADATA_TYPE> {
	
	public static final WYIdentifier	NULL	= new WYIdentifier(-1,WYIdentifier.class.getName(),"NULL");
	
	private String	value;
	
	
	public WYIdentifier( int lineNumber, String identifier, String strValue){//, WYSection parent, WYItem prev ) {
		super( lineNumber, identifier);//, parent, prev );
		value = strValue;
		assert Q.nn( value );
		// System.out.println("ID "+this+" parent="+parent+" prev="+prev);
	}
	
	
	public String getValue() {
		return value;
	}
	
	
	@Override
	public String toString() {
		return super.toString() + " " + getValue();
	}
	
	
	@Override
	public boolean equals( Object obj ) {
		if ( ( null == obj ) || ( !( obj instanceof WYIdentifier ) ) ) {// which means can be subclass of WYIdentifier too
			return false;
		}
		return ( (WYIdentifier)obj ).getId().equals( this.getId() );
	}
	
	
	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}
	
	
	public void setValue( String string ) {
		value = string;
	}
	
}
