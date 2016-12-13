package de.mhus.lib.vaadin.form;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.ComponentAdapter;
import de.mhus.lib.form.ComponentDefinition;
import de.mhus.lib.form.DataSource;
import de.mhus.lib.form.Item;
import de.mhus.lib.form.UiComponent;

public class UiCombobox extends UiVaadin {

	@Override
	public Component createEditor() {
		ComboBox ret = new ComboBox();
		ret.setNullSelectionAllowed(false);
		ret.setTextInputAllowed(false);
		return ret;
	}

	@Override
	protected void setValue(Object value) throws MException {
		((ComboBox)getComponentEditor()).setValue(MCast.toString(value));
	}

	@Override
	protected Object getValue() throws MException {
		Item ret = (Item)((ComboBox)getComponentEditor()).getValue();
		if (ret == null) return null;
		return ret.getKey();
	}

	@Override
	public void doUpdateMetadata() throws MException {
		ComboBox cb = (ComboBox)getComponentEditor();
		cb.removeAllItems();
		Item[] items = (Item[]) getForm().getDataSource().getObject(this, DataSource.ITEMS, null);
		if (items != null)
			for (Item item : items)
				cb.addItem(item);
	}

	public static class Adapter implements ComponentAdapter {

		@Override
		public UiComponent createAdapter(IConfig config) {
			return new UiCombobox();
		}

		@Override
		public ComponentDefinition getDefinition() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
