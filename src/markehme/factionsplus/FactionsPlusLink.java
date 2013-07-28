package markehme.factionsplus;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.ps.PS;

/**
 * The FactionsPlusLink is to be used by the FactionsTownyLink plugin.
 * FactionsPlusLink makes a simple way to retrieve the information for
 * the plugin.
 * 
 * @author MarkehMe
 *
 */
public class FactionsPlusLink {
	
	/**
	 * getUPlayer will return the UPlayer object for player
	 * @param player
	 * @return UPlayer
	 */
	public static UPlayer getUPlayer( Player player ) {
		return UPlayer.get( player );
	}
	
	/**
	 * getUPlayer will return the UPlayer object for player name
	 * @param player
	 * @return UPlayer
	 */
	public static UPlayer getUPlayer( String player ) {
		return UPlayer.get( Bukkit.getPlayerExact( player ) );
	}
	
	/**
	 * getFaction will return the Faction that a Player belongs to
	 * @param player
	 * @return Faction
	 */
	public static Faction getFaction( Player player ) {
		return getUPlayer( player ).getFaction();
	}
	
	/**
	 * getFactionAt will return the Faction a specific position (Player)
	 * @param player
	 * @return Faction
	 */
	public static Faction getFactionAt( Player player ) {
		return BoardColls.get().getFactionAt( PS.valueOf( player.getLocation() ) );
	}
	
	/**
	 * getFactionAt will return the Faction a specific position (Location)
	 * @param player
	 * @return Faction
	 */
	public static Faction getFactionAt( Location location ) {
		return BoardColls.get().getFactionAt( PS.valueOf( location ) );
	}
	
	/**
	 * isFactionAt will return true if a Faction has claimed land at a specific Location
	 * @param location
	 * @return boolean
	 */
	public static boolean isFactionAt( Location location ) {
		
		return( ! BoardColls.get().getFactionAt ( PS.valueOf( location ) ).isNone() );
		
	}

}
