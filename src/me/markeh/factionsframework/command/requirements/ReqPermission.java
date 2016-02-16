package me.markeh.factionsframework.command.requirements;

import java.util.HashMap;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.objs.Perm;

public class ReqPermission  extends Requirement {
	
	private static HashMap<Perm, ReqPermission> permissionRequirements = new HashMap<Perm, ReqPermission>();
	public static ReqPermission get(Perm perm) {
		if ( ! permissionRequirements.containsKey(perm)) {
			permissionRequirements.put(perm, new ReqPermission(perm));
		}
		
		return permissionRequirements.get(perm);
	}
	
	private Perm permission;
	
	public ReqPermission(Perm permission) {
		this.permission = permission;
	}
	
	@Override
	public boolean isMet(FactionsCommand command) {
		if ( ! this.permission.has(command.sender, true)) return false;
		
		return true;
	}
}
