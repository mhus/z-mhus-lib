package de.mhus.lib.core.strategy;

import de.mhus.lib.core.logging.Log;

public interface Monitor {

	void println();
	void println(Object...out);
	void print(Object...out);
	
	Log log();

	void setEstimatedSteps(long steps);
	void setCurrentStep(long step);
	void incrementStep();
		
}
