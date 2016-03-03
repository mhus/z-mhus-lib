package de.mhus.lib.core.system;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.lang.Base;
import de.mhus.lib.core.lang.BaseControl;
import de.mhus.lib.core.logging.LogFactory;
import de.mhus.lib.core.service.ConfigProvider;

/**
 * <p>ISingleton interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface ISingleton {

	//Log createLog(Object owner);

	/**
	 * <p>getConfigProvider.</p>
	 *
	 * @return a {@link de.mhus.lib.core.service.ConfigProvider} object.
	 */
	ConfigProvider getConfigProvider();

	/**
	 * <p>getBaseControl.</p>
	 *
	 * @return a {@link de.mhus.lib.core.lang.BaseControl} object.
	 */
	BaseControl getBaseControl();

	/**
	 * <p>createActivator.</p>
	 *
	 * @return a {@link de.mhus.lib.core.MActivator} object.
	 */
	MActivator createActivator();

	/**
	 * <p>getLogFactory.</p>
	 *
	 * @return a {@link de.mhus.lib.core.logging.LogFactory} object.
	 */
	LogFactory getLogFactory();
	
	/**
	 * <p>isTrace.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	boolean isTrace(String name);
	
	/**
	 * <p>base.</p>
	 *
	 * @return a {@link de.mhus.lib.core.lang.Base} object.
	 * @since 3.2.9
	 */
	Base base();

	/**
	 * <p>reConfigure.</p>
	 *
	 * @since 3.2.9
	 */
	void reConfigure();
}
