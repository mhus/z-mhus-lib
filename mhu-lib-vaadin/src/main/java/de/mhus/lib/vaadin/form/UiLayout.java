package de.mhus.lib.vaadin.form;

import com.vaadin.ui.GridLayout;

public class UiLayout {

	private GridLayout layout;
	
	public UiLayout(GridLayout layout) {
		this.layout = layout;
	}
	
	public UiRow createRow() {
		int row = layout.getRows();
		layout.setRows(row+1);
		return new UiRow(layout, row);
	}
	
}
