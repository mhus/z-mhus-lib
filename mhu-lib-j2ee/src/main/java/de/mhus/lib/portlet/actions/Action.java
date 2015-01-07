package de.mhus.lib.portlet.actions;


import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import de.mhus.lib.core.IProperties;

public interface Action {

	public boolean processAction(String path, ActionRequest request, ActionResponse response)
			throws Exception;

	public IProperties createProperties(ActionRequest request);
}
