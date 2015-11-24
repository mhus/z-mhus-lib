package de.mhus.test.forms02;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.vaadin.form.VaadinForm;

@Title("Forms02")
@Theme("valo")
public class Forms02UI extends UI {

	@Override
	protected void init(VaadinRequest request) {
		
		VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        
        mainLayout.addComponent(new Label("Moin"));
		setContent(mainLayout);
		
		VaadinForm vf = new VaadinForm();
		
		
	}

	@WebServlet(urlPatterns = "/*")
    @VaadinServletConfiguration(ui = Forms02UI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
