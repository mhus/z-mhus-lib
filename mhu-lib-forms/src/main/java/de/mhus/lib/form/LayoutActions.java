package de.mhus.lib.form;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;


public class LayoutActions extends LayoutComposite {

	private LinkedList<FormAction> actions = new LinkedList<FormAction>();
	private HashMap<String, FormAction> map = new HashMap<String, FormAction>();
	
	@Override
	protected void doBuildChildren() throws Exception {
		// load actions
		ResourceNode cActions = config.getNode("actions");
		if (cActions == null) return;
		for (ResourceNode action : cActions.getNodes()) {
			FormAction a = new FormAction(this,action);
			actions.add(a);
			map.put(action.getName(),  a);
		}
	}
	
	public FormAction[] getActions() {
		return actions.toArray(new FormAction[actions.size()]);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		DataConnector dc = (DataConnector)o;
		FormAction a = map.get(dc.getTaskName());
		if (a != null)
			try {
				a.doUpdate(dc);
			} catch (MException e) {
				log().w(e);
			}
		else
			super.update(o, arg);
	}
		
}
