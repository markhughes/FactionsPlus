package markehme.factionsplus.util;

import org.bukkit.permissions.Permissible;

import com.massivecraft.massivecore.util.PermUtil;

/**
 * FactionsPlus Permissions
 * Based off the way Factions does it.
 * @author MarkehMe <mark@markeh.me>
 *
 */
public enum FPPerm {

	CREATEWARP("createwarp"),
	ANNOUNCE("announce"),
	BAN("ban"),
	CLEARLWCLOCKS("clearlwclocks"),
	DEBUG("debug"),
	FACTIONHOME("factionhome"),
	NEED("need"),
	JAIL("jail"),
	SETJAIL("setjail"),
	LISTWARPS("listwarps"),
	LISTWARPSOTHERS("listwarps.others"),
	MODIFYRULES("modifyrules"),
	TOGGLESTATE("togglestate"),
	TOGGLESTATEOTHERS("togglestate.others"),
	WARP("warp"),
	WARPOTHERS("warp.others"),
	RULES("rules"),
	SCOREBOARD("scoreboard"),
	CHEST("chest"),
	CHESTSET("chest.set"),
	;

	public final String node;

	FPPerm(final String node) {
		this.node = "factionsplus."+node;
	}
	
	public boolean has(Permissible permissible, boolean informSenderIfNot) {
		return PermUtil.has(permissible, this.node, informSenderIfNot);
	}

	public boolean has(Permissible permissible) {
		return has(permissible, false);
	}
}
