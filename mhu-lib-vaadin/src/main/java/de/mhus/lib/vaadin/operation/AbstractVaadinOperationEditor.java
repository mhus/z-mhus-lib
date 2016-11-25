package de.mhus.lib.vaadin.operation;

import com.vaadin.ui.Panel;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.vaadin.DialogControl;

public abstract class AbstractVaadinOperationEditor extends Panel {

	private static final long serialVersionUID = 1L;
	protected DialogControl control;
	protected IProperties editorProperties;

	protected final void initialize(IProperties editorProperties, DialogControl control) {
		this.editorProperties = editorProperties;
		this.control = control;
		initUI();
	}

	protected abstract void initUI();

	public abstract void fillOperationParameters(MProperties param);
	
}
