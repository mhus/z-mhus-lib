package de.mhus.lib.core.util;

import java.io.Serializable;

public class TableColumn implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String type;
	private String note;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}
