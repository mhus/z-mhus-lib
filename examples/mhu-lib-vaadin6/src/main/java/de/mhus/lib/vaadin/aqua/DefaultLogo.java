package de.mhus.lib.vaadin.aqua;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeButton;

public class DefaultLogo implements Logo {

	private Desktop desktop;
	

	public Desktop getDesktop() {
		return desktop;
	}

	public void setDesktop(Desktop desktop) {
		this.desktop = desktop;
	}

	@Override
	public Component getUI() {
        Button logo = new NativeButton("Name", new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				
			}
        });
        logo.setDescription("Home");
        logo.setDebugId("homeButton");
        //logo.setStyleName(BaseTheme.BUTTON_LINK);
        //logo.addStyleName("logo");
        logo.setHeight("50px");
        return logo;
	}

}
