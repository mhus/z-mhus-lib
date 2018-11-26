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

import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.ComponentAdapter;
import de.mhus.lib.form.ComponentDefinition;
import de.mhus.lib.form.DataSource;
import de.mhus.lib.form.UiComponent;

public class UiPanel extends UiLayout {

	private static final long serialVersionUID = 1L;
	private Panel layout;
	private UiVaadin content = null;
	
	public UiPanel() {
		this.layout = new Panel();
//		layout.setSizeFull();
		layout.setWidth("100%");
	}
	
	@Override
	public void createRow(final UiVaadin c) {
		
		
		//String name = c.getName();
		Component editor = c.createEditor();
		DataSource ds = getForm().getDataSource();
		String caption = c.getCaption(ds);
		
		
		layout.setCaption(caption);
		if (editor instanceof Layout) {
			layout.setContent(editor);
		} else {
			VerticalLayout container = new VerticalLayout(editor);
			layout.setContent(container);
			c.setComponentEditor(editor);
			c.setListeners();
		}
		editor.setWidth("100%");
		content = c;
	}
	
	@Override
	public void doRevert() throws MException {
		
		try {
			content.doRevert();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		super.doRevert();
	}

	@Override
	public Component getComponent() {
		return layout;
	}

	public static class Adapter implements ComponentAdapter {

		@Override
		public UiComponent createAdapter(IConfig config) {
			return new UiPanel();
		}

		@Override
		public ComponentDefinition getDefinition() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
}
