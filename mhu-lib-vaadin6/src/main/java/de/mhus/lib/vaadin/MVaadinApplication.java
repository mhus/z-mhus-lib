package de.mhus.lib.vaadin;

import com.vaadin.Application;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;


public abstract class MVaadinApplication extends Application {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected VerticalLayout layout;
	protected Window window;

	public MVaadinApplication() {
		
	}
	
    @Override
	public void init() {
		window = new Window();
		setMainWindow(window);

		layout = (VerticalLayout) getMainWindow().getContent();

    }
	
	@Override
	public void close() {
		super.close();
	}
	
	
	protected VerticalLayout getContent() {
		return layout;
	}

}
