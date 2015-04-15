package de.mhus.lib.vaadin.form2;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;

import de.mhus.lib.core.MString;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.DataConnector;
import de.mhus.lib.form.DataSource;
import de.mhus.lib.form.LayoutComposite;
import de.mhus.lib.form.LayoutElement;
import de.mhus.lib.form.UiElement;

public class UiVaadinComposite extends UiElement {

	protected GridLayout layout;

	@Override
	protected void doConnect() {
	}

	@Override
	protected void doDisconnect() {
	}
	
	public boolean isTransparent() {
		return false;
	}

	@Override
	public void doUpdate(DataConnector data) throws MException {
		if (data.getTaskName().equals(DataSource.CONNECTOR_TASK_ENABLED)) {
			boolean arg = data.getBoolean(layout.isVisible());
			if (equals(arg,layout.isVisible())) return;
			layout.setVisible(data.getBoolean(true));
			return;
		}

	}

	public void createUi(VaadinFormBuilder builder) {
		layout = new GridLayout();
		layout.setColumns(((LayoutComposite)getElement()).getColumns());
		log().i("grid",layout.getColumns());
		layout.setWidth("100%");
		layout.setSpacing(true);
		addToCurrent(builder);
	}
	
	protected void addToCurrent(VaadinFormBuilder builder) {
		builder.addComposite((LayoutComposite)getElement(), layout);
		
	}

	public GridLayout getGrid() {
		return layout;
	}

	@Override
	public void setErrorMessage(String msg) {
		layout.setComponentError(MString.isEmpty(msg) ? null : new UserError(msg));		
	}

	public void addComponent(LayoutElement element, Component component, int col1, int row1, int col2, int row2) {
		layout.addComponent(component, col1, row1, col2, row2);
	}

	public int createRow() {
		if (layout == null) return 0;
		int rows = layout.getRows();
		layout.setRows(rows+1);
		return rows;
	}

}
