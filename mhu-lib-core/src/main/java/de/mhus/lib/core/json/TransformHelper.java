package de.mhus.lib.core.json;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import de.mhus.lib.core.pojo.DefaultFilter;
import de.mhus.lib.core.pojo.PojoModel;
import de.mhus.lib.core.pojo.PojoParser;

public class TransformHelper {
	int level = 0;
	private String prefix = "";
	private boolean rememberClass = false;
	
	public TransformHelper incLevel() {
		level++;
		return this;
	}
	
	public void postToJson(Object from, ObjectNode to) {
		if (rememberClass && from != null)
			to.put("_class", from.getClass().getCanonicalName());
	}

	public Object createObject(JsonNode from) throws IllegalAccessException {
		JsonNode cNameNode = from.get("_class");
		if (cNameNode == null) return null;
		String cName = cNameNode.getTextValue();
		try {
			return getClassLoader().loadClass(cName).newInstance();
		} catch (Exception e) {
		}
		return null;
	}

	public ClassLoader getClassLoader() {
		return TransformHelper.class.getClassLoader();
	}

	public TransformHelper decLevel() {
		level--;
		return this;
	}

	public Object createObject(Class<?> type) throws InstantiationException, IllegalAccessException {
		return type.newInstance();
	}
	
	public void log(String string, Throwable t) {
		System.out.println(string);
		t.printStackTrace();
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String in) {
		prefix = in;
	}
	public PojoModel createPojoModel(Object from) {
		PojoModel model = new PojoParser().parse(from,"_",null).filter(new DefaultFilter(true, false, true, true, true) ).getModel();
		return model;
	}
	public void log(String msg) {
		System.out.println(msg);
	}
	/**
	 * Return true if the level is ok
	 * 
	 * @return
	 */
	public boolean checkLevel() {
		return level < 10;
	}

	public boolean isRememberClass() {
		return rememberClass;
	}

	public void setRememberClass(boolean rememberClass) {
		this.rememberClass = rememberClass;
	}

}