package de.mhus.lib.vaadin.layouter;

import com.vaadin.ui.VerticalSplitPanel;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;


public class XLayVerticalSplit extends VerticalSplitPanel implements XLayElement {

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
		addComponent(child);
	}

}
