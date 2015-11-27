package de.mhus.lib.form;

public class FormControlAdapter implements FormControl {

	@Override
	public void focus(UiComponent component) {
		component.clearError();
		UiInformation info = component.getForm().getInformationPane();
		DataSource ds = component.getForm().getDataSource();
		if (info == null || ds == null) return;
		info.setInformation(ds.getString(component, DataSource.CAPTION, component.getName()), ds.getString(component, DataSource.DESCRIPTION, ""));
	}

	@Override
	public boolean newValue(UiComponent component, Object newValue) {
		return true;
	}

	@Override
	public void reverted(UiComponent component) {
	}

	@Override
	public void attachedForm(Form form) {
		
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

}
