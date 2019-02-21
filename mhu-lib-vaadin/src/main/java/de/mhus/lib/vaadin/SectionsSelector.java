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

import java.util.LinkedList;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.core.MObserverHandler;
import de.mhus.lib.vaadin.ui.Border;

public class SectionsSelector extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	public enum EVENTS {SECTION_CHANGEING,SECTION_CHANGED, ADDED, REMOVED, UPDATED};
	
	LinkedList<Section> sections = new LinkedList<SectionsSelector.Section>();
	private String bWidth = "100px";
	private String bHeight = "100px";
	private String bSelectedColor = "#AAA";
	private String bNotSelectedColor = "#999";
	private String bDisabledColor = "#555";
	private MObserverHandler<Event> eventHandler = new MObserverHandler<>();
	private boolean autoSelect = true; 
	
	public SectionsSelector() {
		setWidth(bWidth);
	}
	
	public Section addSection(String name, String title) {
		return addSection(name, title, null);
	}
	
	public Section addSection(String name, String title, Object userData) {
		Section section = getSection(name);
		if (section != null) {
			section.setTitle(title);
			if (userData != null) section.setUserData(userData);
			eventHandler.fireChanged(new Event(this,EVENTS.UPDATED,section));
		} else {
			section = new Section(name,title);
			section.setUserData(userData);
			eventHandler.fireChanged(new Event(this,EVENTS.ADDED,section));
			if (autoSelect) {
				if (sections.size() == 1) { // first one
					setSelected(name);
				}
			}
		}
		return section;
	}
	
	
	public Section getSection(String name) {
		synchronized (sections) {
			for (Section section : sections) {
				if (section.getName().equals(name)) return section;
			}
		}
		return null;
	}

	public Section getSelected() {
		synchronized (sections) {
			for (Section section : sections) {
				if (section.isSelected()) return section;
			}
		}
		return null;
	}

	public String getButtonWidth() {
		return bWidth;
	}


	public void setButtonWidth(String bWidth) {
		this.bWidth = bWidth;
		setWidth(bWidth);
	}


	public String getButtonHeight() {
		return bHeight;
	}


	public void setButtonHeight(String bHeight) {
		this.bHeight = bHeight;
	}


	public class Section implements ClickListener {

		private static final long serialVersionUID = 1L;
		private String name;
		private String title;
		private CardButton button;
		private boolean enabled = true;
		private boolean selected = false;
		private Object userData;

		public Section(String name, String title) {
			this.name = name;
			this.title = title;
			this.button = new CardButton(title);
			button.setWidth(bWidth);
			button.setHeight(bHeight);
			button.setBorder(new Border(1,1,1,1));
			button.setMargin(new Border(1,0,0,0));
			button.setData(name);
			button.addClickListener(this);
			doUpdate();
			
			synchronized (sections) {
				sections.add(this);
				SectionsSelector.this.addComponent(button);
			}
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
			button.setCaption(title);
		}
		
		@Deprecated
		public Button getButton() {
			return button;
		}

		private void setSelected(boolean selected) {
			this.selected = selected;
			doUpdate();
		}
		
		private void doUpdate() {
			if (!enabled) {
				button.setBackgroundColor(bDisabledColor);
				if (autoSelect && selected) {
					synchronized (sections) {
						for (Section section : sections) {
							if (section.isEnabled()) {
								section.setSelected(true);
								break;
							}
						}
					}
					selected = false;
				}
			} else
			if (selected)
				button.setBackgroundColor(bSelectedColor);
			else
				button.setBackgroundColor(bNotSelectedColor);
			button.updateCaption();
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
			doUpdate();
		}
		
		public boolean isEnabled() {
			return enabled;
		}

		public boolean isSelected() {
			return selected;
		}
		
		@Override
		public void buttonClick(ClickEvent event) {
			if (!enabled) return;
			SectionsSelector.this.setSelected(name);
		}

		public Object getUserData() {
			return userData;
		}

		public void setUserData(Object userData) {
			this.userData = userData;
		}
		
		
	}

	public Section setSelected(String name) {
		eventHandler.fireChanged(new Event(this,EVENTS.SECTION_CHANGEING,getSection(name)));
		Section sel = null;
		synchronized (sections) {
			for (Section section : sections) {
				if (section.getName().equals(name)) {
					section.setSelected(true);
					sel = section;
				} else {
					section.setSelected(false);
				}
						
			}
		}
		eventHandler.fireChanged(new Event(this,EVENTS.SECTION_CHANGED,getSection(name)));
		return sel;
	}
		
	public MObserverHandler<Event> eventHandler() {
		return eventHandler;
	}

	public Section removeSection(String name) {
		Section sec = getSection(name);
		if (autoSelect && sec.isSelected()) {
			synchronized (sections) {
				for (Section section : sections) {
					if (section.isEnabled()) {
						setSelected(section.getName());
						break;
					}
				}
			}
		}
		synchronized (sections) {
			if (sec == null) return null;
			sections.remove(sec);
		}
		
		
		eventHandler.fireChanged(new Event(this,EVENTS.REMOVED,sec));
		return sec;
	}
	
	public boolean isAutoSelect() {
		return autoSelect;
	}

	public void setAutoSelect(boolean autoSelect) {
		this.autoSelect = autoSelect;
	}


	public static class Event {
		private SectionsSelector source;
		private EVENTS event;
		private Section section;
		
		private Event(SectionsSelector source, EVENTS event, Section section) {
			this.source = source;
			this.event = event;
			this.section = section;
		}
		
		public SectionsSelector getSource() {
			return source;
		}
		public EVENTS getEvent() {
			return event;
		}
		public Section getSection() {
			return section;
		}
		
	}
}
