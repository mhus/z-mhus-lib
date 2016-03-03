package de.mhus.lib.core.json;

import java.lang.reflect.Array;

import org.codehaus.jackson.JsonNode;

import de.mhus.lib.core.MJson;
import de.mhus.lib.core.pojo.DefaultFilter;
import de.mhus.lib.core.pojo.PojoModel;
import de.mhus.lib.core.pojo.PojoParser;

/**
 * <p>TransformHelper class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class TransformHelper {
	int level = 0;
	protected String prefix = "";
	protected TransformStrategy strategy = MJson.DEFAULT_STRATEGY;
	
	/**
	 * <p>incLevel.</p>
	 *
	 * @return a {@link de.mhus.lib.core.json.TransformHelper} object.
	 */
	public TransformHelper incLevel() {
		level++;
		return this;
	}
	
	/**
	 * <p>postToJson.</p>
	 *
	 * @param from a {@link java.lang.Object} object.
	 * @param to a {@link org.codehaus.jackson.JsonNode} object.
	 */
	public void postToJson(Object from, JsonNode to) {
	}
	
	/**
	 * <p>getType.</p>
	 *
	 * @param clazz a {@link java.lang.String} object.
	 * @return a {@link java.lang.Class} object.
	 * @throws java.lang.IllegalAccessException if any.
	 * @since 3.2.9
	 */
	public Class<?> getType(String clazz) throws IllegalAccessException {
		try {
			
			
			if (clazz.endsWith("[]")) {
				clazz = clazz.substring(0, clazz.length()-2);
			}
			
			switch (clazz) {
			case "byte" : return byte.class;
			case "int" : return int.class;
			case "float" : return float.class;
			case "double" : return double.class;
			case "boolean" : return boolean.class;
			case "short" : return short.class;
			case "char" : return char.class;
			}
			
			return getClassLoader().loadClass(clazz);
		} catch (Exception e) {
		}
		return null;
	}
	
	/**
	 * <p>isArrayType.</p>
	 *
	 * @param type a {@link java.lang.String} object.
	 * @return a boolean.
	 * @since 3.2.9
	 */
	public boolean isArrayType(String type) {
		return type.endsWith("[]");
	}
	
	/**
	 * <p>getClassLoader.</p>
	 *
	 * @return a {@link java.lang.ClassLoader} object.
	 */
	public ClassLoader getClassLoader() {
		return TransformHelper.class.getClassLoader();
	}

	/**
	 * <p>decLevel.</p>
	 *
	 * @return a {@link de.mhus.lib.core.json.TransformHelper} object.
	 */
	public TransformHelper decLevel() {
		level--;
		return this;
	}

	/**
	 * <p>createObject.</p>
	 *
	 * @param type a {@link java.lang.Class} object.
	 * @return a {@link java.lang.Object} object.
	 * @throws java.lang.InstantiationException if any.
	 * @throws java.lang.IllegalAccessException if any.
	 */
	public Object createObject(Class<?> type) throws InstantiationException, IllegalAccessException {
		return type.newInstance();
	}
	
	/**
	 * <p>log.</p>
	 *
	 * @param string a {@link java.lang.String} object.
	 * @param t a {@link java.lang.Throwable} object.
	 */
	public void log(String string, Throwable t) {
		System.out.println(string);
		t.printStackTrace();
	}
	/**
	 * <p>Getter for the field <code>prefix</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPrefix() {
		return prefix;
	}
	/**
	 * <p>Setter for the field <code>prefix</code>.</p>
	 *
	 * @param in a {@link java.lang.String} object.
	 */
	public void setPrefix(String in) {
		prefix = in;
	}
	/**
	 * <p>createPojoModel.</p>
	 *
	 * @param from a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.core.pojo.PojoModel} object.
	 */
	public PojoModel createPojoModel(Object from) {
		PojoModel model = new PojoParser().parse(from,"_",null).filter(new DefaultFilter(true, false, true, true, true) ).getModel();
		return model;
	}
	/**
	 * <p>log.</p>
	 *
	 * @param msg a {@link java.lang.String} object.
	 */
	public void log(String msg) {
		System.out.println(msg);
	}
	/**
	 * Return true if the level is ok
	 *
	 * @return a boolean.
	 */
	public boolean checkLevel() {
		return level < 10;
	}

	/**
	 * <p>createArray.</p>
	 *
	 * @param length a int.
	 * @param type a {@link java.lang.Class} object.
	 * @return a {@link java.lang.Object} object.
	 * @since 3.2.9
	 */
	public Object createArray(int length, Class<?> type) {
		return Array.newInstance(type, length);
	}

	/**
	 * <p>Getter for the field <code>strategy</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.json.TransformStrategy} object.
	 * @since 3.2.9
	 */
	public TransformStrategy getStrategy() {
		return strategy;
	}

	/**
	 * <p>postToPojo.</p>
	 *
	 * @param from a {@link org.codehaus.jackson.JsonNode} object.
	 * @param to a {@link java.lang.Object} object.
	 * @since 3.2.9
	 */
	public void postToPojo(JsonNode from, Object to) {
		// TODO Auto-generated method stub
		
	}

}
