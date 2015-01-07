package de.mhus.lib.vaadin.layouter;

import java.util.HashMap;

public class LayModel {

	private XLayElement root;
	private HashMap<String, XLayElement> elements;

	public LayModel(XLayElement root, HashMap<String, XLayElement> elements) {
		this.root = root;
		this.elements = elements;
	}
	
	public XLayElement getRoot() {
		return root;
	}

	public XLayElement get(String name) {
		return elements.get(name);
	}
	
}
