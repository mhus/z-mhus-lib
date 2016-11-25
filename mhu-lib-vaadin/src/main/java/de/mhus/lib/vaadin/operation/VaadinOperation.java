package de.mhus.lib.vaadin.operation;

import com.vaadin.ui.Component;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.strategy.Operation;
import de.mhus.lib.core.strategy.OperationResult;
import de.mhus.lib.vaadin.DialogControl;

public interface VaadinOperation extends Operation {

	Component createEditor(IProperties editorProperties, DialogControl control);
	OperationResult doExecute(Component editor) throws Exception;
	
}
