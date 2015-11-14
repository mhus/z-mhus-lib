package de.mhus.lib.vaadin.form;

import java.io.IOException;
import java.net.URISyntaxException;

import junit.framework.TestSuite;

import org.junit.Test;

import de.mhus.lib.form.ActivatorControl;
import de.mhus.lib.form.LayoutFactory;
import de.mhus.lib.form.pojo.LayoutModelByPojo;
import de.mhus.lib.vaadin.form2.VaadinFormBuilder;

public class TestForm2 extends TestSuite {

	@Test
	public void testForm1() throws URISyntaxException, IOException, Exception {
		
		MyModel pojo = new MyModel();

		VaadinFormBuilder builder = new VaadinFormBuilder();
		builder.setFormFactory(new LayoutFactory());

		ActivatorControl control = new ActivatorControl();

		LayoutModelByPojo layout = new LayoutModelByPojo(pojo);
		layout.setFormControl(control);
		layout.setFormFactory(builder.getFormFactory());
		layout.doBuild();
		
		
	}
	
}
