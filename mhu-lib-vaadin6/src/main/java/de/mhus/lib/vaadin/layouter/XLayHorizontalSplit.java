package de.mhus.lib.vaadin.layouter;

import com.vaadin.ui.HorizontalSplitPanel;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;

public class XLayHorizontalSplit extends HorizontalSplitPanel implements XLayElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void setConfig(ResourceNode config) throws MException {
		LayUtil.configure(this, config);
	}

	@Override
	public void doAppendChild(XLayElement child, ResourceNode cChild) {
		if (getFirstComponent() == null)
			setFirstComponent(child);
		else
			setSecondComponent(child);
	}

}
