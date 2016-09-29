package de.mhus.lib.form;

import de.mhus.lib.annotations.activator.DefaultImplementation;

/**
 * <p>DataSource interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
@DefaultImplementation(ModelDataSource.class)
public interface DataSource {

	/** Constant <code>ENABLED="enabled"</code> */
	public static final String ENABLED = "enabled";
	/** Constant <code>EDITABLE="enabled"</code> */
	public static final String EDITABLE = "enabled";
	/** Constant <code>VISIBLE="visible"</code> */
	public static final String VISIBLE = "visible";
	/** Constant <code>VALUE=""</code> */
	public static final String VALUE = "";
	/** Constant <code>CAPTION="caption"</code> */
	public static final String CAPTION = "caption";
	/** Constant <code>EDITOR_EDITABLE="editable"</code> */
	public static final String EDITOR_EDITABLE = "editable";
	/** Constant <code>DESCRIPTION="description"</code> */
	public static final String DESCRIPTION = "description";
	/** Constant <code>ITEMS="items"</code> */
	public static final String ITEMS = "items";
	
	/**
	 * <p>getBoolean.</p>
	 *
	 * @param component a {@link de.mhus.lib.form.UiComponent} object.
	 * @param name a {@link java.lang.String} object.
	 * @param def a boolean.
	 * @return a boolean.
	 */
	boolean getBoolean(UiComponent component, String name, boolean def);

	/**
	 * <p>getInt.</p>
	 *
	 * @param component a {@link de.mhus.lib.form.UiComponent} object.
	 * @param name a {@link java.lang.String} object.
	 * @param def a int.
	 * @return a int.
	 */
	int getInt(UiComponent component, String name, int def);

	/**
	 * <p>getString.</p>
	 *
	 * @param component a {@link de.mhus.lib.form.UiComponent} object.
	 * @param name a {@link java.lang.String} object.
	 * @param def a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	String getString(UiComponent component, String name, String def);
	
	/**
	 * <p>getObject.</p>
	 *
	 * @param component a {@link de.mhus.lib.form.UiComponent} object.
	 * @param name a {@link java.lang.String} object.
	 * @param def a {@link java.lang.Object} object.
	 * @return a {@link java.lang.Object} object.
	 */
	Object getObject(UiComponent component, String name, Object def);

	/**
	 * <p>setObject.</p>
	 *
	 * @param component a {@link de.mhus.lib.form.UiComponent} object.
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Object} object.
	 * @throws java.lang.Exception if any.
	 */
	void setObject(UiComponent component, String name, Object value) throws Exception;

	/**
	 * <p>getNext.</p>
	 *
	 * @return a {@link de.mhus.lib.form.DataSource} object.
	 */
	DataSource getNext();
	
}
