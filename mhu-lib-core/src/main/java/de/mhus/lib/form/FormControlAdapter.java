package de.mhus.lib.form;

/**
 * <p>FormControlAdapter class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class FormControlAdapter implements FormControl {

	/** {@inheritDoc} */
	@Override
	public void focus(UiComponent component) {
		component.clearError();
		UiInformation info = component.getForm().getInformationPane();
		DataSource ds = component.getForm().getDataSource();
		if (info == null || ds == null) return;
		info.setInformation(ds.getString(component, DataSource.CAPTION, component.getName()), ds.getString(component, DataSource.DESCRIPTION, ""));
	}

	/** {@inheritDoc} */
	@Override
	public boolean newValue(UiComponent component, Object newValue) {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public void reverted(UiComponent component) {
	}

	/** {@inheritDoc} */
	@Override
	public void attachedForm(Form form) {
		
	}

	/** {@inheritDoc} */
	@Override
	public void newValueError(UiComponent component, Object newValue, Throwable t) {
		if (t == null) {
			component.clearError();
			return;
		}
		//TODO Special NLS enabled exception
		component.setError(t.getMessage());
	}

	/** {@inheritDoc} */
	@Override
	public void valueSet(UiComponent component) {
		
	}

}
