package de.mhus.lib.form;

public class FormControlAdapter implements FormControl {

	@Override
	public void focus(UiComponent component) {
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

}
