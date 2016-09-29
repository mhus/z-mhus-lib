package de.mhus.lib.core.strategy;

import java.util.HashMap;

import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsProvider;

/**
 * <p>OperationGroupDescription class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class OperationGroupDescription {

	private String group;
	private String title;
	private HashMap<String, Object> parameters;
	
	/**
	 * <p>Constructor for OperationGroupDescription.</p>
	 */
	public OperationGroupDescription() {}
	/**
	 * <p>Constructor for OperationGroupDescription.</p>
	 *
	 * @param group a {@link java.lang.String} object.
	 * @param title a {@link java.lang.String} object.
	 */
	public OperationGroupDescription(String group, String title) {
		setGroup(group);
		setTitle(title);
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

	/**
	 * <p>findTitle.</p>
	 *
	 * @param p a {@link de.mhus.lib.core.util.MNlsProvider} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String findTitle(MNlsProvider p) {
		return MNls.find(p, getTitle());
	}

}
