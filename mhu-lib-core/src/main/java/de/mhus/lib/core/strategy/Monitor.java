package de.mhus.lib.core.strategy;

import de.mhus.lib.core.logging.Log;

public interface Monitor {

	void println();
	void println(Object...out);
	void print(Object...out);
	
	Log log();

	void setSteps(long steps);
	void setStep(long step);
	void incrementStep();
		
}
