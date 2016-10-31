package de.mhus.lib.portlet.actions;

import javax.portlet.ActionRequest;

import de.mhus.lib.core.IProperties;

/**
 * <p>Abstract AbstractAction class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class AbstractAction implements Action {


	/** {@inheritDoc} */
	@Override
	public synchronized IProperties createProperties(ActionRequest request) {
		ActionProperties properties = new ActionProperties(request);
		return properties;
	}

}
