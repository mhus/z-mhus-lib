package de.mhus.lib.vaadin.aqua;

import java.util.Observable;
import java.util.Observer;

import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;
import de.mhus.lib.vaadin.layouter.LayUtil;
import de.mhus.lib.vaadin.layouter.XLayElement;

public class PreviewContentPane extends VerticalLayout implements XLayElement, DesktopInject, Observer {

	private static final long serialVersionUID = 1L;
	private Desktop desktop;

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof NavigationNode) {
			updateView( (NavigationNode)arg );
		} else
		if (arg instanceof NavigationSelection) {
			setPath((NavigationSelection)arg);
		}
	}

	private void setPath(NavigationSelection arg) {
		this.removeAllComponents();
		arg.getSelectedNode().doShowPreview(desktop, this);
	}

	private void updateView(NavigationNode arg) {
		
	}

	@Override
	public void setDesktop(Desktop desktop) {
		this.desktop = desktop;
		desktop.getObserverable().addObserver(this);
	}

	@Override
	public void setConfig(ResourceNode config) throws MException {
		setSizeFull();
		LayUtil.configure(this, config);
	}

	@Override
	public void doAppendChild(XLayElement child, ResourceNode cChild) {
		
	}

}
