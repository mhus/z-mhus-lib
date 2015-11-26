package de.mhus.lib.vaadin.form;

import de.mhus.lib.form.PojoForm;

public class VaadinPojoForm extends VaadinForm {

	private Object pojo;

	public VaadinPojoForm(Object pojo) throws Exception {
		setForm(new PojoForm(pojo) );
		setPojo(pojo);
	}
	
	public Object getPojo() {
		return pojo;
	}

	public void setPojo(Object pojo) {
		this.pojo = pojo;
	}
	
}
