package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class _boolean extends ConfigOptionName {
	public boolean _;
	
	
	public _boolean(boolean defaultValue) {
		_=defaultValue;
	}
	
	@Override
	public void setValue( String value ) {
		_=Boolean.parseBoolean( value);
	}
	
}
