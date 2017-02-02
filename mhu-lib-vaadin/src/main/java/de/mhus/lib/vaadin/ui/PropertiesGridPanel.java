package de.mhus.lib.vaadin.ui;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsProvider;

public class PropertiesGridPanel extends VerticalLayout implements MNlsProvider {

	private static final long serialVersionUID = 1L;
	private GridLayout grid;
	private MNls nls;

	public PropertiesGridPanel() {
		grid = new GridLayout(2, 1);
		grid.setWidth("100%");
		addComponent(grid);
		setExpandRatio(grid, 0);
	}
	
	public void clear() {
		grid.removeAllComponents();
		grid.setRows(1);
		grid.addComponent(new Label("<strong>" + MNls.find(this,"attribute=Attribute") +"</strong>",ContentMode.HTML));
		grid.addComponent(new Label("<strong>" + MNls.find(this,"value=Value") + "</strong>",ContentMode.HTML));
	}
	
	public void addAttribute(String name, String value) {
		grid.setRows(grid.getRows()+1);
		grid.addComponent(new Label(name));
		grid.addComponent(new Label(value));
	}
	
	@Override
	public MNls getNls() {
		return nls;
	}

	public void setNls(MNls nls) {
		this.nls = nls;
	}
	
}
