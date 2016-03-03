package de.mhus.lib.core.pojo;

import de.mhus.lib.annotations.pojo.Action;
import de.mhus.lib.annotations.pojo.Embedded;
import de.mhus.lib.annotations.pojo.Hidden;

/**
 * <p>DefaultFilter class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class DefaultFilter implements PojoFilter {

	private boolean removeHidden;
	private boolean removeWriteOnly;
	private boolean removeReadOnly;
	private boolean removeEmbedded;
	private boolean removeNoActions;

	/**
	 * <p>Constructor for DefaultFilter.</p>
	 */
	public DefaultFilter() {
		this(true, false, true, false, true);
	}
	
	/**
	 * <p>Constructor for DefaultFilter.</p>
	 *
	 * @param removeHidden a boolean.
	 * @param removeEmbedded a boolean.
	 * @param removeWriteOnly a boolean.
	 * @param removeReadOnly a boolean.
	 * @param removeNoActions a boolean.
	 */
	public DefaultFilter(boolean removeHidden, boolean removeEmbedded, boolean removeWriteOnly, boolean removeReadOnly, boolean removeNoActions ) {
		this.removeHidden = removeHidden;
		this.removeEmbedded = removeEmbedded;
		this.removeWriteOnly = removeWriteOnly;
		this.removeReadOnly = removeReadOnly;
		this.removeNoActions = removeNoActions;
	}
	
	/** {@inheritDoc} */
	@Override
	public void filter(PojoModelImpl model) {
		for (String name : model.getAttributeNames()) {
			PojoAttribute<?> attr = model.getAttribute(name);
			if (removeHidden && attr.getAnnotation(Hidden.class) != null) {
				model.removeAttribute(name);
			} else
			if (removeEmbedded && attr.getAnnotation(Embedded.class) != null) {
				model.removeAttribute(name);
			} else
			if (removeWriteOnly && !attr.canRead() ) {
				model.removeAttribute(name);
			} else
			if (removeReadOnly && !attr.canWrite()) {
				model.removeAttribute(name);
			}
		}
		
		for (String name : model.getActionNames()) {
			PojoAction action = model.getAction(name);
			if (removeNoActions && action.getAnnotation(Action.class) == null)
				model.removeAction(name);
		}

	}

}
