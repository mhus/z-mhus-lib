package de.mhus.lib.vaadin.aqua;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.vaadin.CardButton;
import de.mhus.lib.vaadin.layouter.XLayElement;

@SuppressWarnings("unchecked")
public abstract class ActionButton extends CardButton implements XLayElement, DesktopInject {

//	@Override
//	public void setConfig(IConfig config) {
//		
//	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Desktop desktop;
	protected WorkingArea workArea;

	public ActionButton() {
		setWidth("20px");
		setHeight("20px");
		setBackgroundColor("#333");
		setForegroundColor("#fff");
	}
	
	@Override
	public void doAppendChild(XLayElement child, ResourceNode cChild) {
		
	}

	public void setWorkingArea(WorkingArea workArea) {
		this.workArea = workArea;
	}
	
	@Override
	public void setDesktop(Desktop desktop) {
		this.desktop = desktop;
	}

}
