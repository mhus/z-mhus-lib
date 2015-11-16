package de.mhus.lib.vaadin.form;

import java.util.LinkedList;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Button.ClickEvent;

import de.mhus.lib.form.UiComponent;
import de.mhus.lib.form.UiWizard;

public class UiLayout {

	private GridLayout layout;
	
	public UiLayout(GridLayout layout) {
		this.layout = layout;
	}
	
	public void createRow(final UiVaadin c) {
		final UiWizard wizard = c.getWizard();
		Component e = c.createEditor();
		if (e == null) return;
		
		if (c.isFullSize()) {
			UiRow row1 = createRow();
			row1.setFull(true);
			if (wizard != null) {
				Button b = new Button("W");
				b.addClickListener(new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						wizard.showWizard(c);
					}
				});
				b.setWidth("100%");
				row1.setWizard(b);
				c.setComponentWizard(b);
			}
			
			Label l = new Label();
			
			
			components.add(e);
			row1.setComponent(e);
		}
	}
	
	protected UiRow createRow() {
		int row = layout.getRows();
		layout.setRows(row+1);
		return new UiRow(layout, row);
	}
	
}
