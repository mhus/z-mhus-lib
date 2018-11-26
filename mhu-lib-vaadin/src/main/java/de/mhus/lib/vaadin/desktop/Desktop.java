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
package de.mhus.lib.vaadin.desktop;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.TreeMap;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsProvider;

public class Desktop extends CssLayout implements MNlsProvider {

	private static final long serialVersionUID = 1L;
	private MenuBar menuBar;
	private MenuItem menuSpaces;
	private HorizontalLayout contentScreen;
	private MenuItem[] menuSpace = new MenuItem[4];
	private MenuItem menuLeave;
	protected MenuItem menuUser;
	@SuppressWarnings("unused")
	private MenuItem menuLogout;
	protected GuiSpaceService currentSpace;
	@SuppressWarnings("unused")
	private MenuItem menuOverview;
	private CssLayout overView;
	private MenuItem menuHistory;
	@SuppressWarnings("unused")
	private MenuItem menuBack;
	private static Log log = Log.getLog(Desktop.class);
	private LinkedList<String> history = new LinkedList<>();
	private TreeMap<String,GuiSpaceService> spaceList = new TreeMap<String, GuiSpaceService>();
	private HashMap<String, AbstractComponent> spaceInstanceList = new HashMap<String, AbstractComponent>();
	private HashMap<String, HelpContext> helpInstanceList = new HashMap<String, HelpContext>();
	private GuiApi api;
	private MNls nls;
	private int tileWidth = 200;
	private int tileHeight = 160;
	private int tileHorizontalGap = 20;
	private MenuItem menuHelp;
	private boolean helpActive;
	private VerticalLayout helpView;

	public Desktop(GuiApi api) {
		this.api = api;
		initGui();
	}

	protected void initGui() {
		
		helpView = new VerticalLayout();
		helpView.setSizeFull();
		helpView.setStyleName("help-panel");
		
		overView = new CssLayout();
		overView.setSizeFull();
		overView.setStyleName("overview");
		
		menuBar = new MenuBar();
		menuSpaces = menuBar.addItem(MNls.find(this, "menu.spaces=Spaces"), null);

		menuHistory = menuBar.addItem(MNls.find(this, "menu.history=History"), null);
		menuBack = menuHistory.addItem(MNls.find(this, "menu.back=Back"), new MenuBar.Command() {
			private static final long serialVersionUID = 1L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				navigateBack();
			}
			
		});
		menuHistory.addSeparator();
		for (int i = 0; i < 15; i++)
			menuHistory.addItem("", new MenuBar.Command() {

				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					String text = selectedItem.getDescription();
					if (MString.isSet(text)) {
						String[] parts = text.split("\\|", 4);
						if (parts.length == 4) {
							if (parts[2].equals("null")) parts[2] = null;
							if (parts[3].equals("null")) parts[3] = null;
							openSpace(parts[1], parts[2], parts[3]);
						}
					}
				}
				
			});
		
		menuSpace[0] = menuBar.addItem("", null);
		menuSpace[1] = menuBar.addItem("", null);
		menuSpace[2] = menuBar.addItem("", null);
		menuSpace[3] = menuBar.addItem("", null);
		
		menuUser = menuBar.addItem( "?", null);
		menuUser.setStyleName("right");
		menuLogout = menuUser.addItem(MNls.find(this, "menu.logout=Logout"), new MenuBar.Command() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				try {
					getApi().getAccessControl().signOut();
				} catch (Throwable t) {
					log.d(t);
				}
				try {
					UI.getCurrent().close();
				} catch (Throwable t) {
					log.d(t);
				}
				UI.getCurrent().getPage().reload();
			}
		});
		menuUser.addSeparator();
		menuHelp = menuUser.addItem(MNls.find(this, "menu.help=Help"), new MenuBar.Command() {
			private static final long serialVersionUID = 1L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				doShowHelp();
			}
		});
		menuHelp.setEnabled(false);
		menuUser.addSeparator();
		
		setStyleName("desktop-screen");
		menuBar.setStyleName("menubar");
		
		addComponent(menuBar);
		
		contentScreen = new HorizontalLayout();
		contentScreen.addStyleName("content");
		contentScreen.setSizeFull();
		addComponent(contentScreen);
		setSizeFull();
		
		showOverview(false);
	}

	protected void doShowHelp() {
		if (currentSpace == null) return;
		
		if (helpActive) {
			hideHelp();
			return;
		}
		
		showHelpTopic(null);
	}

	public void hideHelp() {
		synchronized (this) {
			if (!helpActive) return;
			helpView.removeAllComponents();
			contentScreen.removeComponent(helpView);
			helpActive = false;
		}
	}
	
	public void showHelpTopic(String topic) {
		
		HelpContext help = getHelpContext(currentSpace.getName());
		if (help == null) {
			menuHelp.setEnabled(false);
			return;
		}
		Component component = help.getComponent();
		if (component == null) return;

		synchronized (this) {
			if (!helpActive) {
				contentScreen.addComponent(helpView);
				contentScreen.setExpandRatio(helpView, 0.5f);
			};
			helpView.removeAllComponents();
			helpView.addComponent(component);
			
		}
		helpActive = true;
	}
	
	
	public void refreshSpaceList() {
		
		String name = "?";
		try {
			name = getApi().getAccessControl().getAccount().getDisplayName();
		} catch (Throwable t) {
			log.t(t);
		}
		menuUser.setText(name == null ? "?" : name);
		
		menuSpaces.removeChildren();
		overView.removeAllComponents();
		
		menuOverview = menuSpaces.addItem(MNls.find(this, "menu.overview=Overview"), new MenuBar.Command() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				showOverview(true);
			}
		});

		menuSpaces.addSeparator();
		
		LinkedList<GuiSpaceService> componentList = new LinkedList<>();
		for (GuiSpaceService space : spaceList.values()) {
			
			try {
				if (space.isHiddenSpace() || !hasAccess(space) || !space.hasAccess(getApi().getAccessControl())) continue;
				componentList.add(space);
			} catch (Throwable t) {
				log.d(space,t);
			}
		}

		Locale locale = null;
		try {
			locale =UI.getCurrent()
					.getPage()
					.getWebBrowser()
					.getLocale();
		} catch (Throwable t) {
			log.i(t);
			locale = Locale.getDefault();
		}

		for (final GuiSpaceService space : componentList ) {
			
			AbstractComponent tile = space.createTile();
			if (tile == null) {
				NativeButton button = new NativeButton();
				button.setCaptionAsHtml(false);
				button.setCaption( space.getDisplayName(locale));
				button.addClickListener(new NativeButton.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						openSpace(space.getName(), null, null); // not directly to support history
					}
				});
				tile = button;
				tile.addStyleName("cursorhand");
			}
			int tileSize = space.getTileSize();
			if (tileSize < 1) tileSize = 1;
			if (tileSize > 3) tileSize = 3;
			tile.addStyleName("thumbnail" + tileSize);
			tile.setWidth(( tileWidth * tileSize + (tileSize - 1) * tileHorizontalGap ) + "px");
			tile.setHeight( tileHeight + "px");
			overView.addComponent(tile);
			
			if (!space.isHiddenInMenu()) {
				MenuItem item = menuSpaces.addItem(space.getDisplayName(locale), new MenuBar.Command() {
					
					private static final long serialVersionUID = 1L;

					@Override
					public void menuSelected(MenuItem selectedItem) {
						openSpace(space.getName(), null, null); // not directly to support history
					}
				});
				item.setEnabled(true);
			}
		}
		
		if (componentList.size() > 0)
			menuSpaces.addSeparator();
		
		menuLeave = menuSpaces.addItem(MNls.find(this, "menu.leave=Leave Space"), new MenuBar.Command() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				if (currentSpace == null) return;
				removeSpaceComponent(currentSpace.getName());
				currentSpace = null;
				showOverview(true);
			}
		});
		menuLeave.setEnabled(false);

	}

	public void removeSpaceComponent(String name) {
		AbstractComponent c = spaceInstanceList.remove(name);
		if (c != null && c instanceof GuiLifecycle) ((GuiLifecycle)c).doDestroy();
	}

	protected boolean hasAccess(GuiSpaceService space) {
		return getApi().hasAccess(space.getName());
	}

	protected String showSpace(GuiSpaceService space, String subSpace, String search) {
		boolean exists = spaceInstanceList.containsKey(space.getName());
		AbstractComponent component = getSpaceComponent(space.getName());
		HelpContext help = getHelpContext(space.getName());
		
		menuHelp.setEnabled(false);
		helpActive = false;
		
		contentScreen.removeAllComponents();
		cleanupMenu();
		if (component == null) {
			contentScreen.addComponent(new Label(MNls.find(this, "spaces.unavailable=Space currently not available") + space.getName()));
			addComponent(contentScreen);
			return null;
		}
		
		component.setSizeFull();
		contentScreen.addComponent(component);
		contentScreen.setExpandRatio(component, 1f);
		
		Locale locale = UI.getCurrent().getPage().getWebBrowser().getLocale();

		menuHistory.setText(space.getDisplayName(locale));
		menuLeave.setEnabled(true);
		currentSpace = space;
		space.createMenu(component,menuSpace);
		
		if (help != null) {
			menuHelp.setEnabled(true);
		}
		
		if (component instanceof Navigable) {
			if ( (MString.isSet(subSpace) || MString.isSet(search))) 
				return ((Navigable)component).navigateTo(subSpace, search);
			else
				((Navigable)component).onShowSpace(!exists);
		}
		return space.getDisplayName(locale);
	}

	protected void showOverview(boolean setLinking) {
		if (menuLeave != null) menuLeave.setEnabled(false);
		contentScreen.removeAllComponents();
		cleanupMenu();
		currentSpace = null;
		contentScreen.addComponent(overView);
		contentScreen.setExpandRatio(overView, 1f);

		menuHistory.setText(MNls.find(this, "menu.history=History"));
		if (setLinking)
			UI.getCurrent().getPage().setUriFragment("");
	}

	private void cleanupMenu() {
		
		for (int i=0; i < menuSpace.length; i++) {
			menuSpace[i].removeChildren();
			menuSpace[i].setText("");
			menuSpace[i].setVisible(false);
		}
	}

	public void doUpdateHistoryMenu() {
		int cnt = -2;
		for (MenuItem c : menuHistory.getChildren()) {
			if (cnt >= 0) {
				if (history.size() - cnt - 1 < 0) {
					c.setText("");
					c.setDescription("");
					c.setIcon(null);
				} else {
					String x = history.get(history.size() - cnt - 1);
					c.setText(MString.beforeIndex(x, '|'));
					c.setDescription(x);
					c.setIcon(VaadinIcons.ARROW_RIGHT); //  FontAwesome.ARROW_RIGHT
				}
			}
			cnt++;
		}
	}

	public void rememberNavigation(String caption, String space, String subSpace, String search, boolean navLink) {
		String newEntry = caption.replace('|', ' ') + "|" + space + "|" + subSpace + "|" + search;
		while (this.history.remove(newEntry) ) {} // move up
		this.history.add(newEntry);
		doUpdateHistoryMenu();
		if (navLink)
			UI.getCurrent().getPage().setUriFragment("!:" + space + "/" + (subSpace == null ? "" : subSpace) + "/" + (search == null ? "" : search));
	}

	public boolean openSpace(String spaceId, String subSpace, String search) {
		return openSpace(spaceId, subSpace, search, true, true);
	}
	
	public boolean openSpace(String spaceId, String subSpace, String search, boolean history, boolean navLink) {
		GuiSpaceService space = getSpace(spaceId);
		if (space == null) return false;
		if (!getApi().hasAccess(space.getName()) || !space.hasAccess(getApi().getAccessControl())) return false;

		String ret = showSpace(space, subSpace, search);
		if (ret != null && history) {
			String newEntry = ret.replace('|', ' ') + "|" + spaceId + "|" + subSpace + "|" + search;
			while (this.history.remove(newEntry) ) {} // move up
			this.history.add(newEntry);
			doUpdateHistoryMenu();
		}
		if (navLink)
			UI.getCurrent().getPage().setUriFragment("!:" + spaceId + "/" + (subSpace == null ? "" : subSpace) + "/" + (search == null ? "" : search));
		return ret != null;
	}

	public void navigateBack() {
		if (history.size() == 0) return;
		String link = history.removeLast();
		if (history.size() == 0) return;
		link = history.getLast();
		doUpdateHistoryMenu();
		String[] parts = link.split("\\|", 4);
		if (parts[2].equals("null")) parts[2] = null;
		if (parts[3].equals("null")) parts[3] = null;
		openSpace(parts[1], parts[2], parts[3], false, true);
	}

	public GuiSpaceService getSpace(String name) {
		return spaceList.get(name);
	}

	public GuiApi getApi() {
		return api;
	}

	public void close() {
		spaceList.clear();
		for (AbstractComponent v : spaceInstanceList.values())
			if (v instanceof GuiLifecycle) ((GuiLifecycle)v).doDestroy();
		spaceInstanceList.clear();
	}

	public void addSpace(GuiSpaceService service) {
		log.d("add space", service);
		spaceList.put(service.getName(),service);
		refreshSpaceList();
	}

	public void removeSpace(GuiSpaceService service) {
		log.d("remove space",service);
		spaceList.remove(service.getName());
		AbstractComponent v = spaceInstanceList.remove(service.getName());
		if (v != null && v instanceof GuiLifecycle) ((GuiLifecycle)v).doDestroy();
		refreshSpaceList();
	}
	
	public AbstractComponent getSpaceComponent(String name) {
		GuiSpaceService space = spaceList.get(name);
		if (space == null) return null;
		AbstractComponent instance = spaceInstanceList.get(name);
		if (instance == null) {
			instance = space.createSpace();
			if (instance == null) return null;
			if (instance instanceof GuiLifecycle) ((GuiLifecycle)instance).doInitialize();
			spaceInstanceList.put(name, instance);
		}
		return instance;
	}

	public HelpContext getHelpContext(String name) {
		GuiSpaceService space = spaceList.get(name);
		if (space == null) return null;
		HelpContext instance = helpInstanceList.get(name);
		if (instance == null) {
			Locale locale = UI.getCurrent().getPage().getWebBrowser().getLocale();
			instance = space.createHelpContext(locale);
			if (instance == null) return null;
			if (instance instanceof GuiLifecycle) ((GuiLifecycle)instance).doInitialize();
			helpInstanceList.put(name, instance);
		}
		return instance;
	}
	
	@Override
	public MNls getNls() {
		if (nls == null) nls = MNls.lookup(this);
		return nls;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}

	public int getTileHorizontalGap() {
		return tileHorizontalGap;
	}

	public void setTileHorizontalGap(int tileHorizontalGap) {
		this.tileHorizontalGap = tileHorizontalGap;
	}

}
