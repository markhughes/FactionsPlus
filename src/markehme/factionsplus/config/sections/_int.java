package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;



public class _int extends ConfigOptionName {
	
	public int	_;
	
	
	public _int( int defaultValue ) {
		_ = defaultValue;
	}
	
	
	@Override
	public void setValue( String value ) {
		_ = Integer.parseInt( value );
	}


	@Override
	public String getValue() {
		return Integer.toString( _ );
	}
}
