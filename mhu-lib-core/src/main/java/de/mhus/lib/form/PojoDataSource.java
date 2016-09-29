package de.mhus.lib.form;

import java.io.IOException;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.pojo.MPojo;
import de.mhus.lib.core.pojo.PojoModel;

/**
 * <p>PojoDataSource class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class PojoDataSource extends MLog implements DataSource {

	private PojoProvider pojo;
	private PojoModel model;

	/**
	 * <p>Constructor for PojoDataSource.</p>
	 *
	 * @param pojo a {@link de.mhus.lib.form.PojoProvider} object.
	 */
	public PojoDataSource(PojoProvider pojo) {
		this.pojo = pojo;
		model = MPojo.getDefaultModelFactory().createPojoModel(pojo.getPojo().getClass());
	}

	/** {@inheritDoc} */
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

	/**
	 * <p>getName.</p>
	 *
	 * @param component a {@link de.mhus.lib.form.UiComponent} object.
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String getName(UiComponent component, String name) {
		String ret = (component.getName() + name).toLowerCase();
		return ret;
	}

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public void setObject(UiComponent component, String name, Object value) throws IOException {
		log().t("setObject",component,name,value);
		model.getAttribute(getName(component,name)).set(pojo.getPojo(), value);
	}

	/** {@inheritDoc} */
	@Override
	public DataSource getNext() {
		return null;
	}

}
