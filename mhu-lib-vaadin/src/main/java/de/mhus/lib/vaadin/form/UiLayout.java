package de.mhus.lib.vaadin.form;

import java.util.LinkedList;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Button.ClickEvent;

import de.mhus.lib.form.UiComponent;
import de.mhus.lib.form.UiWizard;

public class UiLayout {

	private GridLayout layout;
	
	public UiLayout() {
		this.layout = new GridLayout(3,0);
	}
	
	public void createRow(final UiVaadin c) {
		final UiWizard wizard = c.getWizard();
		Component e = c.createEditor();
		if (e == null) return;
		
		if (c.isFullSize()) {
			UiRow row1 = createRow();
			row1.setFull(true);
			
			Label l = new Label();
			c.setComponentLabel(l);
			row1.setComponent(l);

			UiRow row2 = createRow();
			row2.setFull(true);
			
			if (wizard != null) {
				Button b = new Button("W");
				b.addClickListener(new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						wizard.showWizard(c);
					}
				});
				b.setWidth("100%");
				row2.setWizard(b);
				c.setComponentWizard(b);
			}
			
			c.setComponentEditor(e);
			row2.setComponent(e);
			
		} else {
			
			UiRow row1 = createRow();
			
			Label l = new Label();
			c.setComponentLabel(l);
			row1.setLeft(l);

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

			row1.setRight(e);
			
		}
		
		UiRow row3 = createRow();
		row3.setFull(true);
		Label le = new Label();
		c.setComponentError(le);
		row3.setComponent(le);
		
	}
	
	protected UiRow createRow() {
		int row = layout.getRows();
		layout.setRows(row+1);
		return new UiRow(layout, row);
	}
	
}
