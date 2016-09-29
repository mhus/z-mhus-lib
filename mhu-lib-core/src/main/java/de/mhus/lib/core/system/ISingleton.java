package de.mhus.lib.core.system;

import java.io.File;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.lang.Base;
import de.mhus.lib.core.lang.BaseControl;
import de.mhus.lib.core.logging.LogFactory;

/**
 * <p>ISingleton interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface ISingleton {

	//Log createLog(Object owner);

	/**
	 * <p>getCfgManager.</p>
	 *
	 * @return a {@link de.mhus.lib.core.system.CfgManager} object.
	 * @since 3.3.0
	 */
	CfgManager getCfgManager();

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
	 */
	Base base();

	/**
	 * Return a File inside the current application context.
	 *
	 * @param dir a {@link java.lang.String} object.
	 * @return a {@link java.io.File} object.
	 * @since 3.3.0
	 */
	File getFile(String dir);

	/**
	 * <p>getSystemProperty.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param def a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 * @since 3.3.0
	 */
	String getSystemProperty(String name, String def);
}
