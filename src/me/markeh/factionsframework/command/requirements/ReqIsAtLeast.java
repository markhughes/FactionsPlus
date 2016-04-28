package me.markeh.factionsframework.command.requirements;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.objs.Rel;

public class ReqIsAtLeast extends Requirement {
	
	public static ReqIsAtLeast get(Rel rel) {
		return new ReqIsAtLeast(rel);
	}
	
	public ReqIsAtLeast(Rel minimumRel) {
		this.minimumRel = minimumRel;
	}
	
	private Rel minimumRel;
	
	@Override
	public boolean isMet(FactionsCommand command) {
		Rel rel = command.getFPlayer().getFaction().getRealtionshipTo(command.getFPlayer());
		
		Boolean met = false;
		
		if (this.minimumRel == Rel.LEADER) {
			met = (rel == Rel.LEADER);
		}
		
		if (this.minimumRel == Rel.OFFICER) {
			met = (rel == Rel.LEADER || rel == Rel.OFFICER);
		}
		
		if (this.minimumRel == Rel.MEMBER) {
			met = (rel == Rel.LEADER || rel == Rel.OFFICER || rel == Rel.MEMBER);
		}
		
		if (this.minimumRel == Rel.RECRUIT) {
			met = (rel == Rel.LEADER || rel == Rel.OFFICER || rel == Rel.MEMBER || rel == Rel.RECRUIT);
		}
		
		if ( ! met) {
			command.sender.sendMessage(command.colourise("<reset><red>Your rank is not high enough to run this command!"));
		}
		
		return met;
	}

}
