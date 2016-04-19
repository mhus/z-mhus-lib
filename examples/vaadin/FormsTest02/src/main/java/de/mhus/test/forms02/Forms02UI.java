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
import de.mhus.lib.form.definition.FmCheckbox;
import de.mhus.lib.form.definition.FmDate;
import de.mhus.lib.form.definition.FmDisabled;
import de.mhus.lib.form.definition.FmLayout100;
import de.mhus.lib.form.definition.FmLayout50x50;
import de.mhus.lib.form.definition.FmLayoutTabs;
import de.mhus.lib.form.definition.FmNumber;
import de.mhus.lib.form.definition.FmRichText;
import de.mhus.lib.form.definition.FmRootLayout50x50;
import de.mhus.lib.form.definition.FmText;
import de.mhus.lib.form.definition.FmTextArea;
import de.mhus.lib.form.definition.FmDate.FORMATS;
import de.mhus.lib.form.definition.FmNumber.TYPES;
import de.mhus.lib.form.definition.FmOptions;
import de.mhus.lib.form.definition.FmPanel;
import de.mhus.lib.form.definition.FmReadOnly;
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
	        
	        Label label = new Label("Moin");
	        split.addComponent(label);
	        split.setSplitPosition(30,Unit.PIXELS);

	        mainLayout.addComponent(split);
			setContent(mainLayout);
			
			DefRoot model = new DefRoot( 
					new DefAttribute("showInformation", true),
					new FmText("firstName", "Vorname", "Dein Vorname" ),
					new FmText("lastName", "Nachname", "Dein Nachname", new FmDisabled()),
					new FmTextArea("n1", "N1", "Dein Nachname"),
					new FmDate("n2", FORMATS.DATETIME, "N1", "Dein Nachname"),
					new FmNumber("n3", TYPES.INTEGER ,"N1", "Dein Nachname"),
					new FmCheckbox("n5", "N1", "Dein Nachname"),
					new FmRichText("n4", "N1", "Dein Nachname" ),
					new FmOptions("n6","Options","Hobbies"),
					
					new FmLayoutTabs("tabs","Tabs Example","",
							new FmLayout100("t1", "Address","",
							//	new DefAttribute("visible","false"),
								new FmDisabled(),
								new FmText("n10", "N10", "Dein Nachname"),
								new FmText("n11", "N11", "Dein Nachname")
								),
							new FmPanel("t2","Panel","", 
								new FmLayout100("t2", "Location","",
										new FmText("n20", "N20", "Dein Nachname"),
										new FmText("n21", "N21", "Dein Nachname")
										)
								)
							),
					
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
			
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@WebServlet(urlPatterns = "/*")
    @VaadinServletConfiguration(ui = Forms02UI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
