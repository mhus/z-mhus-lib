package de.mhus.lib.vaadin.form;

import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;

/**
 * | 0       | 1     | 2      |
 * | Caption | Field | Wizard |
 * | Caption | Field          |
 * 
 * | Error                    |
 * 
 * | Caption                  |
 * | Field                    |
 * | Field           | Wizard |
 * 
 * @author mikehummel
 *
 */
public class UiRow {

	private GridLayout layout;
	private int row;
	private boolean full;
	private boolean wizard;

	public UiRow(GridLayout layout, int row) {
		this.layout = layout;
		this.row = row;
	}
	
	public void setLeft(Component component) {
		if (full) return;
		layout.addComponent(component, 0, row);
	}

	public void setRight(Component component) {
		if (full) return;
		if (wizard)
			layout.addComponent(component, 1, row);
		else
			layout.addComponent(component, 1, row, 2, row);
	}
	
	public void setComponent(Component component) {
		if (!full) return;
		if (wizard)
			layout.addComponent(component, 0, row, 1, row);
		else
			layout.addComponent(component, 0, row, 2, row);
	}

	public boolean isFull() {
		return full;
	}

	public void setFull(boolean full) {
		this.full = full;
	}

	public boolean isWizard() {
		return wizard;
	}

	public void setWizard(Component wizard) {
		this.wizard = wizard != null;
		if (wizard == null) return;
		layout.addComponent(wizard, 2, row );
	}
	
}
