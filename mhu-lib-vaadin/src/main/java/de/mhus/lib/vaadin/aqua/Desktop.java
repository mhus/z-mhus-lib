package de.mhus.lib.vaadin.aqua;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.util.MNls;

public class Desktop extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private Navigator nav;
	private CssLayout content;
    private CssLayout menu;
    private Connector connector;
    private MNls nls;
    private MenuItem settingsMenu;
	private Command settingsMenuCmd;
	private HelpManager helpManager;

    LinkedList<ViewDefinition> views = new LinkedList<>();
    HashMap<String,ViewDefinition> currentViews = new HashMap<>();
    

	public void initUI() {
		
		if (nav != null) return; // for secure only - not synchronized
		
		helpManager = new HelpManager(getUI());
		
		content = new CssLayout();
		nav = new Navigator(getUI(), content);
        
		addComponent(content);
		content.setSizeFull();
        content.addStyleName("view-content");
        setExpandRatio(content, 1);

        menu = new CssLayout();

        addComponent(new HorizontalLayout() {
			private static final long serialVersionUID = 1L;

			{
                setSizeFull();
                addStyleName("main-view");
                addComponent(new VerticalLayout() {
					private static final long serialVersionUID = 1L;

					// Sidebar
                    {
                        addStyleName("sidebar");
                        setWidth(null);
                        setHeight("100%");

                        // Branding element
                        addComponent(new CssLayout() {
							private static final long serialVersionUID = 1L;
							{
                                addStyleName("branding");
                                Label logo = new Label(
                                        "<span>" + MNls.find(nls, "appname1=") + "</span><br/>" + MNls.find(nls, "appname2="),
                                        ContentMode.HTML);
                                logo.setSizeUndefined();
                                addComponent(logo);
                            }
                        });

                        // Main menu
                        addComponent(menu);
                        setExpandRatio(menu, 1);

                        // User menu
                        addComponent(new VerticalLayout() {
							private static final long serialVersionUID = 1L;

							{
                                setSizeUndefined();
                                addStyleName("user");
                                Image profilePic = new Image(
                                        null,
                                        new ThemeResource("img/profile-pic.png"));
                                profilePic.setWidth("34px");
                                addComponent(profilePic);
                                Label userName = new Label(connector.getUserName());
                                userName.setSizeUndefined();
                                addComponent(userName);

                                settingsMenuCmd = new MenuBar.Command() {
									private static final long serialVersionUID = 1L;

									@Override
									public void menuSelected(
											MenuItem selectedItem) {
										connector.doSettingsMenuAction(selectedItem);
	                                    }
		
									};
                                MenuBar settings = new MenuBar();
                                settingsMenu = settings.addItem("",null);
                                settingsMenu.setStyleName("icon-cog");
                                doUpdateSettingsMenu();
                                addComponent(settings);

                                Button exit = new NativeButton("Exit");
                                exit.addStyleName("icon-cancel");
                                exit.setDescription(MNls.find(nls, "logout=Sign Out"));
                                addComponent(exit);
                                exit.addClickListener(new Button.ClickListener() {

									private static final long serialVersionUID = 1L;

									@Override
									public void buttonClick(Button.ClickEvent event) {
                                    	connector.doLogout();
                                    }
                                });
                            }
                        });
                    }
                });
                // Content
                addComponent(content);
                content.setSizeFull();
                content.addStyleName("view-content");
                setExpandRatio(content, 1);
            }

        });

        menu.removeAllComponents();
        menu.addStyleName("menu");
        menu.setHeight("100%");
        
        doUpdateStructure();

        String f = Page.getCurrent().getUriFragment();
        if (f == null) f = "";
        if (f.startsWith("!")) {
            f = f.substring(1);
        }
        
        String defName = f.length() < 1 ? f : MString.beforeIndex(f.substring(1), '/');
        ViewDefinition def = getDefinition(defName);
        
        if (def == null || f.equals("") || f.equals("/")) {
        	defName = connector.getDefaultViewName();
        	def = getDefinition(defName);
        }
        
        if (def != null) {
            nav.navigateTo("/" + defName );
            def.b.addStyleName("selected");
            def.showHelp(helpManager);
        }

        nav.addViewChangeListener(new ViewChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
            public boolean beforeViewChange(ViewChangeEvent event) {
                helpManager.closeAll();
				if (event.getNewView() instanceof DesktopView)
					((DesktopView)event.getNewView()).doInitWithDesktop(Desktop.this, event.getParameters());
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {
            	String n = event.getViewName();
                String defName = MString.beforeIndex(n.substring(1), '/');
                ViewDefinition def = getDefinition(defName);
                if (def != null) {
                	def.showHelp(helpManager);
                }
  //              View newView = event.getNewView();
                //helpManager.showHelpFor(newView);
            }
        });

        doUpdateSettingsMenu();
        
	}
	
	public void doUpdateSettingsMenu() {
		settingsMenu.removeChildren();
		List<String> list = connector.getSettingsMenuCaptions();
		if (list == null) return;
		for (String caption : list) {
			if (caption.equals("-"))
				settingsMenu.addSeparator();
			else
				settingsMenu.addItem(caption, settingsMenuCmd);
		}
	}
	
	public void doUpdateStructure() {
		
		// remove all
		for (String name : currentViews.keySet())
			nav.removeView(name);
		
		currentViews.clear();
		
		menu.removeAllComponents();
		
		// create all
		for (final ViewDefinition def : views) {
			currentViews.put(def.getName(), def);
			if (def.getView() == null)
				nav.addView("/" + def.getName(), def.getViewClass());
			else
				nav.addView("/" + def.getName(), def.getView());
				
            Button b = new NativeButton(def.getCaption());
            def.setNavButton(b);
            if (def.getIconName() != null) b.addStyleName("icon-" + def.getIconName());
            b.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
                public void buttonClick(Button.ClickEvent event) {
                    clearMenuSelection();
                    event.getButton().addStyleName("selected");
                    if (!nav.getState().equals("/" + def.getName()))
                        nav.navigateTo("/" + def.getName());
                }
            });
            b.setHtmlContentAllowed(true);
            def.updateCaption();

            menu.addComponent(b);

			
		}
		
	}
	
    private void clearMenuSelection() {
        for (@SuppressWarnings("deprecation")
		Iterator<Component> it = menu.getComponentIterator(); it.hasNext();) {
            Component next = it.next();
            if (next instanceof NativeButton) {
                next.removeStyleName("selected");
            } else if (next instanceof DragAndDropWrapper) {
                // Wow, this is ugly (even uglier than the rest of the code)
                ((DragAndDropWrapper) next).iterator().next()
                        .removeStyleName("selected");
            }
        }
    }

	public Connector getConnector() {
		return connector;
	}

	public void setConnector(Connector connector) {
		this.connector = connector;
	}

	public MNls getNls() {
		return nls;
	}

	public void setNls(MNls nls) {
		this.nls = nls;
	}

	public ViewDefinition getDefinition(String name) {
		ViewDefinition ret = currentViews.get(name);
		if (ret == null) {
			for (ViewDefinition def : views)
				if (def.getName().equals(name)) return def;
		}
		return ret;
	}
	
	public List<ViewDefinition> getViewDefinitions() {
		return views;
	}
	
	
	public static interface Connector {
		String getUserName();

		String getDefaultViewName();

		List<String> getSettingsMenuCaptions();

		void doSettingsMenuAction(MenuItem selectedItem);

		void doLogout();
	}
	
	public static class ViewDefinition {
		
		private String name;
		private String caption;
		private Class<? extends View> viewClass;
		
		private int badge;
		private Button b;
		private String iconName;
		private View view;
		private String helpStyle;
		private String help;
		
		public void setView(View view) {
			this.view = view;
		}
		
		public void setHelp(String help) {
			this.help = help;
		}
		
		public void setHelpStyle(String style) {
			helpStyle = style;
		}
		
		public void showHelp(HelpManager helpManager) {
			String help = getHelp();
			if (help != null)
				helpManager.showHelp(help, getCaption(), getHelpStyle());
		}

		private String getHelpStyle() {
			return helpStyle;
		}

		private String getHelp() {
			return help;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		public void setCaption(String caption) {
			this.caption = caption;
		}
		
		public void setViewClass(Class<? extends View> clazz) {
			viewClass = clazz;
		}
		
		public void setBadge(int badge) {
			this.badge = badge;
		}
		
		public void setIconName(String name) {
			iconName = name;
		}
		
		public String getName() {
			return name;
		}
		
		public void updateCaption() {
            if (getBadge() != 0) {
                b.setCaption(getCaption() + "<span class=\"badge\">" + getBadge() +"</span>");
            } else {
            	b.setCaption(getCaption());
            }
		}
		
		private void setNavButton(Button b) {
			this.b = b;
		}
		public int getBadge() {
			return badge;
		}
		public String getIconName() {
			return iconName;
		}
		public String getCaption() {
			return caption;
		}
		public Class<? extends View> getViewClass() {
			return viewClass;
		}
		public View getView() {
			return view;
		}
	}

	public void doSelect(String name) {
		ViewDefinition def = currentViews.get(name);
		if (def == null) return;
		nav.navigateTo("/" + name);
		clearMenuSelection();
        def.b.addStyleName("selected");
	}
	
}
