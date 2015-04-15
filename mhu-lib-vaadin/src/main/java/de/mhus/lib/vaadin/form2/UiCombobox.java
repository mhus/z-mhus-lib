package de.mhus.lib.vaadin.form2;

import java.util.Map;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.ComboBox;

import de.mhus.lib.errors.MException;
import de.mhus.lib.form.DataConnector;
import de.mhus.lib.form.DataSource;

public class UiCombobox extends UiText {

	private IndexedContainer container;
	private Object value;

	@Override
	protected void setValueToField(Object arg) {
		value = arg;
		ComboBox editor = (ComboBox)field;
		if (!editor.containsId(arg))
			editor.unselect(editor.getValue());
		else
			editor.select(arg);	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void doUpdate(DataConnector data) throws MException {

		if (data.getTaskName().equals(DataSource.CONNECTOR_TASK_OPTIONS)) {
			Object arg = data.getObject();
			container.removeAllItems();
			if (arg == null) return;
			
			if (field.getValue() != null) value = field.getValue();
			
			if (arg instanceof String) {
				arg = ((String) arg).split(",");
			} 
			if (arg instanceof String[]) {
				String[][] next = new String[((String[])arg).length][];
				for (int i = 0; i < ((String[])arg).length; i++) {
					next[i] = ((String[])arg)[i].split("=");
				}
				arg = next;
			}
			if (arg instanceof String[][]) {
				for (String[] pair : ((String[][])arg)) {
					if (pair.length == 2) {
						Item item = container.addItem(pair[0]);
						item.getItemProperty("title").setValue(pair[1]);
					}
				}
			}
			if (arg instanceof Map<?, ?>) {
				for (Map.Entry<?, ?> pair : ((Map<?,?>)arg).entrySet()) {
					Item item = container.addItem(String.valueOf(pair.getKey()));
					item.getItemProperty("title").setValue(String.valueOf(pair.getValue()));
				}
			}
			
			setValueToField(value);
			return;
		}

		super.doUpdate(data);
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	protected AbstractField createTextField() {
		ComboBox out = new ComboBox();
		out.setItemCaptionPropertyId("title");
        out.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
        out.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS);
        out.setNullSelectionAllowed(false); //TODO 
        container = new IndexedContainer();
//        container.addContainerProperty("id", String.class, "");
        container.addContainerProperty("title", String.class, "");
        out.setContainerDataSource(container);
        
		return out;
	}


}
