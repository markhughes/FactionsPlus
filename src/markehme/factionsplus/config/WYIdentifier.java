package markehme.factionsplus.config;

import markehme.factionsplus.extras.*;



public class WYIdentifier extends WYItem {
	
	private String	id;
	private String	value;
	
	
	public WYIdentifier( String identifier, String strValue, WYItem parent, WYItem prev ) {
		super( parent, prev );
		id = identifier;
		assert Q.nn( id );
		value = strValue;
		assert Q.nn( value );
	}
	
	
	public String getId() {
		return id;
	}
	
	public String getValue() {
		return value;
	}
	// public void setValue(String strValue ) {
	// }
	
}
