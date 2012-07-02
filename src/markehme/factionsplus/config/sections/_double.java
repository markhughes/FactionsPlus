package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class _double extends ConfigOptionName {
	public double _;
	
	
	public _double(double defaultValue) {
		_=defaultValue;
	}
	
	@Override
	public void setValue( String value ) {
		_=Double.parseDouble( value );
	}

	@Override
	public String getValue() {
		return Double.toString(_);
	}
}
