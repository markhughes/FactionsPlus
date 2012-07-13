package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;



public class _int extends _Base {
	
	public int	_;
	public int	_defaultValue;
	
	
	public _int( int defaultValue ) {
		_defaultValue=defaultValue;
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


	@Override
	public String getDefaultValue() {
		return Integer.toString( _defaultValue );
	}
}
