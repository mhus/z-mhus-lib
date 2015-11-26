package de.mhus.test.forms02;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.form.DummyDataSource;
import de.mhus.lib.form.Form;
import de.mhus.lib.form.ui.FmText;
import de.mhus.lib.vaadin.form.VaadinForm;

@Title("Forms02")
@Theme("valo")
public class Forms02UI extends UI {

	@Override
	protected void init(VaadinRequest request) {
		
		
		try {
			VerticalLayout mainLayout = new VerticalLayout();
	        mainLayout.setSizeFull();
	        
	        mainLayout.addComponent(new Label("Moin"));
			setContent(mainLayout);
			
			DefRoot model = new DefRoot(
					new FmText("firstName", "Vorname", "Dein Vorname"),
					new FmText("lastName", "Nachname", "Dein Nachname")
					);
			
			Form form = new Form(model);
			System.out.println( form.getModel().dump() );
			form.setDataSource(new DummyDataSource());
			
			
			VaadinForm vf = new VaadinForm(form);
		
			vf.doBuild();
			//vf.setSizeFull();
			vf.setWidth("100%");
			
			mainLayout.addComponent(vf);
			
/*			
			GridLayout grid = new GridLayout(3,1);
			
			grid.addComponent(new Label("Label"), 0, 0);
			grid.addComponent(new TextField(), 1, 0, 2, 0);
			
			mainLayout.addComponent(grid);
*/			
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@WebServlet(urlPatterns = "/*")
    @VaadinServletConfiguration(ui = Forms02UI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
