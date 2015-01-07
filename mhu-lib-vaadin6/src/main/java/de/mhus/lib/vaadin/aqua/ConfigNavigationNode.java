package de.mhus.lib.vaadin.aqua;

import java.util.LinkedList;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.errors.MException;


public class ConfigNavigationNode extends MObject implements NavigationNode {

	private ResourceNode config;

	public ConfigNavigationNode(ResourceNode config) {
		this.config = config;
	}
	
	@Override
	public NavigationNode getParent() {
		
		if (config != null) {
			ResourceNode cur = config.getParent();
			if (cur != null) {
				return new ConfigNavigationNode(cur);
			}
		}
		return null;
	}

	@Override
	public NavigationNode[] getChildren() {
		LinkedList<NavigationNode> out = new LinkedList<NavigationNode>();
		if (config != null) {
			for (ResourceNode c : config.getNodes()) {
				out.add(new ConfigNavigationNode(c));
			}
		}
		return out.toArray(new NavigationNode[out.size()]);
	}

	@Override
	public String getTitle() {
		try {
			return config.getExtracted("title");
		} catch (MException e) {
			log().d(e);
		}
		return null;
	}

	@Override
	public boolean isLeaf() {
		return config == null || config.getNodes().length == 0;
	}

	public boolean equals(Object other) {
		if (other != null && other instanceof ConfigNavigationNode && ((ConfigNavigationNode)other).config.equals(config) ) {
			return true;
		}
		return super.equals(other);
	}

	@Override
	public void doShowPreview(Desktop desktop, ComponentContainer previewContentPane) {
		previewContentPane.addComponent(new Label(config.toString() ));
	}

	@Override
	public void doOpenNode(Desktop desktop) {
		try {
			desktop.getContentArea().addPanel(new ContentPanel(getTitle(), new Label(Desktop.pathToString(this)), this, true ));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
