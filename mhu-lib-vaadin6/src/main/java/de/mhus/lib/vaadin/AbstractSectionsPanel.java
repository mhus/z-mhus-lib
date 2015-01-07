package de.mhus.lib.vaadin;

import java.util.Observable;
import java.util.Observer;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

import de.mhus.lib.vaadin.SectionsSelector.Section;

public abstract class AbstractSectionsPanel extends HorizontalLayout implements Observer {

	private static final long serialVersionUID = 1L;
	protected SectionsSelector selector;
	protected Panel content;

	public AbstractSectionsPanel() {
		selector = new SectionsSelector();
		this.addComponent(selector);
		
		content = new Panel();
		this.addComponent(content);
		content.setSizeFull();
		setExpandRatio(content, 1.0f);
		
		selector.eventHandler().register(this);
	}
	
	
    @Override
	public void update(Observable o, Object arg) {
    	if (arg == null) return;
    	if (arg instanceof SectionsSelector.Event) {
    		switch (((SectionsSelector.Event)arg).getEvent()) {
			case SECTION_CHANGED:
				Section sel = selector.getSelected();
				if (sel != null)
					doShow(sel);
				break;
			case SECTION_CHANGEING:
				sel = selector.getSelected();
				if (sel != null)
					doRemove(sel);
				break;
			default:
				break;
    		
    		}
    	}
    }


	protected abstract void doShow(Section sel);

	protected abstract void doRemove(Section sel);
	
	protected void setContent(AbstractComponent c) {
		content.removeAllComponents();
		content.addComponent(c);
	}
	
	
	
}
