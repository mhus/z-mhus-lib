package de.mhus.lib.form;

import java.io.IOException;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.pojo.MPojo;
import de.mhus.lib.core.pojo.PojoModel;

public class PojoDataSource extends MLog implements DataSource {

	private PojoProvider pojo;
	private PojoModel model;

	public PojoDataSource(PojoProvider pojo) {
		this.pojo = pojo;
		model = MPojo.getDefaultModelFactory().createPojoModel(pojo.getPojo().getClass());
	}

	@Override
	public boolean getBoolean(UiComponent component, String name, boolean def) {
		try {
			log().t("getBoolean",component,name,def);
			return (boolean)model.getAttribute(getName(component,name)).get(pojo.getPojo());
		} catch (Throwable e) {
			log().t(e);
		}
		return def;
	}

	protected String getName(UiComponent component, String name) {
		String ret = (component.getName() + name).toLowerCase();
		return ret;
	}

	@Override
	public int getInt(UiComponent component, String name, int def) {
		try {
			log().t("getInt",component,name,def);
			return (int) model.getAttribute(getName(component,name)).get(pojo.getPojo());
		} catch (Throwable e) {
			log().t(e);
		}
		return def;
	}

	@Override
	public String getString(UiComponent component, String name, String def) {
		try {
			log().t("getString",component,name,def);
			String ret = (String) model.getAttribute(getName(component,name)).get(pojo.getPojo());
			if (ret == null) return def;
			return ret;
		} catch (Throwable e) {
			log().t(e);
		}
		return def;
	}

	@Override
	public Object getObject(UiComponent component, String name, Object def) {
		try {
			log().t("getObject",component,name,def);
			Object ret = model.getAttribute(getName(component,name)).get(pojo.getPojo());
			if (ret == null) return def;
			return ret;
		} catch (Throwable e) {
			log().t(e);
		}
		return def;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setObject(UiComponent component, String name, Object value) throws IOException {
		log().t("setObject",component,name,value);
		model.getAttribute(getName(component,name)).set(pojo.getPojo(), value);
	}

	@Override
	public DataSource getNext() {
		return null;
	}

}
