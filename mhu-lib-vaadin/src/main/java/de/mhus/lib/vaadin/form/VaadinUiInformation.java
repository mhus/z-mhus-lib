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

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.form.UiInformation;

public class VaadinUiInformation extends Panel implements UiInformation {

	private static final long serialVersionUID = 1L;
	private Label description;

	public VaadinUiInformation() {
		initUI();
	}
	
	protected void initUI() {
		setWidth("100%");
		setHeight("100px");
		description = new Label();
		description.setContentMode(ContentMode.HTML);
		//description.setWidth("100%");
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(description);
		layout.setWidth("100%");
		setContent(layout);
	}

	@Override
	public void setInformation(String name, String description) {
		if (this.description == null) return;
		this.description.setValue("<b>" + name + "</b><br/>" + description);
	}
	
}
