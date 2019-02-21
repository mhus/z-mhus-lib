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
package de.mhus.lib.vaadin;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.core.lang.IObserver;
import de.mhus.lib.vaadin.SectionsSelector.Section;

public abstract class AbstractSectionsPanel extends HorizontalLayout implements IObserver<SectionsSelector.Event> {

	private static final long serialVersionUID = 1L;
	protected SectionsSelector selector;
	protected Panel content;

    public AbstractSectionsPanel() {
		selector = new SectionsSelector();
		this.addComponent(selector);
		
		content = new Panel();
		this.addComponent(content);
		content.setSizeFull();
		setExpandRatio(content, 1.0f);
		
		selector.eventHandler().register(this);
	}
	
	
    @Override
	public void update(Object src, Object evt, SectionsSelector.Event arg) {
    	if (arg == null) return;
		switch (arg.getEvent()) {
		case SECTION_CHANGED:
			Section sel = selector.getSelected();
			if (sel != null)
				doShow(sel);
			break;
		case SECTION_CHANGEING:
			sel = selector.getSelected();
			if (sel != null)
				doRemove(sel);
			break;
		default:
			break;
		
		}
	
    }


	protected abstract void doShow(Section sel);

	protected abstract void doRemove(Section sel);
	
	protected void setContent(AbstractComponent c) {
		AbstractComponentContainer x = (AbstractComponentContainer) content.getContent();
		if (x == null) {
			x = new VerticalLayout();
			content.setContent(x);
		}
		x.removeAllComponents();
		x.addComponent(c);
	}
	
	
	
}
