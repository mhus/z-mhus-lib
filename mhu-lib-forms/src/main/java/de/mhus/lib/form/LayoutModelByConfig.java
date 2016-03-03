package de.mhus.lib.form;

import de.mhus.lib.core.directory.ResourceNode;


/**
 * <p>LayoutModelByConfig class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class LayoutModelByConfig extends LayoutModel {

	private ResourceNode definition;
	/** {@inheritDoc} */
	@Override
	public void doBuild() throws Exception {
		root = new LayoutRoot(getFormFactory(), getDataSource(), getFormControl(), getNls(), getDefinition());
	}
	
	/**
	 * <p>Getter for the field <code>definition</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 */
	public ResourceNode getDefinition() {
		return definition;
	}
	
	/**
	 * <p>Setter for the field <code>definition</code>.</p>
	 *
	 * @param definition a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 */
	public void setDefinition(ResourceNode definition) {
		this.definition = definition;
	}

}
