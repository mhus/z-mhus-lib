package de.mhus.lib.core.util;

import java.io.Serializable;

/**
 * <p>TableColumn class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class TableColumn implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String type;
	private String note;
	
	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return name;
	}
	/**
	 * <p>Setter for the field <code>name</code>.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * <p>Getter for the field <code>type</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getType() {
		return type;
	}
	/**
	 * <p>Setter for the field <code>type</code>.</p>
	 *
	 * @param type a {@link java.lang.String} object.
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * <p>Getter for the field <code>note</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getNote() {
		return note;
	}
	/**
	 * <p>Setter for the field <code>note</code>.</p>
	 *
	 * @param note a {@link java.lang.String} object.
	 */
	public void setNote(String note) {
		this.note = note;
	}
}
