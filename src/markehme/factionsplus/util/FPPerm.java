package markehme.factionsplus.util;

import org.bukkit.permissions.Permissible;

import com.massivecraft.mcore.util.PermUtil;

public enum FPPerm {

	CREATEWARP("createwarp"),
	;

	public final String node;

	FPPerm(final String node) {
		this.node = "factionsplus."+node;
	}
	
	public boolean has(Permissible permissible, boolean informSenderIfNot){
		return PermUtil.has(permissible, this.node, informSenderIfNot);
	}

	public boolean has(Permissible permissible){
		return has(permissible, false);
	}
}
