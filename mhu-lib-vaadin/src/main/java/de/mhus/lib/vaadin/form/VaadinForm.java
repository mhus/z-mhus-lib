package de.mhus.lib.vaadin.form;

import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.form.Form;

public class VaadinForm extends VerticalLayout {

	private VaadinFormBuilder builder;
	private Form form;
	private boolean showInformation = true;
	private UiInformation informationPane;
	private UiLayout layout;

	
	
	public void doBuild() throws Exception {

		if (showInformation) {
			informationPane = new UiInformation();
			addComponent(informationPane);
		}
		builder = new VaadinFormBuilder(form);
		builder.doBuild();
		
		
		layout = builder.getLayout();
		addComponent(layout.getComponent());
	}

	public boolean isShowInformation() {
		return showInformation;
	}

	public void setShowInformation(boolean showInformation) {
		this.showInformation = showInformation;
	}

}
