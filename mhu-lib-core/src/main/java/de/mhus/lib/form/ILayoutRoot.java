package de.mhus.lib.form;

import de.mhus.lib.errors.MException;

/**
 * <p>ILayoutRoot interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public interface ILayoutRoot {
	
	/**
	 * <p>build.</p>
	 *
	 * @param builder a {@link de.mhus.lib.form.IUiBuilder} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	void build(IUiBuilder builder) throws MException;
	
}
