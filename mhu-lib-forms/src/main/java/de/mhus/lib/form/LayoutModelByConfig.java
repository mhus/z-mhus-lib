package de.mhus.lib.form;

import de.mhus.lib.core.directory.ResourceNode;


public class LayoutModelByConfig extends LayoutModel {

	private ResourceNode definition;
	@Override
	public void doBuild() throws Exception {
		root = new LayoutRoot(getFormFactory(), getDataSource(), getFormControl(), getNls(), getDefinition());
	}
	
	public ResourceNode getDefinition() {
		return definition;
	}
	
	public void setDefinition(ResourceNode definition) {
		this.definition = definition;
	}

}
