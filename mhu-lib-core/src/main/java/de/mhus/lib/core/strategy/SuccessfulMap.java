package de.mhus.lib.core.strategy;

import java.util.HashMap;
import java.util.Set;

/**
 * <p>SuccessfulMap class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class SuccessfulMap extends Successful {

	/**
	 * <p>Constructor for SuccessfulMap.</p>
	 *
	 * @param operation a {@link de.mhus.lib.core.strategy.Operation} object.
	 * @param msg a {@link java.lang.String} object.
	 */
	public SuccessfulMap(Operation operation, String msg) {
		super(operation, msg);
		setResult(new HashMap<>());
	}

	/**
	 * <p>Constructor for SuccessfulMap.</p>
	 *
	 * @param path a {@link java.lang.String} object.
	 * @param msg a {@link java.lang.String} object.
	 * @param rc a long.
	 */
	public SuccessfulMap(String path, String msg, long rc) {
		super(path, msg, rc, new HashMap<>());
	}

	/**
	 * <p>Constructor for SuccessfulMap.</p>
	 *
	 * @param path a {@link java.lang.String} object.
	 * @param msg a {@link java.lang.String} object.
	 * @param rc a long.
	 * @param keyValues a {@link java.lang.String} object.
	 */
	public SuccessfulMap(String path, String msg, long rc, String... keyValues) {
		super(path, msg, rc, keyValues);
	}

	/**
	 * <p>put.</p>
	 *
	 * @param key a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Object} object.
	 */
	@SuppressWarnings("unchecked")
	public void put(String key, Object value) {
		((HashMap<String,Object>)getResult()).put(key, value);
	}
	
	/**
	 * <p>get.</p>
	 *
	 * @param key a {@link java.lang.String} object.
	 * @return a {@link java.lang.Object} object.
	 */
	@SuppressWarnings("unchecked")
	public Object get(String key) {
		return ((HashMap<String,Object>)getResult()).get(key);
	}
	
	/**
	 * <p>remove.</p>
	 *
	 * @param key a {@link java.lang.String} object.
	 */
	@SuppressWarnings("unchecked")
	public void remove(String key) {
		((HashMap<String,Object>)getResult()).remove(key);
	}

	/**
	 * <p>keySet.</p>
	 *
	 * @return a {@link java.util.Set} object.
	 */
	@SuppressWarnings("unchecked")
	public Set<String> keySet() {
		return ((HashMap<String,String>)getResult()).keySet();
	}
	
}
