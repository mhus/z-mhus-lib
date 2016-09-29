package de.mhus.lib.portlet.actions;


import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.core.IProperties;

/**
 * <p>Action interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface Action {

	/**
	 * <p>processAction.</p>
	 *
	 * @param path a {@link java.lang.String} object.
	 * @param request a {@link javax.portlet.ActionRequest} object.
	 * @param response a {@link javax.portlet.ActionResponse} object.
	 * @return a boolean.
	 * @throws java.lang.Exception if any.
	 */
	public boolean processAction(String path, ActionRequest request, ActionResponse response)
			throws Exception;

	/**
	 * <p>createProperties.</p>
	 *
	 * @param request a {@link javax.portlet.ActionRequest} object.
	 * @return a {@link de.mhus.lib.core.IProperties} object.
	 */
	public IProperties createProperties(ActionRequest request);
}
