package de.mhus.lib.vaadin.aqua;


import java.util.Observable;
import java.util.Observer;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.vaadin.CardButton;
import de.mhus.lib.vaadin.layouter.XLayElement;

public class DefaultNavigation extends VerticalLayout implements XLayElement, DesktopInject, Observer, Button.ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Desktop desktop;

	protected Button historyButton(NavigationNode n, boolean isSelected) {
		CardButton b = new CardButton(n.getTitle());
		if (isSelected) {
			b.setBackgroundColor("#000");
			b.setForegroundColor("#fff");
		} else {
			b.setBackgroundColor("#aaa");
			b.setForegroundColor("#000");
		}
		b.setBorder(new Border(1,0,1,0));
		b.setMargin(new Border(1,0,0,0));
		b.setWidth("100%");
		b.setHeight("40px");
		b.updateCaption();
		b.setData(n);
		b.addListener(this);
		
		return b;
	}
	
	protected Button childButton(NavigationNode n, boolean isSelected) {
		CardButton b = new CardButton(n.getTitle());
		if (isSelected) {
			b.setBackgroundColor("#000");
			b.setForegroundColor("#fff");
		} else {
			b.setBackgroundColor("#ccc");
			b.setForegroundColor("#000");
		}
		b.setBorder(new Border(1,0,1,0));
		b.setMargin(new Border(1,0,0,20));
		b.setWidth("100%");
		b.setHeight("40px");
		b.updateCaption();
		b.setData(n);
		b.addListener(this);

		return b;
	}
	
	public Desktop getDesktop() {
		return desktop;
	}

	public void setDesktop(Desktop desktop) {
		this.desktop = desktop;
		desktop.getObserverable().addObserver(this);
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
		// check all shown nodes and update if needed
	}

	public void setPath(NavigationSelection arg) {
		removeAllComponents();
		
		for (NavigationNode n : arg.getPath()) {
			Button b = historyButton(n,arg.isSelectedNode(n));
			addComponent(b);
		}
		
		for (NavigationNode n : arg.getChildren()) {
			Button b = childButton(n,arg.isSelectedNode(n));
			addComponent(b);
		}
		
	}

	@Override
	public void buttonClick(ClickEvent event) {
		NavigationNode n = (NavigationNode) event.getButton().getData();
		if (n == null) return;
		if (getDesktop().isSelectedPath(n))
			getDesktop().openNode(n);
		else
			getDesktop().setSelectedPath(n);
	}

	@Override
	public void setConfig(ResourceNode config) {
		setWidth("100%");
	}

	@Override
	public void doAppendChild(XLayElement child, ResourceNode cChild) {
		
	}

}
