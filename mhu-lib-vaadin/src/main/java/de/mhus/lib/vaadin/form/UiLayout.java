package de.mhus.lib.vaadin.form;

import com.vaadin.ui.Component;

import de.mhus.lib.errors.MException;
import de.mhus.lib.form.UiComponent;

public abstract class UiLayout extends UiComponent {

	public abstract void createRow(final UiVaadin c);
	public abstract Component getComponent();
	
	@Override
	public void doRevert() throws MException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void doUpdateValue() throws MException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void doUpdateMetadata() throws MException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setVisible(boolean visible) throws MException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean isVisible() throws MException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void setEnabled(boolean enabled) throws MException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean isEnabled() throws MException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void setError(String error) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void clearError() {
		// TODO Auto-generated method stub
		
	}

}
