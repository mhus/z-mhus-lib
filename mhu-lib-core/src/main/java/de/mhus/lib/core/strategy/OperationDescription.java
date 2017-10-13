package de.mhus.lib.core.strategy;

import java.util.HashMap;

import de.mhus.lib.basics.Versioned;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsProvider;
import de.mhus.lib.core.util.Nls;
import de.mhus.lib.core.util.ParameterDefinitions;
import de.mhus.lib.core.util.Version;

public class OperationDescription implements MNlsProvider, Nls, Versioned {

	private static Log log = Log.getLog(OperationDescription.class);
	
	private String id;
	private String title;
	private String group;
	private DefRoot form;
	private HashMap<String, Object> parameters;

	private ParameterDefinitions parameterDef;

	private MNls nls;
	private MNlsProvider nlsProvider;

	private Version version;
	
	public OperationDescription() {}
	
	public OperationDescription(Class<?> clazz, MNlsProvider nlsProvider, String version, String title) {
		this(clazz, nlsProvider, new Version(version), title, null);
	}
	
	public OperationDescription(Class<?> clazz, MNlsProvider nlsProvider, Version version, String title) {
		this(clazz, nlsProvider, version, title, null);
	}
	
	public OperationDescription(Class<?> clazz, MNlsProvider nlsProvider, String title) {
		this(clazz, nlsProvider, new Version(null), title, null);
	}
	
	public OperationDescription(Operation owner, Version version, String title, DefRoot form) {
		this(owner.getClass(), owner, version, title, form);
	}
	
	public OperationDescription(Operation owner, String title, DefRoot form) {
		this(owner.getClass(), owner, new Version(null), title, form);
	}
	
	public OperationDescription(Class<?> clazz, MNlsProvider nlsProvider, Version version, String title, DefRoot form) {
		this(clazz.getPackage().getName(), clazz.getSimpleName(), version, nlsProvider, title);
		if (form != null)
			setForm(form);
	}
	
	public void setForm(DefRoot form) {
		try {
			form = form.build();
			parameterDef = ParameterDefinitions.create(form);
//			Document document = MXml.createDocument();
//			Element de = document.createElement("root");
//			XmlConfig c = new XmlConfig(de);
//			new ConfigBuilder().cloneConfig(form, c);
//			String formStr = MXml.toString(de, false);
			this.form = form;
		} catch (Exception e) {
			log.w("invalid form",group,id,version,e);
		}
	}
	
	public ParameterDefinitions getParameterDefinitions() {
		return parameterDef;
	}

	public OperationDescription(OperationGroupDescription group, String id, Version version, MNlsProvider nlsProvider, String title ) {
		this(group.getGroup(),id, version, nlsProvider, title);
	}
	
	public OperationDescription(String group, String id, Version version, MNlsProvider nlsProvider, String title) {
		this.id = id;
		this.group = group;
		this.nlsProvider = nlsProvider;
		this.title = title;
		this.version = version;
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

	public DefRoot getForm() {
		return form;
	}
	
	public String getPath() {
		return group + '.' + id;
	}
	
	@Override
	public String getVersionString() {
		return version.toString();
	}
	
	public Version getVersion() {
		return version;
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
