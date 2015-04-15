package de.mhus.lib.vaadin.aqua;

import java.util.Observable;
import java.util.Observer;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.vaadin.CardButton;
import de.mhus.lib.vaadin.layouter.XLayElement;

public class DefaultBreadcrumb extends HorizontalLayout implements XLayElement, Observer, Button.ClickListener, DesktopInject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Desktop desktop;


	public Desktop getDesktop() {
		return desktop;
	}

	public void setDesktop(Desktop desktop) {
		this.desktop = desktop;
		desktop.getObserverable().addObserver(this);
	}

	public void setPath(NavigationSelection arg) {
		removeAllComponents();
		
		boolean first = true;
		for (NavigationNode n : arg.getPath()) {
			if (!first)
				addComponent(label());
			Button b = historyButton(n);
			addComponent(b);
			first = false;
		}
		
		if (arg.getSelectedNode().isLeaf()) {
			if (!first)
				addComponent(label());
			Button b = historyButton(arg.getSelectedNode());
			addComponent(b);
		}
	
	}
	
	protected Component label() {
		Label out = new Label(" > ");
		out.setHeight("30px");
		return out;
	}

	protected Button historyButton(NavigationNode n) {
		CardButton b = new CardButton(n.getTitle());
//		b.setBackgroundColor("#aaa");
		b.setForegroundColor("#000");
//		b.setBorder(new Border(1,0,1,0));
		b.setMargin(new Border(1,0,0,0));
		//b.setWidth("100%");
		b.setHeight("30px");
		b.updateCaption();
		b.setData(n);
		b.addListener(this);
		
		return b;
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		NavigationNode n = (NavigationNode) event.getButton().getData();
		if (n == null) return;
		getDesktop().setSelectedPath(n);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof NavigationNode) {
			updateView( (NavigationNode)arg );
		} else
		if (arg instanceof NavigationSelection) {
			setPath((NavigationSelection)arg);
		}
	}

	private void updateView(NavigationNode arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setConfig(ResourceNode config) {
		setHeight("30px");
	}

	@Override
	public void doAppendChild(XLayElement child, ResourceNode cChild) {
		
	}


}
