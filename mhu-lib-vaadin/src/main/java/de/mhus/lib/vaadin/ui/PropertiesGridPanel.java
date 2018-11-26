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
package de.mhus.lib.vaadin.ui;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsProvider;

public class PropertiesGridPanel extends VerticalLayout implements MNlsProvider {

	private static final long serialVersionUID = 1L;
	private GridLayout grid;
	private MNls nls;

	public PropertiesGridPanel() {
		grid = new GridLayout(2, 1);
		grid.setWidth("100%");
		addComponent(grid);
		setExpandRatio(grid, 0);
	}
	
	public void clear() {
		grid.removeAllComponents();
		grid.setRows(1);
		grid.addComponent(new Label("<strong>" + MNls.find(this,"attribute=Attribute") +"</strong>",ContentMode.HTML));
		grid.addComponent(new Label("<strong>" + MNls.find(this,"value=Value") + "</strong>",ContentMode.HTML));
	}
	
	public void addAttribute(String name, String value) {
		grid.setRows(grid.getRows()+1);
		grid.addComponent(new Label(name));
		grid.addComponent(new Label(value));
	}
	
	@Override
	public MNls getNls() {
		return nls;
	}

	public void setNls(MNls nls) {
		this.nls = nls;
	}
	
}
