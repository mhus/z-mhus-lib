package de.mhus.lib.vaadin.form2;

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

import de.mhus.lib.form.LayoutComposite;
import de.mhus.lib.form.LayoutElement;

public class UiTabsheet extends UiVaadinComposite {

	private TabSheet tabSheed;

	@Override
	public void createUi(VaadinFormBuilder builder) {
		tabSheed = new TabSheet();
		builder.addComposite((LayoutComposite)getElement(), tabSheed);
	}
	
	@Override
	public void addComponent(LayoutElement element, Component component, int col1, int row1, int col2, int row2) {
		//TODO if (component instanceof AbstractLayout) ((AbstractLayout)component).setMargin(true);
		tabSheed.addTab(component,element.getTitle());
	}

}
