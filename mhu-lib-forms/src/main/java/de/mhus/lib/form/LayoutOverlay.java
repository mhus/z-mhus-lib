package de.mhus.lib.form;

import de.mhus.lib.core.directory.ResourceNode;

public class LayoutOverlay extends LayoutComposite {

	public void doInit() throws Exception {
		super.doInit();
		setFullWidth(true);
		setTitleInside(true);
	}

	protected LayoutElement doBuildChild(LayoutComposite parent, ResourceNode subConfig) throws Exception {
		if (!subConfig.getName().equals("composite")) return null;
		return super.doBuildChild(parent, subConfig);
	}

	protected int doCalculateLableColumns() {
		return 0;
	}

	protected int doCalculateColums() {
		return getParent().getColumns();
	}

	
}
