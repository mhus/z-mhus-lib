package de.mhus.lib.vaadin.layouter;

import java.util.HashMap;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.activator.MutableActivator;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.lang.MObject;

public class LayoutBuilder extends MObject {

	public static final String VERTICAL = "vertical";
	public static final String HORIZONTAL = "horizontal";
	public static final String VERTICAL_SPLIT = "split_vertical";
	public static final String HORIZONTAL_SPLIT = "split_horizontal";
	public static final String DATA = "data";
	public static final String FULL_SIZE = "full_size";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String MARGIN = "margin";
	public static final String SPACING = "spacing";
	public static final String STYLE = "style";
	public static final String HIDDEN = "hidden";
	public static final String SPLIT_MIN = "split_min";
	public static final String SPLIT_MAX = "split_max";
	public static final String SPLIT_POS = "split_pos";
	public static final String EXPAND = "expand";
	
	
	private HashMap<String, XLayElement> elements;
	private LayModel model;

	public LayoutBuilder() {
	}
	
	public LayoutBuilder doBuild(ResourceNode layout) throws Exception {
		
		if (layout.getName().equals(DefRoot.ROOT))
			layout = layout.getNode("layout").getNodes()[0];
	
		XLayElement root = getActivator().createObject(XLayElement.class, layout.getName());
		root.setConfig(layout);
		elements = new HashMap<String, XLayElement>();
		model  = new LayModel(root, elements);
		build(root,layout);
		
		return this;
	}
	
	public MActivator getActivator() {
		return MSingleton.baseLookup(this,MActivator.class);
	}

	protected void build(XLayElement parent, ResourceNode layout) throws Exception {
		ResourceNode layoutLayout = layout.getNode("layout");
		if (layoutLayout == null) return;
		
		for (ResourceNode cChild : layoutLayout.getNodes()) {
			XLayElement child = getActivator().createObject(XLayElement.class, cChild.getName());
			parent.doAppendChild(child,cChild);
			child.setConfig(cChild);
			
			// remember if have name
			String name = cChild.getExtracted("name");
			if (name != null)
				elements.put(name, child);
			
			// recursion
			build(child, cChild);
		}
	}

	public static void initDefaultActivator(MutableActivator activator) {
		activator.addMap(XLayElement.class, VERTICAL, XLayVertical.class);
		activator.addMap(XLayElement.class, HORIZONTAL, XLayHorizontal.class);
		activator.addMap(XLayElement.class, VERTICAL_SPLIT, XLayVerticalSplit.class);
		activator.addMap(XLayElement.class, HORIZONTAL_SPLIT, XLayHorizontalSplit.class);
	}

	public LayModel getModel() {
		return model;
	}
	
}
