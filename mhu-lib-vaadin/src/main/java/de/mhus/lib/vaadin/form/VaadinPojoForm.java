package de.mhus.lib.vaadin.form;

import java.util.Locale;

import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.form.ComponentAdapterProvider;
import de.mhus.lib.form.Form;
import de.mhus.lib.form.PojoDataSource;
import de.mhus.lib.form.PojoForm;

public class VaadinPojoForm {

	private AbstractLayout informationPane;
	private Object pojo;
	private VaadinFormBuilder builder;
	private Locale locale;
	private ComponentAdapterProvider adapterProvider;
	private Form form;

	public void setPojo(Object pojo) {
		this.pojo = pojo;
	}

	public void setInformationContainer(AbstractLayout informationPane) {
		this.informationPane = informationPane;
	}

	public void doBuild(AbstractLayout panel, MActivator activator) throws Exception {
		
		form = new PojoForm(locale, adapterProvider, pojo);
		builder = new VaadinFormBuilder(form);
		builder.setInformationPane(informationPane);
		
	}

	public Object getPojo() {
		return pojo;
	}

	public void setEnabled(boolean b) {
		// TODO Auto-generated method stub
		
	}

}
