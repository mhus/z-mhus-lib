package de.mhus.lib.logging;

import de.mhus.lib.core.logging.Log;

public class YLogger extends Log {

	private Log[] targets;

	public YLogger(Log ... targets) {
		super(targets[0].getName());
		this.targets = targets;
	}

    @Override
	public void log(LEVEL level, Object ... msg) {
    	for (Log target : targets)
    		target.log(level, msg);
    }
    
	@Override
	public void update() {
	}
	
	@Override
	public boolean isLevelEnabled(LEVEL level) {
		return targets[0].isLevelEnabled(level);
	}
	
}
