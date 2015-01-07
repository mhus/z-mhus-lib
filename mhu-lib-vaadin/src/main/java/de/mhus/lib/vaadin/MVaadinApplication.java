package de.mhus.lib.vaadin;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


public abstract class MVaadinApplication extends UI {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected VerticalLayout layout;

	public MVaadinApplication() {
		
	}
	
    @Override
	protected void init(VaadinRequest request) {
    	layout = new VerticalLayout();
    	setContent(layout);
    	doContent(layout);
    }
	
    protected abstract void doContent(VerticalLayout layout);

	@Override
	public void close() {
		super.close();
	}
	
	
	protected VerticalLayout getContentLayout() {
		return layout;
	}

}
