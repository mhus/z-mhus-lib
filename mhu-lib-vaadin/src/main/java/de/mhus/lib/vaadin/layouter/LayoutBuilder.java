/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.vaadin.layouter;

import java.util.HashMap;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.M;
import de.mhus.lib.core.activator.MutableActivator;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.definition.DefRoot;
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
	
	public LayoutBuilder doBuild(IConfig layout) throws Exception {
		
		if (layout.getName().equals(DefRoot.ROOT))
			layout = layout.getNode("layout").getNodes().iterator().next();
	
		XLayElement root = getActivator().createObject(XLayElement.class, layout.getName());
		root.setConfig(layout);
		elements = new HashMap<String, XLayElement>();
		model  = new LayModel(root, elements);
		build(root,layout);
		
		return this;
	}
	
	public MActivator getActivator() {
		return M.l(MActivator.class);
	}

	protected void build(XLayElement parent, IConfig layout) throws Exception {
		IConfig layoutLayout = layout.getNode("layout");
		if (layoutLayout == null) return;
		
		for (IConfig cChild : layoutLayout.getNodes()) {
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
