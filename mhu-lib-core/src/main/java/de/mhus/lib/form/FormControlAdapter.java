package de.mhus.lib.form;

import de.mhus.lib.core.util.MNls;

public class FormControlAdapter implements FormControl {

	@Override
	public void focus(UiComponent component) {
		component.clearError();
		UiInformation info = component.getForm().getInformationPane();
		DataSource ds = component.getForm().getDataSource();
		if (info == null || ds == null) return;
		info.setInformation(
				MNls.find(component.getForm(), ds.getString(component, DataSource.CAPTION, component.getConfigString(DataSource.CAPTION,  component.getName()))), 
				MNls.find(component.getForm(), ds.getString(component, DataSource.DESCRIPTION, component.getConfigString(DataSource.DESCRIPTION,  "")) ));
	}

	@Override
	public boolean newValue(UiComponent component, Object newValue) {
		return true;
	}

	@Override
	public void reverted(UiComponent component) {
	}

	@Override
	public void attachedForm(MForm form) {
		
	}

	@Override
	public void newValueError(UiComponent component, Object newValue, Throwable t) {
		if (t == null) {
			component.clearError();
			return;
		}
		//TODO Special NLS enabled exception
		component.setError(t.getMessage());
	}

	@Override
	public void valueSet(UiComponent component) {
		
	}

}
