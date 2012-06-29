package markehme.factionsplus.config;

import markehme.factionsplus.extras.*;



public class WYIdentifier extends WYItem {
	
	private String	id;
	private String	value;
	
	
	public WYIdentifier( String identifier, String strValue, WYSection parent, WYItem prev ) {
		super( parent, prev );
		id = identifier;
		assert Q.nn( id );
		value = strValue;
		assert Q.nn( value );
		System.out.println("ID "+this+" parent="+parent+" prev="+prev);
	}
	
	
	public String getId() {
		return id;
	}
	
	public String getValue() {
		return value;
	}
	// public void setValue(String strValue ) {
	// }

	@Override
	public String toString() {
		return getId()+": "+getValue();
	}
}
