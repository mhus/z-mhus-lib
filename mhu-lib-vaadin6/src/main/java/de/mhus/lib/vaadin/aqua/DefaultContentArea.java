package de.mhus.lib.vaadin.aqua;

import java.util.LinkedList;

import com.vaadin.ui.TabSheet;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;
import de.mhus.lib.vaadin.layouter.LayUtil;
import de.mhus.lib.vaadin.layouter.XLayElement;

public class DefaultContentArea extends TabSheet implements XLayElement, ContentArea, DesktopInject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Desktop desktop;
	private LinkedList<ContentPanel> list = new LinkedList<ContentPanel>();

	@Override
	public void setDesktop(Desktop desktop) {
		this.desktop = desktop;
		desktop.setContentArea(this);
	}

	public Desktop getDesktop() {
		return desktop;
	}

	@Override
	public boolean addPanel(ContentPanel panel) {
		
		NavigationNode node = panel.getNavigationNode();
		if (node != null) {
			ContentPanel cp = findTabForNode(node);
			if (cp != null) {
				setSelectedTab((Tab)cp.getData());
				return false;
			}
		}
		Tab tab = addTab(panel.getUI(), panel.getTitle());
		tab.setClosable(panel.isCloseable());
		panel.setData(tab);
		setSelectedTab(tab);
		list.add(panel);
		return true;
	}

	private ContentPanel findTabForNode(NavigationNode node) {
		for (ContentPanel cp : list) {
			if (cp.getNavigationNode() != null && cp.getNavigationNode().equals(node))
				return cp;
		}
		return null;
	}

	@Override
	public void setConfig(ResourceNode config) throws MException {
		LayUtil.configure(this, config);
	}

	@Override
	public void doAppendChild(XLayElement child, ResourceNode cChild) {
		String title = null;
		try {
			title = cChild.getExtracted("title");
		} catch (MException e) {
			//log().d(e);
		}
		addPanel(new ContentPanel(title,child,null,false)); //TODO
	}
	
}
