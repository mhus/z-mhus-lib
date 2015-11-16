package de.mhus.lib.vaadin.form;

import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;

import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.DataSource;
import de.mhus.lib.form.Form;
import de.mhus.lib.form.UiComponent;

public abstract class UiVaadin extends UiComponent {

	private Component componentWizard;
	private Component componentError;
	private Component componentLabel;
	private Component componentEditor;
	
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
		if (componentLabel != null) componentLabel.setVisible(visible);
		if (componentError != null) componentError.setVisible(visible);
		if (componentEditor != null) componentEditor.setVisible(visible);
		if (componentWizard != null) componentWizard.setVisible(visible);
	}

	@Override
	public boolean isVisible() throws MException {
		for (Component c : getComponents())
			if (c.isVisible()) return true;
		return false;
	}

	protected abstract void setValue(Object value) throws MException;
	protected abstract void setCaption(String value) throws MException;
	protected abstract Component create(UiLayout grid) throws MException;

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
}
