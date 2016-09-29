package de.mhus.lib.form;

import de.mhus.lib.form.DataSource;
import de.mhus.lib.form.UiComponent;

/**
 * <p>DummyDataSource class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class DummyDataSource extends FormControlAdapter implements DataSource {

	/** {@inheritDoc} */
	public boolean getBoolean(UiComponent component, String name, boolean def) {
		System.out.println("getBoolean " + component.getName() + "." + name);
		return true;
	}

	/** {@inheritDoc} */
	public int getInt(UiComponent component, String name, int def) {
		System.out.println("getInt " + component.getName() + "." + name);
		return def;
	}
	
	/** {@inheritDoc} */
	public String getString(UiComponent component, String name, String def) {
		System.out.println("getString " + component.getName() + "." + name);
		return def;
	}
	
	/** {@inheritDoc} */
	public Object getObject(UiComponent component, String name, Object def) {
		System.out.println("getObject " + component.getName() + "." + name);
		return def;
	}

	/** {@inheritDoc} */
	@Override
	public void setObject(UiComponent component, String name, Object value) {
		System.out.println("setObject " + component.getName() + "." + name + ": " + value);
	}

	/** {@inheritDoc} */
	@Override
	public void focus(UiComponent component) {
		System.out.println("Focus " + component.getName());
		super.focus(component);
	}

	/** {@inheritDoc} */
	@Override
	public boolean newValue(UiComponent component, Object newValue) {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public void reverted(UiComponent component) {
		System.out.println("Reverted " + component.getName());
	}

	/** {@inheritDoc} */
	@Override
	public void attachedForm(Form form) {
		System.out.println("Attached " + form.getClass());
	}

	/** {@inheritDoc} */
	@Override
	public DataSource getNext() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public void valueSet(UiComponent component) {
		System.out.println("valueSet " + component.getName());
	}

}
