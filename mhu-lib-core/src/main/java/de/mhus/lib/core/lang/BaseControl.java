package de.mhus.lib.core.lang;

import de.mhus.lib.core.base.BaseFindStrategy;
import de.mhus.lib.core.base.InjectStrategy;
import de.mhus.lib.core.base.NoInjectionStrategy;
import de.mhus.lib.core.base.SingleBaseStrategy;
import de.mhus.lib.core.system.DefaultBase;

/**
 * <p>BaseControl class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class BaseControl {

	// need to create instances on request to avoid recursive loops using createBase() inside findStrategy
	private BaseFindStrategy findStrategy = null;
	private InjectStrategy injectStrategy = null;
	
	/**
	 * <p>base.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.core.lang.Base} object.
	 */
	public Base base(Object owner) {
		return getFindStrategy().find(owner);
	}

	/**
	 * <p>Setter for the field <code>findStrategy</code>.</p>
	 *
	 * @param strategy a {@link de.mhus.lib.core.base.BaseFindStrategy} object.
	 */
	public void setFindStrategy(BaseFindStrategy strategy) {
		findStrategy = strategy;
	}
	
	/**
	 * <p>Setter for the field <code>injectStrategy</code>.</p>
	 *
	 * @param strategy a {@link de.mhus.lib.core.base.InjectStrategy} object.
	 */
	public void setInjectStrategy(InjectStrategy strategy) {
		injectStrategy = strategy;
	}
	
	/**
	 * <p>Getter for the field <code>injectStrategy</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.base.InjectStrategy} object.
	 */
	public synchronized InjectStrategy getInjectStrategy() {
		if (injectStrategy == null) {
			injectStrategy = new NoInjectionStrategy();
		}
		return injectStrategy;
	}
	
	/**
	 * <p>Getter for the field <code>findStrategy</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.base.BaseFindStrategy} object.
	 */
	public synchronized BaseFindStrategy getFindStrategy() {
		if (findStrategy == null) {
			findStrategy = new SingleBaseStrategy();
		}
		return findStrategy;
	}
	
	
	/**
	 * <p>createBase.</p>
	 *
	 * @param mObject a {@link de.mhus.lib.core.lang.MObject} object.
	 * @param parent a {@link de.mhus.lib.core.lang.Base} object.
	 * @return a {@link de.mhus.lib.core.lang.Base} object.
	 */
	public Base createBase(MObject mObject, Base parent) {
		Base newBase = new DefaultBase(parent);
		return newBase;
	}

	/**
	 * <p>installBase.</p>
	 *
	 * @param base a {@link de.mhus.lib.core.lang.Base} object.
	 * @return a {@link de.mhus.lib.core.lang.Base} object.
	 */
	public Base installBase(Base base) {
		return getFindStrategy().install(base);
	}
	
	/**
	 * <p>getCurrentBase.</p>
	 *
	 * @return a {@link de.mhus.lib.core.lang.Base} object.
	 */
	public Base getCurrentBase() {
		return getFindStrategy().find();
	}

	/**
	 * <p>inject.</p>
	 *
	 * @param object a {@link java.lang.Object} object.
	 * @param base a {@link de.mhus.lib.core.lang.Base} object.
	 */
	public void inject(Object object, Base base) {
		getInjectStrategy().inject(object, base);
	}
	
}
