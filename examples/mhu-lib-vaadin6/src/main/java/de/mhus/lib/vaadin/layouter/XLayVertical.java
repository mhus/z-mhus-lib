package de.mhus.lib.vaadin.layouter;

import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;


public class XLayVertical extends VerticalLayout implements XLayElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void setConfig(ResourceNode config) throws MException {
		LayUtil.configure(this, config);
	}

	@Override
	public void doAppendChild(XLayElement child, ResourceNode cChild) throws MException {
		addComponent(child);
		float ratio = cChild.getFloat(LayoutBuilder.EXPAND, -1);
		if (ratio >= 0)
			setExpandRatio(child, ratio);
	}

}
