package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.BooleanFormatException;
import markehme.factionsplus.config._Base;


public class _boolean extends _Base {
	public boolean _;
	public boolean _defaultValue;
	
	
	public _boolean(boolean defaultValue) {
		_defaultValue=defaultValue;
		_=defaultValue;
	}
	
	@Override
	public void setValue( String value ) {
		String nValue=value.trim().toLowerCase();
		
		if ( (!nValue.isEmpty())&& ( nValue.equals( "true" ) || nValue.equals( "false" ) || nValue.equals("0") || nValue.equals("1")) ) {
			_=Boolean.parseBoolean( nValue);
		}else{
			throw new BooleanFormatException(nValue);
		}
	}

	@Override
	public String getValue() {
		return Boolean.toString( _ );
	}

	@Override
	public String getDefaultValue() {
		return Boolean.toString( _defaultValue );
	}
	
}
