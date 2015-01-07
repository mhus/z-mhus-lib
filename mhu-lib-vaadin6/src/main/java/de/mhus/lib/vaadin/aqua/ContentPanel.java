package de.mhus.lib.vaadin.aqua;

import com.vaadin.ui.Component;

public class ContentPanel {

	private Component ui;
	private String title;
	private boolean closeable = true;
	private Object data;
	private NavigationNode node;

	public ContentPanel(String title, Component ui, NavigationNode node, boolean closeable) {
		this.title = title;
		this.ui = ui;
		this.node = node;
		this.closeable = closeable;
	}
	
	public Component getUI() {
		return ui;
	}

	public String getTitle() {
		return title;
	}

	public boolean isCloseable() {
		return closeable;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public NavigationNode getNavigationNode() {
		return node;
	}

}
