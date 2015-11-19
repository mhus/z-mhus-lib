package de.mhus.lib.vaadin.form;

import com.vaadin.ui.AbstractLayout;

import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.Form;
import de.mhus.lib.form.UiComponent;

public class VaadinFormBuilder {

	private Form form;
	private UiLayout layout;
	private AbstractLayout informationPane;

	public VaadinFormBuilder(Form form) {
		this.form = form;
	}

	public void doBuild() throws MException {
		IConfig model = form.getModel();
		layout = new UiLayout();
		build(layout, model);
	}

	private void build(UiLayout layout, IConfig model) throws MException {
		
		for (ResourceNode node : model.getNodes()) {
			String name = node.getName();
			if (name.equals("element")) name = node.getString("type");
			UiComponent comp = form.getAdapterProvider().createComponent(name, (IConfig) node);
			
			layout.createRow((UiVaadin) comp);
			
			UiLayout nextLayout = ((UiVaadin)comp).getLayout();
			if (nextLayout != null)
				build(nextLayout, (IConfig) node);
		}
	}
	
	public UiLayout getLayout() {
		return layout;
	}

	public void setInformationPane(AbstractLayout informationPane) {
		this.informationPane = informationPane;
	}
	
}
