package de.mhus.lib.vaadin.form2;

import com.vaadin.ui.Accordion;
import com.vaadin.ui.Component;

import de.mhus.lib.form.LayoutComposite;
import de.mhus.lib.form.LayoutElement;

public class UiAccordion extends UiVaadinComposite {

	private Accordion accordion;

	@Override
	public void createUi(VaadinFormBuilder builder) {
		accordion = new Accordion();
		builder.addComposite((LayoutComposite)getElement(), accordion);
	}
	
	@Override
	public void addComponent(LayoutElement element, Component component, int col1, int row1, int col2, int row2) {
		accordion.addTab(component,element.getTitle());
	}

}
