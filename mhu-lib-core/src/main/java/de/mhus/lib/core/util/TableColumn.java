package de.mhus.lib.core.util;

import java.io.Serializable;

public class TableColumn implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String type;
	private int size;
	
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
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
}
