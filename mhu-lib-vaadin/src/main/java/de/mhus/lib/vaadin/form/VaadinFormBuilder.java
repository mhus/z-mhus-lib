package de.mhus.lib.vaadin.form;

import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.form.Form;

public class VaadinFormBuilder {

	private Form form;

	public VaadinFormBuilder(Form form) {
		this.form = form;
	}

	public void doBuild() {
		IConfig model = form.getModel();
		UiLayout layout = new UiLayout();
		build(layout, model);
	}

	private void build(UiLayout layout, IConfig model) {
		
	}
	
}
