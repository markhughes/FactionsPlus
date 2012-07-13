package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class _double extends _Base {
	public double _;
	public double _defaultValue;
	
	
	public _double(double defaultValue) {
		_defaultValue=defaultValue;
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

	@Override
	public String getDefaultValue() {
		return Double.toString(_defaultValue);
	}
}
