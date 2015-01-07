package de.mhus.lib.portlet.actions;

import javax.portlet.ActionRequest;

import de.mhus.lib.core.IProperties;

public abstract class AbstractAction implements Action {


	@Override
	public synchronized IProperties createProperties(ActionRequest request) {
		ActionProperties properties = new ActionProperties(request);
		return properties;
	}

}
