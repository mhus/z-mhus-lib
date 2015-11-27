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
import com.vaadin.ui.VerticalSplitPanel;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.DummyDataSource;
import de.mhus.lib.form.Form;
import de.mhus.lib.form.ModelDataSource;
import de.mhus.lib.form.UiComponent;
import de.mhus.lib.form.ui.FmCheckbox;
import de.mhus.lib.form.ui.FmDate;
import de.mhus.lib.form.ui.FmNumber;
import de.mhus.lib.form.ui.FmNumber.TYPES;
import de.mhus.lib.form.ui.FmRichText;
import de.mhus.lib.form.ui.FmDate.FORMATS;
import de.mhus.lib.form.ui.FmText;
import de.mhus.lib.form.ui.FmTextArea;
import de.mhus.lib.vaadin.form.VaadinForm;

@Title("Forms02")
@Theme("valo")
public class Forms02UI extends UI {

	@Override
	protected void init(VaadinRequest request) {
		
		
		try {
			VerticalLayout mainLayout = new VerticalLayout();
	        mainLayout.setSizeFull();
	        
	        VerticalSplitPanel split = new VerticalSplitPanel();
	        split.setSizeFull();
	        
	        split.addComponent(new Label("Moin"));

	        mainLayout.addComponent(split);
			setContent(mainLayout);
			
			DefRoot model = new DefRoot( new DefAttribute("showInformation", true),
					new FmText("firstName", "Vorname", "Dein Vorname"),
					new FmText("lastName", "Nachname", "Dein Nachname"),
					new FmTextArea("n1", "N1", "Dein Nachname"),
					new FmDate("n2", FORMATS.DATETIME, "N1", "Dein Nachname"),
					new FmNumber("n3", TYPES.INTEGER ,"N1", "Dein Nachname"),
					new FmCheckbox("n5", "N1", "Dein Nachname"),
					new FmRichText("n4", "N1", "Dein Nachname"),
					null
					);
			
			Form form = new Form(model);
			VaadinForm vf = new VaadinForm(form);
			System.out.println( form.getModel().dump() );
			DummyDataSource ds = new DummyDataSource() {
				@Override
				public boolean newValue(UiComponent component, Object newValue) {
					if (component.getName().equals("n5"))
						try {
							vf.getBuilder().getComponent("n4").setVisible( MCast.toboolean(newValue, true));
						} catch (MException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					return true;
				}
			};
			form.setDataSource(new ModelDataSource(ds));
			form.setControl(ds);
			
		
			vf.doBuild();
			vf.setSizeFull();
			//vf.setWidth("100%");
			
			split.addComponent(vf);
			
			vf.getBuilder().getComponent("firstName").setError("Error Message");
			
			
			
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
