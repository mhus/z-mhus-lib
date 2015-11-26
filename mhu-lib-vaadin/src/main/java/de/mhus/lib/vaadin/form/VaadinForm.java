package de.mhus.lib.vaadin.form;

import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.form.ActivatorAdapterProvider;
import de.mhus.lib.form.Form;

public class VaadinForm extends VerticalLayout {

	public enum SHOW {MODEL,YES,NO};
	
	private VaadinFormBuilder builder;
	private Form form;
	private SHOW showInformation = SHOW.MODEL;
	private VaadinUiInformation informationPane;
	private UiLayout layout;
	private Panel formPanel;
	
	public VaadinForm() {}
	
	public VaadinForm(Form form) {
		setForm(form);
	}
	
	
	public void doBuild(MActivator activator) throws Exception {
		if (activator != null)
			form.setAdapterProvider(new ActivatorAdapterProvider(activator));
		doBuild();
	}
	
	public void doBuild() throws Exception {

		if (form.getAdapterProvider() == null)
			form.setAdapterProvider(new DefaultAdapterProvider());

		if (isShowInformation()) {
			informationPane = new VaadinUiInformation();
			getForm().setInformationPane(informationPane);
			addComponent(informationPane);
		}
		if (builder == null)
			builder = new VaadinFormBuilder();
	
		builder.setForm(form);
		builder.doBuild();
		builder.doRevert();
		
		formPanel = new Panel();
		formPanel.setWidth("100%");

		layout = builder.getLayout();
		formPanel.setContent(layout.getComponent());
		
		addComponent(formPanel);
		
	}

	public boolean isShowInformation() {
		return showInformation == SHOW.YES || showInformation == SHOW.MODEL && form.getModel().getBoolean("showInformation", false);
	}

	public void setShowInformation(boolean showInformation) {
		this.showInformation = showInformation ? SHOW.YES : SHOW.NO;
	}

	public void setShowInformation(SHOW showInformation) {
		this.showInformation = showInformation;
	}
	
	public SHOW getShowInformation() {
		return showInformation;
	}
	
	public VaadinFormBuilder getBuilder() {
		return builder;
	}

	public void setBuilder(VaadinFormBuilder builder) {
		this.builder = builder;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

}
