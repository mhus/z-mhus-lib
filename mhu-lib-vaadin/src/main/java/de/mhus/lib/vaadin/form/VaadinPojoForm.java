package de.mhus.lib.vaadin.form;

import de.mhus.lib.form.PojoDataSource;
import de.mhus.lib.form.PojoForm;
import de.mhus.lib.form.PojoProvider;

public class VaadinPojoForm<T> extends VaadinForm implements PojoProvider {

	private T pojo;

	public VaadinPojoForm(T pojo) throws Exception {
		setPojo(pojo);
		setForm(new PojoForm(this) );
	}
	
	public T getPojo() {
		return pojo;
	}

	public void setPojo(T pojo) {
		this.pojo = pojo;
		if (getBuilder() != null)
			getBuilder().doUpdateValues();
	}
	
}
