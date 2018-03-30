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

import de.mhus.lib.vaadin.SectionsSelector.Section;

public class SectionsPanel extends AbstractSectionsPanel {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doShow(Section sel) {
		Object data = sel.getUserData();
		if (data != null) {
			if (data instanceof Listener) {
				((Listener)data).doShow();
			}
		}
		if (data instanceof AbstractComponent)
			setContent((AbstractComponent)data);
	}

	@Override
	protected void doRemove(Section sel) {
		Object data = sel.getUserData();
		if (data != null) {
			if (data instanceof Listener) {
				((Listener)data).doRemove();
			}
		}
		
	}

	public Section addSection(String name, String title, AbstractComponent content) {
		Section sec = selector.addSection(name, title, content);
		return sec;
	}

	public Section removeSection(String name) {
		return selector.removeSection(name);
	}
	
	public Section setSelected(String name) {
		return selector.setSelected(name);
	}
	
	public Section getSection(String name) {
		return selector.getSection(name);
	}
	
	
	public static interface Listener {

		void doShow();

		void doRemove();
		
	}
}
