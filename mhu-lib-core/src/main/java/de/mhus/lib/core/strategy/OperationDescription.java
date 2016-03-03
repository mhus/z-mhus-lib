package de.mhus.lib.core.strategy;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.MXml;
import de.mhus.lib.core.config.ConfigBuilder;
import de.mhus.lib.core.config.XmlConfig;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsProvider;

/**
 * <p>OperationDescription class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class OperationDescription {

	private static Log log = Log.getLog(OperationDescription.class);
	
	private String id;
	private String title;
	private String group;
	private String form;
	private HashMap<String, Object> parameters;
	
	/**
	 * <p>Constructor for OperationDescription.</p>
	 */
	public OperationDescription() {}
	
	/**
	 * <p>Constructor for OperationDescription.</p>
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 * @param title a {@link java.lang.String} object.
	 */
	public OperationDescription(Class<?> clazz, String title) {
		this(clazz, title, null);
	}
	
	/**
	 * <p>Constructor for OperationDescription.</p>
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 * @param title a {@link java.lang.String} object.
	 * @param form a {@link de.mhus.lib.core.definition.DefRoot} object.
	 */
	public OperationDescription(Class<?> clazz, String title, DefRoot form) {
		this(clazz.getPackage().getName(), clazz.getSimpleName(), title);
		if (form != null)
			setForm(form);
	}
	
	/**
	 * <p>Setter for the field <code>form</code>.</p>
	 *
	 * @param form a {@link de.mhus.lib.core.definition.DefRoot} object.
	 */
	public void setForm(DefRoot form) {
		try {
			form = form.build();
			Document document = MXml.createDocument();
			Element de = document.createElement("root");
			XmlConfig c = new XmlConfig(de);
			new ConfigBuilder().cloneConfig(form, c);
			String formStr = MXml.toString(de, false);
			setForm(formStr);
		} catch (Exception e) {
			log.w("invalid form",group,id,e);
		}
	}

	/**
	 * <p>Constructor for OperationDescription.</p>
	 *
	 * @param group a {@link de.mhus.lib.core.strategy.OperationGroupDescription} object.
	 * @param id a {@link java.lang.String} object.
	 * @param title a {@link java.lang.String} object.
	 */
	public OperationDescription(OperationGroupDescription group, String id, String title) {
		this(group.getGroup(),id,title);
	}
	
	/**
	 * <p>Constructor for OperationDescription.</p>
	 *
	 * @param group a {@link java.lang.String} object.
	 * @param id a {@link java.lang.String} object.
	 * @param title a {@link java.lang.String} object.
	 */
	public OperationDescription(String group, String id, String title) {
		setGroup(group);
		setId(id);
		setTitle(title);
	}
	
	/**
	 * <p>Getter for the field <code>id</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getId() {
		return id;
	}
	/**
	 * <p>Setter for the field <code>id</code>.</p>
	 *
	 * @param id a {@link java.lang.String} object.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * <p>Getter for the field <code>title</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * <p>Setter for the field <code>title</code>.</p>
	 *
	 * @param title a {@link java.lang.String} object.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * <p>Getter for the field <code>group</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getGroup() {
		return group;
	}
	/**
	 * <p>Setter for the field <code>group</code>.</p>
	 *
	 * @param group a {@link java.lang.String} object.
	 */
	public void setGroup(String group) {
		this.group = group;
	}
	/**
	 * <p>Getter for the field <code>form</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getForm() {
		return form;
	}
	/**
	 * <p>Setter for the field <code>form</code>.</p>
	 *
	 * @param form a {@link java.lang.String} object.
	 */
	public void setForm(String form) {
		this.form = form;
	}
	
	/**
	 * <p>getPath.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPath() {
		return group + '.' + id;
	}
	/**
	 * <p>Getter for the field <code>parameters</code>.</p>
	 *
	 * @return a {@link java.util.HashMap} object.
	 */
	public HashMap<String, Object> getParameters() {
		return parameters;
	}
	/**
	 * <p>Setter for the field <code>parameters</code>.</p>
	 *
	 * @param parameters a {@link java.util.HashMap} object.
	 */
	public void setParameters(HashMap<String, Object> parameters) {
		this.parameters = parameters;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (o instanceof OperationDescription) {
			OperationDescription od = (OperationDescription)o;
			return MSystem.equals(group, od.group) && MSystem.equals(id, od.id);
		}
		return super.equals(o);
	}
	
	/**
	 * <p>findTitle.</p>
	 *
	 * @param p a {@link de.mhus.lib.core.util.MNlsProvider} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String findTitle(MNlsProvider p) {
		return MNls.find(p, getTitle());
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return MSystem.toString(this, group, id, parameters);
	}
	
	
}
