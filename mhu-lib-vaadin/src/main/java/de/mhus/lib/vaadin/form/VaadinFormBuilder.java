/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.vaadin.form;

import java.util.HashMap;
import java.util.Map;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.form.IUiBuilder;
import de.mhus.lib.form.MForm;
import de.mhus.lib.form.UiComponent;

public class VaadinFormBuilder extends MLog implements IUiBuilder {

	private MForm form;
	private UiLayout layout;
	private HashMap<String, UiVaadin> index = new HashMap<>();
	
	public VaadinFormBuilder() {
	}

	@Override
	public void doBuild() throws Exception {
		
		index.clear();
		
		IConfig model = form.getModel();
		layout = createLayout(model);
		build(layout, model);
	}

	public UiLayout createLayout(IConfig model) throws Exception {
		String name = "layout" + model.getString("layout", "100");
		UiLayout ret = (UiLayout)form.getAdapterProvider().createComponent(name, model);
		ret.doInit(form, model);
		return ret;
	}

	private void build(UiLayout layout, IConfig model) throws Exception {
		
		for (ResourceNode<?> node : model.getNodes()) {
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
	
	@Override
	public void doRevert() {
		try {
			layout.doRevert();
		} catch (Throwable e) {
			log().e(e);
		}
		for (Map.Entry<String, UiVaadin> entry : index.entrySet())
			try {
				entry.getValue().doRevert();
			} catch (Throwable e) {
				log().e(entry.getKey(),e);
			}
	}

	public void doUpdateValue(String name) {
		UiVaadin entry = index.get(name);
		try {
			entry.doUpdateValue();
		} catch (Throwable e) {
			log().e(name,e);
		}
	}
	
	@Override
	public void doUpdateValues() {
		for (Map.Entry<String, UiVaadin> entry : index.entrySet())
			try {
				entry.getValue().doUpdateValue();
			} catch (Throwable e) {
				log().e(entry.getKey(),e);
			}
	}

	@Override
	public UiVaadin getComponent(String name) {
		return index.get(name);
	}
	
	public UiLayout getLayout() {
		return layout;
	}

	@Override
	public MForm getForm() {
		return form;
	}

	public void setForm(MForm form) {
		this.form = form;
	}
	
}
