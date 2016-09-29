package de.mhus.lib.vaadin.form;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusNotifier;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Button.ClickEvent;

import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.form.ComponentAdapter;
import de.mhus.lib.form.ComponentDefinition;
import de.mhus.lib.form.UiComponent;
import de.mhus.lib.form.UiWizard;

/**
 * | 0       | 1     | 2      | 3       | 4     | 5      |
 * 
 * 50 x 50
 * | Caption | Field | Wizard | Caption | Field | Wizard |
 * | Caption | Field          | Caption | Field          |
 * |         | Error          |         | Error          |
 * 
 * 50 x 50 full
 * | Caption                  | Caption                  |
 * | Field                    | Field                    |
 * | Field           | Wizard | Field           | Wizard |
 * | Error                    | Error                    |
 * 
 * 100
 * | Caption | Field                            | Wizard |
 * | Caption | Field                                     |
 * |         | Error                                     |
 * 
 * 100 full
 * | Caption                                             |
 * | Field                                               |
 * | Field                                      | Wizard |
 * | Error                                               |
 * 
 * @author mikehummel
 *
 */

public class UiLayout50x50 extends UiLayout {

	private GridLayout layout;
	private int rows;
	private UiSlot slot;
	
	public UiLayout50x50() {
		this.layout = new GridLayout(6,1);
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setHideEmptyRowsAndColumns(true);
		layout.setColumnExpandRatio(0, 0.3f);
		layout.setColumnExpandRatio(1, 0.7f);
		layout.setColumnExpandRatio(2, 0);
		layout.setColumnExpandRatio(3, 0.3f);
		layout.setColumnExpandRatio(4, 0.7f);
		layout.setColumnExpandRatio(5, 0);
//		layout.setSizeFull();
		layout.setWidth("100%");
		rows = 0;
	}
	
	@Override
	public void createRow(final UiVaadin c) {
		final UiWizard wizard = c.getWizard();
		Component e = c.createEditor();
		if (e == null) return;
		c.setComponentEditor(e);
		
		e.setWidth("100%");
		//e.setHeight("30px");
		
		c.setListeners();

		if (slot == null) {
			slot = new UiSlot();
		} else {
			if (c.getConfig().getInt("columns", 1) > 1)
				slot = new UiSlot();
		}
		
		slot.add(c, wizard, e);
		
		if (slot.isFull()) slot = null;
		
	}
	
	protected UiRow createRow(int col, int size, int row) {
		return new UiRow(layout, col, row, size);
	}

	public Component getComponent() {
		return layout;
	}

	public class UiRow {

		private GridLayout layout;
		private int row;
		private boolean full;
		private boolean wizard;
		private int col;
		private int size;

		public UiRow(GridLayout layout, int col, int row, int size) {
			this.layout = layout;
			this.row = row;
			this.col = col;
			this.size = size;
		}
		
		public void setLeft(Component component) {
			if (full) return;
			layout.addComponent(component, col, row);
		}

		public void setRight(Component component) {
			if (full) return;
			if (wizard)
				layout.addComponent(component, col+1, row, col+size-1, row);
			else
				layout.addComponent(component, col+1, row, col+size, row);
		}
		
		public void setComponent(Component component) {
			if (!full) {
				setRight(component);
				return;
			}
			if (wizard)
				layout.addComponent(component, 0, row, col+size-1, row);
			else
				layout.addComponent(component, 0, row, col+size, row);
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
			layout.addComponent(wizard, col+size, row );
		}
		
	}

	private class UiSlot {

		private int startRow;
		private int col;

		public UiSlot() {
			startRow = rows;
			rows+=3;
			layout.setRows(rows);
			col = 0;
		}
		
		public void add(final UiVaadin c, final UiWizard wizard, final Component e) {

			int size = 2;
			if (c.getConfig().getInt("columns", 1) > 1) {
				size = 5;
			}
			UiRow row1 = createRow(col, size, startRow);
			UiRow row2 = createRow(col, size, startRow+1);
			UiRow row3 = createRow(col, size, startRow+2);
			
			if (c.isFullSize()) {
				
				row1.setFull(true);
				
				Label l = new Label();
				l.setStyleName("form-label");
				l.setWidth("100%");
				c.setComponentLabel(l);
				row1.setComponent(l);

				row2.setFull(true);
				
				if (wizard != null) {
					Button b = new Button();
					b.setIcon(FontAwesome.COG);
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
				
				row2.setComponent(e);
				
			} else {
				
				Label l = new Label();
				l.setWidth("100%");
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
			
			row3.setFull(c.isFullSize());
			Label le = new Label();
			le.setStyleName("error-text");
			le.setWidth("100%");
			c.setComponentError(le);
			row3.setComponent(le);
			
			col+=1 + size;
		}

		public boolean isFull() {
			return col >= 6;
		}
		
	}
	
	public static class Adapter implements ComponentAdapter {

		@Override
		public UiComponent createAdapter(IConfig config) {
			return new UiLayout50x50();
		}

		@Override
		public ComponentDefinition getDefinition() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
}
