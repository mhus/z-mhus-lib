package de.mhus.lib.vaadin.form;

import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;

import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.DataSource;
import de.mhus.lib.form.Form;
import de.mhus.lib.form.UiComponent;

public abstract class UiVaadin extends UiComponent {

	public UiVaadin(Form form, IConfig config) {
		super(form, config);
	}

	@Override
	public void doUpdate() throws MException {
		DataSource ds = getForm().getDataSource();
		setEnabled( ds.getBoolean(this, DataSource.ENABLED, true) );
		setVisible( ds.getBoolean(this, DataSource.VISIBLE, true) );
		setValue(ds.getObject(this, DataSource.VALUE, null));
		setCaption(ds.getString(this, DataSource.CAPTION, getName()));
	}

	public String getName() throws MException {
		return getConfig().getName();
	}

	@Override
	public void setVisible(boolean visible) throws MException {
		for (Component c : getComponents())
			c.setVisible(visible);
	}

	@Override
	public boolean isVisible() throws MException {
		for (Component c : getComponents())
			if (c.isVisible()) return true;
		return false;
	}

	protected abstract Component[] getComponents() throws MException;
	protected abstract void setValue(Object value) throws MException;
	protected abstract void setCaption(String value) throws MException;
	protected abstract Component create(UiLayout grid) throws MException;

}
