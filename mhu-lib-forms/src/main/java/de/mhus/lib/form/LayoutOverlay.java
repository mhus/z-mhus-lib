package de.mhus.lib.form;

import de.mhus.lib.core.directory.ResourceNode;

/**
 * <p>LayoutOverlay class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class LayoutOverlay extends LayoutComposite {

	/** {@inheritDoc} */
	@Override
	public void doInit() throws Exception {
		super.doInit();
		setFullWidth(true);
		setTitleInside(true);
	}

	/** {@inheritDoc} */
	@Override
	protected LayoutElement doBuildChild(LayoutComposite parent, ResourceNode subConfig) throws Exception {
		if (!subConfig.getName().equals("composite")) return null;
		return super.doBuildChild(parent, subConfig);
	}

	/** {@inheritDoc} */
	@Override
	protected int doCalculateLableColumns() {
		return 0;
	}

	/** {@inheritDoc} */
	@Override
	protected int doCalculateColums() {
		return getParent().getColumns();
	}

	
}
