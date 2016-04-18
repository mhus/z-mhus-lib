package de.mhus.lib.vaadin.form;

import java.io.Serializable;

import com.vaadin.ui.Component;

import de.mhus.lib.errors.MException;
import de.mhus.lib.form.DataSource;
import de.mhus.lib.form.UiComponent;

public abstract class UiLayout extends UiVaadin implements Serializable {

	public abstract void createRow(final UiVaadin c);
	public abstract Component getComponent();
	
	@Override
	public void doRevert() throws MException {
	}
	@Override
	public void doUpdateValue() throws MException {
	}
	@Override
	public void doUpdateMetadata() throws MException {
		
	}
	@Override
	public void setVisible(boolean visible) throws MException {
		getComponent().setVisible(visible);
	}
	@Override
	public boolean isVisible() throws MException {
		return getComponent().isVisible();
	}
	@Override
	public void setEnabled(boolean enabled) throws MException {
		getComponent().setEnabled(enabled);
	}
	@Override
	public void setEditable(boolean editable) throws MException {
		getComponent().setReadOnly(!editable);
	}
	@Override
	public boolean isEnabled() throws MException {
		return !getComponent().isReadOnly();
	}
	@Override
	public void setError(String error) {
	}
	@Override
	public void clearError() {
	}

	@Override
	protected void setValue(Object value) throws MException {
	}

	@Override
	protected Object getValue() throws MException {
		return null;
	}

	@Override
	public Component createEditor() {
		Component ret = getComponent();
		return ret;
	}
	
	public UiLayout getLayout() {
		return this;
	}

	
}
