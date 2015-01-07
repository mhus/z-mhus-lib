package de.mhus.lib.vaadin.aqua;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;
import de.mhus.lib.vaadin.layouter.LayUtil;
import de.mhus.lib.vaadin.layouter.XLayElement;

public class DefaultHeader extends HorizontalLayout implements XLayElement, WorkingArea {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HorizontalLayout workArea;
	private VerticalLayout buttonArea;
	private Object owner;

	@Override
	public void setConfig(ResourceNode config) throws MException {
		LayUtil.configure(this, config);
		
		setWidth("100%");
		setHeight("50px");
		setStyleName("header");
		setSpacing(false);
		setMargin(false);

		workArea = new HorizontalLayout();
		
        addComponent(workArea);
        setComponentAlignment(workArea, Alignment.MIDDLE_RIGHT);
        setExpandRatio(workArea, 1);
        
        buttonArea = new VerticalLayout();
        buttonArea.setWidth("20px");
        buttonArea.setHeight("100%");
        addComponent(buttonArea);
        setExpandRatio(buttonArea, 0);
	}

	@Override
	public void doAppendChild(XLayElement child, ResourceNode cChild) {
		if (child instanceof ActionButton) {
			buttonArea.addComponent(child);
			((ActionButton)child).setWorkingArea(this);
		}
	}

	@Override
	public void setComponent(Object owner, Component container) {
		this.owner = owner;
		workArea.removeAllComponents();
		if (container != null)
			workArea.addComponent(container);
	}

	@Override
	public boolean isOwner(Object owner) {
		if (owner == null && this.owner == null) return true;
		if (owner == null) return false;
		return owner.equals(this.owner);
	}


}
