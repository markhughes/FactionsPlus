package markehme.factionsplus.config;

import markehme.factionsplus.extras.*;



public class WYIdentifier extends WY_IDBased {
	
	private String	value;
	
	
	public WYIdentifier( String identifier, String strValue, WYSection parent, WYItem prev ) {
		super( identifier, parent, prev );
		value = strValue;
		assert Q.nn( value );
//		System.out.println("ID "+this+" parent="+parent+" prev="+prev);
	}
	
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return super.toString()+" "+getValue();
	}
}
