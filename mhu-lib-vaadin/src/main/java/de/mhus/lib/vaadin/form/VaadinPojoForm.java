package de.mhus.lib.vaadin.form;

import de.mhus.lib.form.PojoDataSource;
import de.mhus.lib.form.PojoForm;
import de.mhus.lib.form.PojoProvider;

public class VaadinPojoForm extends VaadinForm implements PojoProvider {

	private Object pojo;

	public VaadinPojoForm(Object pojo) throws Exception {
		setPojo(pojo);
		setForm(new PojoForm(this) );
	}
	
	public Object getPojo() {
		return pojo;
	}

	public void setPojo(Object pojo) {
		this.pojo = pojo;
		if (getBuilder() != null)
			getBuilder().doUpdateValues();
	}
	
}
