package de.mhus.lib.vaadin.form;

import java.util.LinkedList;

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.ComponentAdapter;
import de.mhus.lib.form.ComponentDefinition;
import de.mhus.lib.form.DataSource;
import de.mhus.lib.form.UiComponent;

public class UiLayoutTabs extends UiLayout {

	private static final long serialVersionUID = 1L;
	private TabSheet layout;
	private LinkedList<UiVaadin> tabIndex = new LinkedList<>();
	
	public UiLayoutTabs() {
		this.layout = new TabSheet();
//		layout.setSizeFull();
		layout.setWidth("100%");
	}
	
	@Override
	public void createRow(final UiVaadin c) {
		
		tabIndex .add(c);
		
		//String name = c.getName();
		Component editor = c.createEditor();
		DataSource ds = getForm().getDataSource();
		String caption = c.getCaption(ds);
		
		layout.addTab(editor, caption);
	}
	
	@Override
	public void doRevert() throws MException {
		
		for (UiVaadin entry : tabIndex)
			try {
				entry.doRevert();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		super.doRevert();
	}

	public Component getComponent() {
		return layout;
	}

	public static class Adapter implements ComponentAdapter {

		@Override
		public UiComponent createAdapter(IConfig config) {
			return new UiLayoutTabs();
		}

		@Override
		public ComponentDefinition getDefinition() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
}
