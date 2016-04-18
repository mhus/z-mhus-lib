package de.mhus.lib.vaadin.form;

import de.mhus.lib.core.activator.DefaultActivator;
import de.mhus.lib.form.ActivatorAdapterProvider;
import de.mhus.lib.form.definition.FmText;

public class DefaultAdapterProvider extends ActivatorAdapterProvider {

	public DefaultAdapterProvider() {
		super(null);
		DefaultActivator a = new DefaultActivator();
		activator = a;
		
		a.addMap("text", UiText.Adapter.class);
		a.addMap("checkbox", UiCheckbox.Adapter.class);
		a.addMap("date", UiDate.Adapter.class);
		a.addMap("password", UiPassword.Adapter.class);
		a.addMap("number", UiNumber.Adapter.class);
		a.addMap("textarea", UiTextArea.Adapter.class);
		a.addMap("richtext", UiRichTextArea.Adapter.class);
		a.addMap("combobox", UiCombobox.Adapter.class);
		a.addMap("layout100", UiLayout100.Adapter.class);
		a.addMap("layout50x50", UiLayout50x50.Adapter.class);
		a.addMap("layouttabs", UiLayoutTabs.Adapter.class);
		a.addMap("100", UiLayout100.Adapter.class);
		a.addMap("50x50", UiLayout50x50.Adapter.class);
		a.addMap("tabs", UiLayoutTabs.Adapter.class);
		a.addMap("options", UiOptions.Adapter.class);
		
	}

}
