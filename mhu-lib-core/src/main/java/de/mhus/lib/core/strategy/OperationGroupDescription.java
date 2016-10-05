package de.mhus.lib.core.strategy;

import java.util.HashMap;

import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsProvider;

public class OperationGroupDescription {

	private String group;
	private String title;
	private HashMap<String, Object> parameters;
	
	public OperationGroupDescription() {}
	public OperationGroupDescription(String group, String title) {
		setGroup(group);
		setTitle(title);
	}
	
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public HashMap<String, Object> getParameters() {
		return parameters;
	}
	public void setParameters(HashMap<String, Object> parameters) {
		this.parameters = parameters;
	}

	public String findTitle(MNlsProvider p) {
		return MNls.find(p, getTitle());
	}

}
