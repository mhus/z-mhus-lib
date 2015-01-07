package de.mhus.lib.form;

import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.errors.MException;

public abstract class UiElement extends MObject {

	private LayoutElement element;

	public LayoutElement getElement() {
		return element;
	}

	public void setElement(LayoutElement element) {
		this.element = element;
		if (element == null) 
			doDisconnect();
		else
			doConnect();
	}

	protected abstract void doConnect();

	protected abstract void doDisconnect();

	public abstract void doUpdate(DataConnector data) throws MException;
	
	public abstract void setErrorMessage(String msg);
	
	public boolean equals(Object arg1, Object arg2) {
		if (arg1 == null && arg2 == null) return true;
		if (arg1 == null) return false;
		return arg1.equals(arg2);
	}

}
