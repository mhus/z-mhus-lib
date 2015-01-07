package de.mhus.lib.form;


public class LayoutSplit extends LayoutOverlay {

	public void doInit() throws Exception {
		super.doInit();
		setFullWidth(true);
		setTitleInside(true);
	}
	
	protected int doCalculateChildColumns(LayoutComposite child) {
		return getColumns() / elements.size();
	}

	protected int doCalculateChildOffset(LayoutComposite child) {
		int idx = elements.indexOf(child);
		if (idx < 1) return 0;
		LayoutComposite prev = (LayoutComposite) elements.get(idx-1);
		return prev.getOffset() + prev.getColumns();
	}

}
