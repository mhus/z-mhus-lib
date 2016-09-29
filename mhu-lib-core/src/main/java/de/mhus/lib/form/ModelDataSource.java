package de.mhus.lib.form;

import de.mhus.lib.core.util.MNls;

/**
 * <p>ModelDataSource class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class ModelDataSource implements DataSource {

	private DataSource next;
	
	/**
	 * <p>Constructor for ModelDataSource.</p>
	 */
	public ModelDataSource() {
	}
	
	/**
	 * <p>Constructor for ModelDataSource.</p>
	 *
	 * @param next a {@link de.mhus.lib.form.DataSource} object.
	 */
	public ModelDataSource(DataSource next) {
		setNext(next);
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean getBoolean(UiComponent component, String name, boolean def) {
		if (isHandled(name) && component.getConfig().isProperty(name))
			return component.getConfig().getBoolean(name, def);
		
		if (next != null)
			return next.getBoolean(component, name, def);
		
		return def;
	}

	private boolean isHandled(String name) {
		switch(name) {
		case DataSource.CAPTION:
		case DataSource.DESCRIPTION:
		case DataSource.EDITOR_EDITABLE:
		case DataSource.ENABLED:
		case DataSource.VISIBLE:
			return true;
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public int getInt(UiComponent component, String name, int def) {
		if (isHandled(name) && component.getConfig().isProperty(name))
			return component.getConfig().getInt(name, def);
		
		if (next != null)
			return next.getInt(component, name, def);
		
		return def;
	}

	/** {@inheritDoc} */
	@Override
	public String getString(UiComponent component, String name, String def) {
		if (isHandled(name) && component.getConfig().isProperty(name)) {
			
			String expression = component.getConfig().getString(name, def);
			return MNls.find(component.getForm(), expression );
		}
		if (next != null)
			return next.getString(component, name, def);
		
		return def;
	}

	/** {@inheritDoc} */
	@Override
	public Object getObject(UiComponent component, String name, Object def) {
		if (isHandled(name) && component.getConfig().isProperty(name))
			return component.getConfig().getString(name, def != null ? String.valueOf( def ) : null );
		
		if (next != null)
			return next.getObject(component, name, def);
		
		return def;
	}

	/** {@inheritDoc} */
	@Override
	public void setObject(UiComponent component, String name, Object value) throws Exception {
		if (next != null)
			next.setObject(component, name, value);
	}
	

	/**
	 * <p>Getter for the field <code>next</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.form.DataSource} object.
	 */
	public DataSource getNext() {
		return next;
	}

	/**
	 * <p>Setter for the field <code>next</code>.</p>
	 *
	 * @param chain a {@link de.mhus.lib.form.DataSource} object.
	 */
	public void setNext(DataSource chain) {
		this.next = chain;
	}

}
