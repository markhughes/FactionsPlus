package markehme.factionsplus.config;

import org.bukkit.*;

import markehme.factionsplus.config.yaml.*;


/**
 * config option metadata<br>
 */
public abstract class COMetadata {
	public static final ChatColor COLOR_FOR_NEW_OPTIONS_ADDED=ChatColor.GREEN; 
	
	private boolean	applied	= false;
	
	
	/**
	 * don't call this directly! it's meant only to be overridden by subclasses !
	 */
	protected abstract void override_apply();
	
	
	/**
	 * @return the changed item
	 */
	public final void apply() {
		if ( applied ) {
			throw new RuntimeException( "already applied before!" );
		} else {
			try {
				override_apply();
			} finally {
				applied = true;
			}
		}
	}
}
