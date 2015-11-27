package de.mhus.lib.vaadin.form;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;

import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.ValidationException;
import de.mhus.lib.form.DataSource;
import de.mhus.lib.form.Form;
import de.mhus.lib.form.UiComponent;

public abstract class UiVaadin extends UiComponent {

	private Component componentWizard;
	private Component componentError;
	private Component componentLabel;
	private Component componentEditor;
	private boolean editorEditable = true;
	
	@Override
	public void doRevert() throws MException {
		DataSource ds = getForm().getDataSource();
		setEnabled( ds.getBoolean(this, DataSource.ENABLED, true) );
		setVisible( ds.getBoolean(this, DataSource.VISIBLE, true) );
		setValue(ds.getObject(this, DataSource.VALUE, null));
		setCaption(ds.getString(this, DataSource.CAPTION, getName()));
		if (componentError != null) componentError.setVisible(false);
		editorEditable = ds.getBoolean(this, DataSource.EDITOR_EDITABLE, true);
		if (componentEditor != null && !editorEditable) componentEditor.setEnabled(false);
		getForm().getControl().reverted(this);
	}

	@Override
	public void setVisible(boolean visible) throws MException {
		if (componentLabel != null) componentLabel.setVisible(visible);
		if (componentError != null && componentError.isVisible()) componentError.setVisible(visible);
		if (componentEditor != null) componentEditor.setVisible(visible);
		if (componentWizard != null) componentWizard.setVisible(visible);
	}

	@Override
	public boolean isVisible() throws MException {
		if (componentLabel != null && componentLabel.isVisible()) return true;
		if (componentError != null && componentError.isVisible()) return true;
		if (componentEditor != null && componentEditor.isVisible()) return true;
		if (componentWizard != null && componentWizard.isVisible()) return true;
		return false;
	}

	@Override
	public void setEnabled(boolean enabled) throws MException {
		if (componentEditor != null && editorEditable) componentEditor.setEnabled(enabled);
		if (componentWizard != null) componentWizard.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() throws MException {
		if (componentEditor != null && componentEditor.isVisible()) return true;
		if (componentWizard != null && componentWizard.isVisible()) return true;
		return false;
	}


	protected abstract void setValue(Object value) throws MException;
	protected abstract Object getValue() throws MException;

	protected void setCaption(String value) throws MException {
		if (componentLabel != null) componentLabel.setCaption(value);
	}

	protected Component create(UiLayout grid) throws MException {
		grid.createRow(this);
		
		return getComponentEditor();
	}

	public abstract Component createEditor();

	public Component getComponentWizard() {
		return componentWizard;
	}

	public void setComponentWizard(Component componentWizard) {
		this.componentWizard = componentWizard;
	}

	public Component getComponentError() {
		return componentError;
	}

	public void setComponentError(Component componentError) {
		this.componentError = componentError;
	}

	public Component getComponentLabel() {
		return componentLabel;
	}

	public void setComponentLabel(Component componentLabel) {
		this.componentLabel = componentLabel;
	}

	public Component getComponentEditor() {
		return componentEditor;
	}

	public void setComponentEditor(Component componentEditor) {
		this.componentEditor = componentEditor;
	}

	public UiLayout getLayout() {
		return null;
	}

	public void setError(String error) {
		Component el = getComponentError();
		if (el == null) return;
		el.setCaption(error);
		el.setVisible(true);
	}

	public void clearError() {
		Component el = getComponentError();
		if (el == null) return;
		el.setCaption("");
		el.setVisible(false);
	}

	public void fieldValueChangedEvent() {
		Component e = getComponentEditor();
		DataSource ds = getForm().getDataSource();
		if (e == null || ds == null) return;
		if (e instanceof AbstractField) {
			Object newValue = ((AbstractField)e).getValue();
			if (getForm().getControl().newValue(this, newValue)) {
				try {
					ds.setObject(this, DataSource.VALUE, newValue );
				} catch (Throwable t) {
					getForm().getControl().newValueError(this, newValue, t);
				}
			}
		}
	}

	public void focusEvent() {
		getForm().getControl().focus(this);
	}
	
}
