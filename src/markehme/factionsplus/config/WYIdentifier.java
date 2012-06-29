package markehme.factionsplus.config;

import markehme.factionsplus.extras.*;


public class WYIdentifier implements WYItem {
	
	private String	id;
	private String value;
	
	
	public WYIdentifier( String identifier ) {
		id=identifier;
		Q.nn( id );
	}


	public void setValue(String strValue ) {
		value=strValue;
		Q.nn( value);
	}
	
}
