package de.mhus.lib.core.strategy;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.MXml;
import de.mhus.lib.core.config.ConfigBuilder;
import de.mhus.lib.core.config.XmlConfig;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsProvider;
import de.mhus.lib.core.util.Nls;
import de.mhus.lib.core.util.ParameterDefinitions;

public class OperationDescription implements MNlsProvider, Nls {

	private static Log log = Log.getLog(OperationDescription.class);
	
	private String id;
	private String title;
	private String group;
	private String form;
	private HashMap<String, Object> parameters;

	private ParameterDefinitions parameterDef;

	private MNls nls;
	private MNlsProvider nlsProvider;
	
	public OperationDescription() {}
	
	public OperationDescription(Class<?> clazz, MNlsProvider nlsProvider, String title) {
		this(clazz, nlsProvider, title, null);
	}
	
	public OperationDescription(Operation owner, String title, DefRoot form) {
		this(owner.getClass(), owner, title, form);
	}
	
	public OperationDescription(Class<?> clazz, MNlsProvider nlsProvider, String title, DefRoot form) {
		this(clazz.getPackage().getName(), clazz.getSimpleName(), nlsProvider, title);
		if (form != null)
			setForm(form);
	}
	
	public void setForm(DefRoot form) {
		try {
			form = form.build();
			parameterDef = ParameterDefinitions.create(form);
			Document document = MXml.createDocument();
			Element de = document.createElement("root");
			XmlConfig c = new XmlConfig(de);
			new ConfigBuilder().cloneConfig(form, c);
			String formStr = MXml.toString(de, false);
			this.form = formStr;
		} catch (Exception e) {
			log.w("invalid form",group,id,e);
		}
	}
	
	public ParameterDefinitions getParameterDefinitions() {
		return parameterDef;
	}

	public OperationDescription(OperationGroupDescription group, String id, MNlsProvider nlsProvider, String title ) {
		this(group.getGroup(),id,nlsProvider, title);
	}
	
	public OperationDescription(String group, String id, MNlsProvider nlsProvider, String title) {
		this.id = id;
		this.group = group;
		this.nlsProvider = nlsProvider;
		this.title = title;
	}
	
	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}
	
	public String getGroup() {
		return group;
	}

	public String getForm() {
		return form;
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
	
	@Override
	public String toString() {
		return MSystem.toString(this, group, id, parameters);
	}
	
	@Override
	public MNls getNls() {
		if (nls == null)
			nls = nlsProvider.getNls();
		return nls;
	}

	@Override
	public String nls(String text) {
		return MNls.find(this, text);
	}

	public String getCaption() {
		return nls("caption=" + getTitle());
	}
	
}
