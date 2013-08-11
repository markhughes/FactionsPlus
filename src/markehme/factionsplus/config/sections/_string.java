package markehme.factionsplus.config.sections;

import markehme.factionsplus.config._Base;


public class _string extends _Base {
	public String _;
	public String _defaultValue;
	
	
	public _string(String defaultValue) {
		_defaultValue=defaultValue;
		_=defaultValue;
	}
	
	@Override
	public void setValue( String value ) {
		_= value.trim();
	}

	@Override
	public String getValue() {
		return _ ;
	}

	@Override
	public String getDefaultValue() {
		return _defaultValue;
	}
	
}
