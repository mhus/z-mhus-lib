package de.mhus.lib.vaadin.form;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.ui.AbstractLayout;

import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.Form;
import de.mhus.lib.form.UiComponent;

public class VaadinFormBuilder {

	private Form form;
	private UiLayout layout;
	private HashMap<String, UiVaadin> index = new HashMap<>();
	
	public VaadinFormBuilder() {
	}

	public void doBuild() throws Exception {
		
		index.clear();
		
		IConfig model = form.getModel();
		layout = createLayout(model);
		build(layout, model);
	}

	public UiLayout createLayout(IConfig model) throws Exception {
		String name = "layout" + model.getString("layout", "100");
		return (UiLayout)form.getAdapterProvider().createComponent(name, model);
	}

	private void build(UiLayout layout, IConfig model) throws Exception {
		
		for (ResourceNode node : model.getNodes()) {
			String name = node.getName();
			if (name.equals("element")) name = node.getString("type");
			
			UiComponent comp = form.getAdapterProvider().createComponent(name, (IConfig) node);
			comp.doInit(form, (IConfig) node);
			
			layout.createRow((UiVaadin) comp);
			
			index.put(node.getString("name"), (UiVaadin)comp);
			
			UiLayout nextLayout = ((UiVaadin)comp).getLayout();
			if (nextLayout != null)
				build(nextLayout, (IConfig) node);
		}
	}
	
	public void doRevert() {
		for (Map.Entry<String, UiVaadin> entry : index.entrySet())
			try {
				entry.getValue().doRevert();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public void doUpdateValues() {
		for (Map.Entry<String, UiVaadin> entry : index.entrySet())
			try {
				entry.getValue().doUpdateValue();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public UiVaadin getComponent(String name) {
		return index.get(name);
	}
	
	public UiLayout getLayout() {
		return layout;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	
}
