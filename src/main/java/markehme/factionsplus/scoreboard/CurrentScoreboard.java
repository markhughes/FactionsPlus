package markehme.factionsplus.scoreboard;

import markehme.factionsplus.MCore.FPUConf;

public enum CurrentScoreboard {
	
	None(0), Power(1), Money(2), Members(3), Land(4);
	
	CurrentScoreboard(int id) {
		this.id = id;
	}
	
	int id = 0;
	
	public CurrentScoreboard getNext(FPUConf fpuconf) {
		if(this.id == 0) return fpuconf.scoreboardDefault;
		
		if(this.id == 1) return Money.get(fpuconf);
		if(this.id == 2) return Members.get(fpuconf);
		if(this.id == 3) return Land.get(fpuconf);
		if(this.id == 4) return Power.get(fpuconf);
		
		return Power;
	}
	
	public CurrentScoreboard get(FPUConf fpuconf) {
		if(fpuconf.scoreboardRotates.containsKey(this)) {
			if(fpuconf.scoreboardRotates.get(this)) {
				return this;
			}
		}
		
		return this.getNext(fpuconf);
	}
	
}
