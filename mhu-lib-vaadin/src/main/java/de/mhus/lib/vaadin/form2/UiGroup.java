package de.mhus.lib.vaadin.form2;

import com.vaadin.ui.Panel;

import de.mhus.lib.core.MString;
import de.mhus.lib.form.LayoutComposite;

public class UiGroup extends UiVaadinComposite {
	
	private Panel panel;

	@Override
	protected void addToCurrent(VaadinFormBuilder builder) {
		if (!MString.isEmpty(getElement().getTitle())) {
			panel = new Panel(getElement().getTitle());
			panel.setContent(layout);
			panel.setWidth("100%");
			builder.addComposite((LayoutComposite)getElement(), panel);
		} else
			super.addToCurrent(builder);
	}
	
}
