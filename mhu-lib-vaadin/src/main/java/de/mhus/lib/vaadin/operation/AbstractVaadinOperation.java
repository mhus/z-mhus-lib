package de.mhus.lib.vaadin.operation;

import com.vaadin.ui.Component;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.strategy.AbstractOperation;
import de.mhus.lib.core.strategy.DefaultTaskContext;
import de.mhus.lib.core.strategy.OperationDescription;
import de.mhus.lib.core.strategy.OperationResult;
import de.mhus.lib.core.strategy.TaskContext;
import de.mhus.lib.vaadin.DialogControl;

public abstract class AbstractVaadinOperation extends AbstractOperation implements VaadinOperation {

	@Override
	public boolean hasAccess() {
		return true;
	}

	@Override
	public Component createEditor(IProperties editorProperties, DialogControl control) {
		AbstractVaadinOperationEditor editor = createEditor();
		if (editor == null) return null;
		editor.setSizeFull();
		//editor.setCaption(getDescription().getCaption());
		editor.initialize(editorProperties,control);
		return editor;
	}

	/**
	 * Create the visible content editor for this operation.
	 * 
	 * @return
	 */
	protected abstract AbstractVaadinOperationEditor createEditor();

	@Override
	protected abstract OperationResult doExecute2(TaskContext context) throws Exception;

	@Override
	public OperationResult doExecute(IProperties editorProperties, Component editor) throws Exception {
		if (editor != null && (editor instanceof AbstractVaadinOperationEditor))
			((AbstractVaadinOperationEditor)editor).fillOperationParameters(editorProperties);
		
		DefaultTaskContext context = new DefaultTaskContext(this.getClass());
		context.setParameters(editorProperties);
		return doExecute2(context);
	}

	@Override
	protected abstract OperationDescription createDescription();

}
