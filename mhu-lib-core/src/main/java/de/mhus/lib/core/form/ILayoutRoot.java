package de.mhus.lib.core.form;

import de.mhus.lib.errors.MException;

/**
 * <p>ILayoutRoot interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface ILayoutRoot {
	
	/**
	 * <p>build.</p>
	 *
	 * @param builder a {@link de.mhus.lib.core.form.IUiBuilder} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	void build(IUiBuilder builder) throws MException;
	
}
