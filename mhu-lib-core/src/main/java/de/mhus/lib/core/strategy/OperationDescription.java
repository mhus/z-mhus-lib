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

public class OperationDescription {

	private static Log log = Log.getLog(OperationDescription.class);
	
	private String id;
	private String title;
	private String group;
	private String form;
	private HashMap<String, Object> parameters;
	
	public OperationDescription() {}
	
	public OperationDescription(Class<?> clazz, String title) {
		this(clazz, title, null);
	}
	
	public OperationDescription(Class<?> clazz, String title, DefRoot form) {
		this(clazz.getPackage().getName(), clazz.getSimpleName(), title);
		if (form != null)
			setForm(form);
	}
	
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

	public OperationDescription(OperationGroupDescription group, String id, String title) {
		this(group.getGroup(),id,title);
	}
	
	public OperationDescription(String group, String id, String title) {
		setGroup(group);
		setId(id);
		setTitle(title);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
	}
	
	public String getPath() {
		return group + '.' + id;
	}
	public HashMap<String, Object> getParameters() {
		return parameters;
	}
	public void setParameters(HashMap<String, Object> parameters) {
		this.parameters = parameters;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (o instanceof OperationDescription) {
			OperationDescription od = (OperationDescription)o;
			return MSystem.equals(group, od.group) && MSystem.equals(id, od.id);
		}
		return super.equals(o);
	}
	
	public String findTitle(MNlsProvider p) {
		return MNls.find(p, getTitle());
	}
	
	@Override
	public String toString() {
		return MSystem.toString(this, group, id, parameters);
	}
	
	
}
