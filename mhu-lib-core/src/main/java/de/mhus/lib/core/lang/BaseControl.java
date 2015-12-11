package de.mhus.lib.core.lang;

import de.mhus.lib.core.base.BaseFindStrategy;
import de.mhus.lib.core.base.InjectStrategy;
import de.mhus.lib.core.base.NoInjectionStrategy;
import de.mhus.lib.core.base.SingleBaseStrategy;
import de.mhus.lib.core.system.DefaultBase;

public class BaseControl {

	// need to create instances on request to avoid recursive loops using createBase() inside findStrategy
	private BaseFindStrategy findStrategy = null;
	private InjectStrategy injectStrategy = null;
	
	public Base base(Object owner) {
		return getFindStrategy().find(owner);
	}

	public void setFindStrategy(BaseFindStrategy strategy) {
		findStrategy = strategy;
	}
	
	public void setInjectStrategy(InjectStrategy strategy) {
		injectStrategy = strategy;
	}
	
	public synchronized InjectStrategy getInjectStrategy() {
		if (injectStrategy == null) {
			injectStrategy = new NoInjectionStrategy();
		}
		return injectStrategy;
	}
	
	public synchronized BaseFindStrategy getFindStrategy() {
		if (findStrategy == null) {
			findStrategy = new SingleBaseStrategy();
		}
		return findStrategy;
	}
	
	
	public Base createBase(MObject mObject, Base parent) {
		Base newBase = new DefaultBase(parent);
		return newBase;
	}

	public Base installBase(Base base) {
		return getFindStrategy().install(base);
	}
	
	public Base getCurrentBase() {
		return getFindStrategy().find();
	}

	public void inject(Object object, Base base) {
		getInjectStrategy().inject(object, base);
	}
	
}
