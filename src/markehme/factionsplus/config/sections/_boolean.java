package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class _boolean extends _Base {
	public boolean _;
	
	
	public _boolean(boolean defaultValue) {
		_=defaultValue;
	}
	
	@Override
	public void setValue( String value ) {
		value=value.trim().toLowerCase();
		if ( (!value.isEmpty())&& ( value.equals( "true" ) || value.equals( "false" ) || value.equals("0") || value.equals("1")) ) {
			_=Boolean.parseBoolean( value);
		}else{
			throw new BooleanFormatException(value);
		}
	}

	@Override
	public String getValue() {
		return Boolean.toString( _ );
	}
	
}
