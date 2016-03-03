package de.mhus.lib.form.binding;

import java.io.IOException;
import java.util.Set;

import de.mhus.lib.core.pojo.DefaultFilter;
import de.mhus.lib.core.pojo.PojoAttribute;
import de.mhus.lib.core.pojo.PojoModel;
import de.mhus.lib.core.pojo.PojoParser;
import de.mhus.lib.core.util.ArraySet;
import de.mhus.lib.errors.MRuntimeException;
import de.mhus.lib.form.DataSource;

/**
 * <p>PojoDataSource class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class PojoDataSource extends DataSource {

	private Object pojo;
	private PojoModel model;

	/**
	 * <p>Constructor for PojoDataSource.</p>
	 */
	public PojoDataSource() {
	}

	/**
	 * <p>Constructor for PojoDataSource.</p>
	 *
	 * @param pojo a {@link java.lang.Object} object.
	 */
	public PojoDataSource(Object pojo) {
		this(pojo, null);
	}
	
	/**
	 * <p>Constructor for PojoDataSource.</p>
	 *
	 * @param pojo a {@link java.lang.Object} object.
	 * @param model a {@link de.mhus.lib.core.pojo.PojoModel} object.
	 */
	public PojoDataSource(Object pojo, PojoModel model) {
		this.pojo = pojo;
		if (model != null) {
			this.model = model;
		} else
			setPojoObject(pojo);
	}
		
	/**
	 * <p>setPojoObject.</p>
	 *
	 * @param pojo a {@link java.lang.Object} object.
	 */
	public void setPojoObject(Object pojo) {
		this.pojo = pojo;
		if (pojo != null && model == null) {
//			if (pojo instanceof FormInjection)
//				((FormInjection)pojo).setForm(getF)
			model = new PojoParser().parse(pojo).filter(new DefaultFilter()).getModel();
			for (String key : model.getAttributeNames())
				fireValueChanged(key);
		}
		if (pojo != null) {
			for (String key : model.getAttributeNames())
				fireValueChanged(key);
		}
	}
	
	/**
	 * <p>getPojoObject.</p>
	 *
	 * @return a {@link java.lang.Object} object.
	 */
	public Object getPojoObject() {
		return pojo;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setProperty(String key, Object value) {
		setPropertyData(key, value);
		if (isConnected()) fireValueChanged(key);
	}

	/** {@inheritDoc} */
	@Override
	public void setPropertyData(String name, Object value) {
		if (model != null) {
			@SuppressWarnings("unchecked")
			PojoAttribute<Object> attr = model.getAttribute(name);
			if (attr == null) throw new MRuntimeException("attribute not found",name);
			try {
				attr.set(pojo,value);
			} catch (IOException e) {
				throw new MRuntimeException(name,e);
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public Object getProperty(String name) {
		if (model != null) {
			@SuppressWarnings("unchecked")
			PojoAttribute<Object> attr = model.getAttribute(name);
			if (attr == null) return null;
			try {
				return attr.get(pojo);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isProperty(String name) {
		if (model != null) {
			PojoAttribute<?> attr = model.getAttribute(name);
			return attr != null;
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void removeProperty(String key) {
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEditable() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public Set<String> keys() {
		if (model != null) {
			return new ArraySet<String>(model.getAttributeNames());
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isPropertyPossible(String name) {
		return isProperty(name);
	}

}
