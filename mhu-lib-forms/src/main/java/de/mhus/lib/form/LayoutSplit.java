package de.mhus.lib.form;


/**
 * <p>LayoutSplit class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class LayoutSplit extends LayoutOverlay {

	/** {@inheritDoc} */
	@Override
	public void doInit() throws Exception {
		super.doInit();
		setFullWidth(true);
		setTitleInside(true);
	}
	
	/** {@inheritDoc} */
	@Override
	protected int doCalculateChildColumns(LayoutComposite child) {
		return getColumns() / elements.size();
	}

	/** {@inheritDoc} */
	@Override
	protected int doCalculateChildOffset(LayoutComposite child) {
		int idx = elements.indexOf(child);
		if (idx < 1) return 0;
		LayoutComposite prev = (LayoutComposite) elements.get(idx-1);
		return prev.getOffset() + prev.getColumns();
	}

}
