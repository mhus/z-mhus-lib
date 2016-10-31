package de.mhus.lib.vaadin.form;

import java.util.Set;
import java.util.TreeSet;

import com.vaadin.ui.Component;
import com.vaadin.ui.TwinColSelect;

import de.mhus.lib.core.MCollection;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.ComponentAdapter;
import de.mhus.lib.form.ComponentDefinition;
import de.mhus.lib.form.DataSource;
import de.mhus.lib.form.Item;
import de.mhus.lib.form.UiComponent;

public class UiOptions extends UiVaadin {

	@Override
	public Component createEditor() {
		TwinColSelect ret = new TwinColSelect();
		ret.setMultiSelect(true);
		ret.setNullSelectionAllowed(true);
		
		ret.setLeftColumnCaption( MNls.find(getForm(), getName() + ".available=Available options") );
        ret.setRightColumnCaption( MNls.find(getForm(), getName() + ".selected=Selected options") );
 
		return ret;
	}

	@Override
	protected void setValue(Object value) throws MException {
		if (value == null) {
			((TwinColSelect)getComponentEditor()).setValue(new TreeSet<String>());
			return;
		}
		if (!(value instanceof Set))
			value = MCollection.toTreeSet(String.valueOf(value).split(","));
		((TwinColSelect)getComponentEditor()).setValue(value);
	}

	@Override
	protected Object getValue() throws MException {
		Set<String> ret = (Set<String>)((TwinColSelect)getComponentEditor()).getValue();
		if (ret == null) return null;
		return ret;
	}

	@Override
	public void doUpdateMetadata() throws MException {
		TwinColSelect cb = (TwinColSelect)getComponentEditor();
		cb.removeAllItems();
		Item[] items = (Item[]) getForm().getDataSource().getObject(this, DataSource.ITEMS, null);
		if (items != null)
			for (Item item : items)
				cb.addItem(item);
	}

	public static class Adapter implements ComponentAdapter {

		@Override
		public UiComponent createAdapter(IConfig config) {
			return new UiOptions();
		}

		@Override
		public ComponentDefinition getDefinition() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
