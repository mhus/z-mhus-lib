package de.mhus.lib.form;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;


/**
 * <p>LayoutActions class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class LayoutActions extends LayoutComposite {

	private LinkedList<FormAction> actions = new LinkedList<FormAction>();
	private HashMap<String, FormAction> map = new HashMap<String, FormAction>();
	
	/** {@inheritDoc} */
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
	
	/**
	 * <p>Getter for the field <code>actions</code>.</p>
	 *
	 * @return an array of {@link de.mhus.lib.form.FormAction} objects.
	 */
	public FormAction[] getActions() {
		return actions.toArray(new FormAction[actions.size()]);
	}
	
	/** {@inheritDoc} */
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
