package markehme.factionsplus.config;

import markehme.factionsplus.config.yaml.*;


/**
 * config option metadata<br>
 */
public abstract class COMetadata {
	
	private boolean	applied	= false;
	
	
	/**
	 * don't call this directly! it's meant only to be overridden by subclasses !
	 */
	protected abstract WYItem<COMetadata> override_apply();
	
	
	/**
	 * @return the changed item
	 */
	public final WYItem<COMetadata> apply() {
		if ( applied ) {
			throw new RuntimeException( "already applied before!" );
		} else {
			try {
				return override_apply();
			} finally {
				applied = true;
			}
		}
	}
}
