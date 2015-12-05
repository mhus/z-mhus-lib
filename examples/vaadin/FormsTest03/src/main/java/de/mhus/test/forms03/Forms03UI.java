package de.mhus.test.forms03;

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

@Title("Forms03")
@Theme("valo")
public class Forms03UI extends UI {

	@Override
	protected void init(VaadinRequest request) {
		
		
		try {
			
			DemoEditor editor = new DemoEditor(DemoEntity.class,"");
			editor.setSizeFull();
			editor.initUI();

			setContent(editor);

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@WebServlet(urlPatterns = "/*")
    @VaadinServletConfiguration(ui = Forms03UI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
